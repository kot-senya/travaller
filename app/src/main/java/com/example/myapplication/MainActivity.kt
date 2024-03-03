package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.ADAPTER.AdapterCity
import com.example.myapplication.BASE.StaticData
import com.example.myapplication.BASE.Supabase
import com.example.myapplication.DATA_CLASS.City
import com.example.myapplication.DATA_CLASS.District
import com.example.myapplication.DATA_CLASS.Region
import com.example.myapplication.databinding.ActivityMainBinding
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.withTimeoutOrNull
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private var _bind: ActivityMainBinding? = null
    private val bind: ActivityMainBinding get() = _bind!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)

        lifecycleScope.launch {
            getData()
        }
    }
    private suspend fun getData() = withContext(Dispatchers.Main) {
        runCatching {
            StaticData.region = Supabase.client().postgrest.from("tbRegion").select().decodeList<Region>().toMutableList()
            StaticData.district = Supabase.client().postgrest.from("tbDistrict").select().decodeList<District>().toMutableList()
            StaticData.city =
                Supabase.client().postgrest.from("tbCity").select().decodeList<City>()
                    .toMutableList()
        }.onSuccess {
            StaticData.city_popular = StaticData.city.filter { it.popular }.toMutableList()
            loadRVCity(StaticData.city)
            loadRVPopularCity(StaticData.city_popular)
        }.onFailure {
            Log.e("Supabse", "Ошибка получения данных :${it}")
        }
    }

    private fun loadRVPopularCity(collection: MutableList<City>) {
        bind.rvPopular.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        bind.rvPopular.adapter = AdapterCity(collection, false)
    }

    private fun loadRVCity(collection: MutableList<City>) {
        bind.rvCity.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        bind.rvCity.adapter = AdapterCity(collection, true)
    }

}
