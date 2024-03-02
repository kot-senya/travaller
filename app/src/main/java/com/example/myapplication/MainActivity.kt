package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.ADAPTER.AdapterCity
import com.example.myapplication.BASE.Connection
import com.example.myapplication.DATA_CLASS.City
import com.example.myapplication.databinding.ActivityMainBinding
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private var _bind: ActivityMainBinding? = null
    private val bind: ActivityMainBinding get() = _bind!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)
        getData()
    }

    private fun getData() = runBlocking {
        CoroutineScope(Dispatchers.IO).launch {
            try {

                val l =  Connection.supabase.postgrest.from("tbCity").select().decodeList<City>()
                l.forEach {
                    Log.d("Data",it.name)
                }
                //loadRVCity(l)
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.println(Log.ERROR,"GetData", e.toString()) // Вывод в консоль для отладки
                }
            }
            //Connection.getCity(client)
            /* Connection.getPopularCity(client)
             Connection.getRegion(client)
             Connection.getPopularCity(client)*/
        }
        //loadRVPopularCity(StaticData.city_popular)
        //loadRVCity(StaticData.city)
    }

    private fun loadRVPopularCity(collection: MutableList<City>) {
        bind.rvPopular.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        bind.rvPopular.adapter = AdapterCity(collection, false)
    }

    private fun loadRVCity(collection: MutableList<City>) {
        bind.rvPopular.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        bind.rvPopular.adapter = AdapterCity(collection, true)
    }
}