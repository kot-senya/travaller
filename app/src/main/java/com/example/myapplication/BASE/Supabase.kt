package com.example.myapplication.BASE

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

object Supabase {

    private val supabaseUrl = "https://fcevwykqmelsbzldmygn.supabase.co"
    private val supabaseKey =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImZjZXZ3eWtxbWVsc2J6bGRteWduIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MDkzMDIzOTQsImV4cCI6MjAyNDg3ODM5NH0.qlbO41Vht-SqnCglFONq4kvqdQhzpA7cEq0uTwbyj2Q"

    fun client():SupabaseClient{
        return createSupabaseClient(supabaseUrl, supabaseKey) {
            install(Postgrest){

            }
        }
    }
}
