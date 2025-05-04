package com.example.khadra.data.repository

import android.net.Uri
import android.os.Build
import android.util.Log
import android.util.Patterns
import androidx.annotation.RequiresApi
import com.example.khadra.data.model.User
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage
import io.ktor.client.statement.readBytes
import io.ktor.http.ContentType
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.datetime.toJavaInstant
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import java.util.UUID

@Singleton
class AuthRepository @Inject constructor(
    private val client: SupabaseClient
) {
    companion object {
        private const val TAG = "AuthRepository"
        private const val BUCKET_NAME = "avatars"
        private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    suspend fun signUp(email: String, password: String) {
        try {
            if (!isValidEmail(email)) {
                throw IllegalArgumentException("Invalid email format")
            }
            if (password.length < 6) {
                throw IllegalArgumentException("Password must be at least 6 characters")
            }

            Log.d(TAG, "Attempting signup for user: $email")
            client.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
        } catch (e: Exception) {
            Log.e(TAG, "Signup failed: ${e.message}")
            throw e
        }
    }

    suspend fun login(email: String, password: String) {
        try {
            if (!isValidEmail(email)) {
                throw IllegalArgumentException("Invalid email format")
            }
            
            Log.d(TAG, "Attempting login for user: $email")
            try {
                client.auth.signInWith(Email) {
                    this.email = email
                    this.password = password
                }
            } catch (e: RestException) {
                when {
                    e.message?.contains("invalid_credentials") == true -> 
                        throw IllegalArgumentException("Invalid email or password")
                    e.message?.contains("Email not confirmed") == true -> 
                        throw IllegalArgumentException("Please confirm your email first")
                    else -> throw e
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Login failed: ${e.message}")
            throw e
        }
    }

    suspend fun logout() {
        try {
            Log.d(TAG, "Logging out user")
            client.auth.signOut()
        } catch (e: Exception) {
            Log.e(TAG, "Logout failed: ${e.message}")
            throw e
        }
    }

    suspend fun resetPassword(email: String) {
        try {
            if (!isValidEmail(email)) {
                throw IllegalArgumentException("Invalid email format")
            }
            
            Log.d(TAG, "Sending password reset email to: $email")
            client.auth.resetPasswordForEmail(email)
        } catch (e: Exception) {
            Log.e(TAG, "Password reset failed: ${e.message}")
            throw e
        }
    }

    suspend fun updateProfilePicture(imageUri: Uri) {
        try {
            val session = client.auth.currentSessionOrNull() ?: throw IllegalStateException("No active session")
            val userId = session.user?.id ?: throw IllegalStateException("No user ID found")
            val fileName = "$userId-${UUID.randomUUID()}.jpg"

            val imageBytes = imageUri.toByteArray()
            client.storage[BUCKET_NAME].upload(fileName, imageBytes) {
                contentType = ContentType.Image.JPEG
            }

            val publicUrl = client.storage[BUCKET_NAME].publicUrl(fileName)

            client.postgrest["profiles"].update({
                set("avatar_url", publicUrl)
            }) {
                filter {
                    eq("id", userId)
                }
            }

            Log.d(TAG, "Profile picture updated successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to update profile picture: ${e.message}")
            throw e
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getCurrentUser(): User? {
        return try {
            val session = client.auth.currentSessionOrNull() ?: return null
            val user = session.user ?: return null

            val profile = client.postgrest["profiles"]
                .select { filter { eq("id", user.id ?: return null) } }
                .decodeSingleOrNull<Map<String, Any?>>()

            User(
                id = user.id ?: return null,
                email = user.email ?: "",
                createdAt = user.createdAt?.toJavaInstant()?.toEpochMilli()?.let { dateFormat.format(it) } ?: "",
                updatedAt = user.updatedAt?.toJavaInstant()?.toEpochMilli()?.let { dateFormat.format(it) } ?: "",
                fullName = profile?.get("full_name")?.toString(),
                avatarUrl = profile?.get("avatar_url")?.toString(),
                treesPlanted = (profile?.get("trees_planted") as? Number)?.toInt() ?: 0,
                treesWatered = (profile?.get("trees_watered") as? Number)?.toInt() ?: 0
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error getting current user: ${e.message}")
            null
        }
    }

    private suspend fun Uri.toByteArray(): ByteArray {
        return client.httpClient.get(this.toString()).readBytes()
    }
}