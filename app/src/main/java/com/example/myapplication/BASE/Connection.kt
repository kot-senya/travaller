package com.example.myapplication.BASE

import com.example.myapplication.DATA_CLASS.City
import com.example.myapplication.DATA_CLASS.District
import com.example.myapplication.DATA_CLASS.Region
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest

object Connection {

    val supabase  =  createSupabaseClient(
    supabaseUrl = "https://fcevwykqmelsbzldmygn.supabase.co",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImZjZXZ3eWtxbWVsc2J6bGRteWduIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MDkzMDIzOTQsImV4cCI6MjAyNDg3ODM5NH0.qlbO41Vht-SqnCglFONq4kvqdQhzpA7cEq0uTwbyj2Q"
    )
    {
        install(Postgrest)
    }


    suspend fun getCity(client: SupabaseClient) {
        StaticData.city = client.postgrest["tbCity"].select().decodeList<City>().toMutableList()
    }

    suspend fun getPopularCity(client: SupabaseClient) {
        StaticData.city_popular =
            client.postgrest["tbCity"].select().decodeList<City>().filter { it.popular }
                .toMutableList()
    }

    suspend fun getRegion(client: SupabaseClient) {
        StaticData.region =
            client.postgrest["tbRegion"].select().decodeList<Region>().toMutableList()
    }

    suspend fun getDistrict(client: SupabaseClient) {
        StaticData.district =
            client.postgrest["tbDistrict"].select().decodeList<District>().toMutableList()
    }

}