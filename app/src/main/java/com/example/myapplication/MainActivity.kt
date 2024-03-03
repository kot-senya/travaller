package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import androidx.appcompat.app.AppCompatActivity
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {
    private var _bind: ActivityMainBinding? = null
    private val bind: ActivityMainBinding get() = _bind!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)/*
        var animation: RotateAnimation = RotateAnimation(-10f, 10f, 0f, 0f)
        animation.repeatMode = Animation.REVERSE
        animation.setRepeatCount(Animation.INFINITE)
        animation.interpolator = LinearInterpolator()
        animation.setDuration(450L)*/
        bind.imageDialog1.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animation_center))
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
            bind.dialog1.visibility = View.GONE
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
