package com.example.khadra.data.remote

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.example.khadra.data.model.Tree
import com.example.khadra.data.model.IrrigationHistory
import com.example.khadra.data.model.Location
import com.example.khadra.data.source.TreeDataSource
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.storage.storage
import io.ktor.http.ContentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.util.Log
import io.github.jan.supabase.exceptions.RestException
import io.ktor.client.utils.EmptyContent.contentType
import java.io.ByteArrayOutputStream
import java.util.UUID
import javax.inject.Inject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone


class SupabaseTreeDataSource @Inject constructor(
    private val client: SupabaseClient,
    private val context: Context
) : TreeDataSource {

    companion object {
        private const val TAG = "SupabaseTreeDataSource"
        private const val TABLE_NAME = "trees"
        private const val BUCKET_NAME = "tree-images"
        private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
    }

    override suspend fun getTrees(): List<Tree> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Fetching trees from Supabase table: $TABLE_NAME")
            val result = client.postgrest[TABLE_NAME].select().decodeList<Tree>()
            Log.d(TAG, "Successfully fetched ${result.size} trees from Supabase")
            result
        } catch (e: RestException) {
            Log.e(TAG, "Error fetching trees from Supabase: ${e.message}", e)
            emptyList()
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching trees from Supabase", e)
            throw e
        }
    }

    override suspend fun addTree(tree: Tree, imageUri: Uri?): Tree = withContext(Dispatchers.IO) {
        try {
            val treeToAdd = if (imageUri != null) {
                val imageUrl = uploadImage(imageUri)
                tree.copy(imageUrl = imageUrl)
            } else {
                tree
            }
            
            client.postgrest[TABLE_NAME].insert(treeToAdd).decodeSingle()
        } catch (e: Exception) {
            Log.e(TAG, "Error adding tree to Supabase", e)
            throw e
        }
    }

    override suspend fun updateTree(tree: Tree): Tree = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Updating tree in Supabase: $tree")

            val updatedTree = client.postgrest[TABLE_NAME]
                .update(
                    {
                        set("name", tree.name)
                        set("type", tree.type)
                        set("status", tree.status)
                        set("location", tree.location)
                        set("coordinates_lat", tree.coordinatesLat)
                        set("coordinates_lng", tree.coordinatesLng)
                        set("last_irrigation_action", dateFormat.format(tree.lastIrrigationAction))
                        set("updated_at", dateFormat.format(tree.updatedAt))
                        set("url_image", tree.imageUrl)
                    }
                ) {
                    filter {
                        eq("id", tree.id)
                    }
                }
                .decodeSingle<Tree>()

            Log.d(TAG, "Tree updated successfully: $updatedTree")
            updatedTree
        } catch (e: RestException) {
            Log.e(TAG, "Error updating tree in Supabase: ${e.message}", e)
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Error updating tree in Supabase", e)
            throw e
        }
    }


    override suspend fun deleteTree(treeId: String): Unit = withContext(Dispatchers.IO) {
        try {
            client.postgrest[TABLE_NAME]
                .delete {
                    filter {
                        eq("id", treeId)
                    }
                }
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting tree from Supabase", e)
            throw e
        }
    }


    override suspend fun getCurrentLocation(): Location {
        throw NotImplementedError("getCurrentLocation should be implemented in LocationDataSource")
    }

    override suspend fun addIrrigationHistory(history: IrrigationHistory): IrrigationHistory = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Adding irrigation history: $history")
            val formattedHistory = mapOf(
                "tree_id" to history.treeId,
                "irrigation_date" to dateFormat.format(history.irrigationDate),
                "notes" to history.notes,
                "created_at" to dateFormat.format(history.createdAt),
                "updated_at" to dateFormat.format(history.updatedAt)
            )
            
            val result = client.postgrest["irrigation_history"]
                .insert(formattedHistory) {
                    select()
                }
                .decodeSingle<IrrigationHistory>()
            Log.d(TAG, "Irrigation history added successfully: $result")
            result
        } catch (e: Exception) {
            Log.e(TAG, "Error adding irrigation history to Supabase", e)
            throw e
        }
    }

    override suspend fun getIrrigationHistory(treeId: String): List<IrrigationHistory> = withContext(Dispatchers.IO) {
        try {
            client.postgrest["irrigation_history"]
                .select {
                    filter {
                        eq("tree_id", treeId)
                    }
                    order("irrigation_date", Order.DESCENDING)
                }
                .decodeList()
        } catch (e: Exception) {
            Log.e(TAG, "Error getting irrigation history from Supabase", e)
            throw e
        }
    }


    private suspend fun uploadImage(imageUri: Uri): String = withContext(Dispatchers.IO) {
        try {
            val inputStream = context.contentResolver.openInputStream(imageUri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            val imageBytes = outputStream.toByteArray()

            val fileName = "${UUID.randomUUID()}.jpg"
            client.storage[BUCKET_NAME].upload(fileName, imageBytes) {
                contentType = ContentType.Image.JPEG
            }

            val publicUrl = client.storage[BUCKET_NAME].publicUrl(fileName)
            Log.d(TAG, "Image uploaded successfully: $publicUrl")
            publicUrl
        } catch (e: Exception) {
            Log.e(TAG, "Error uploading image to Supabase storage", e)
            throw e
        }
    }
}