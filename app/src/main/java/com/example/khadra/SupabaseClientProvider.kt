package com.example.khadra

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.auth.Auth
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SupabaseClientProvider @Inject constructor() {
    val client: SupabaseClient by lazy {
        createSupabaseClient(
            supabaseUrl = "https://tonpwdcvihmsxiblnbis.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InRvbnB3ZGN2aWhtc3hpYmxuYmlzIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDUyNTI0NzcsImV4cCI6MjA2MDgyODQ3N30.EluEB2twOF1hgp0zmzcmEwfVyRQMiARPORBi34F8SbI"
        ) {
            install(Postgrest)
            install(Auth)
            install(Realtime)
            install(Storage)
        }
    }

    companion object {
        private const val BUCKET_NAME = "tree-images"
    }
}
