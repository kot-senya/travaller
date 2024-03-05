package com.example.myapplication

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.ADAPTER.AdapterCityImage
import com.example.myapplication.ADAPTER.AdapterPlace
import com.example.myapplication.BASE.StaticData
import com.example.myapplication.BASE.Supabase
import com.example.myapplication.DATA_CLASS.CityImage
import com.example.myapplication.DATA_CLASS.Place
import com.example.myapplication.DATA_CLASS.PlaceImage
import com.example.myapplication.DATA_CLASS.UserFavorite
import com.example.myapplication.OTHER.Other
import com.example.myapplication.databinding.ActivityCityGuideBinding
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CityGuideActivity : AppCompatActivity() {
    private var _bind: ActivityCityGuideBinding? = null
    private val bind: ActivityCityGuideBinding get() = _bind!!

    private var _pref: SharedPreferences? = null
    private val pref: SharedPreferences get() = _pref!!
    private val city_id: Int = StaticData.city_guide_id
    private var city_place: MutableList<Place> = mutableListOf()
    private var city_image: MutableList<CityImage> = mutableListOf()
    private var place_image: MutableList<PlaceImage> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _bind = ActivityCityGuideBinding.inflate(layoutInflater)
        _pref = getSharedPreferences("Travel", MODE_PRIVATE)
        setContentView(bind.root)

        setVisibility(View.GONE)
        setData()
        setListener()
        lifecycleScope.launch {
            getData()
        }
    }

    private fun setVisibility(value: Int) {
        bind.rvCityImage.visibility = value
        bind.rvObject.visibility = value
        bind.rvFood.visibility = value
        bind.rvOther.visibility = value
        bind.tvObject.visibility = value
        bind.tvFood.visibility = value
        bind.tvOther.visibility = value
    }

    private fun setData() {
        bind.tvCityName.text = StaticData.city.filter { it.id == city_id }.first().name
        changeViewFavorite()

        bind.dialog1.visibility = View.VISIBLE
        bind.imageDialog1.startAnimation(Other.rotateAnimation())
        Other.countDownTimer_load(bind.textviewDialog1).start()
    }

    private fun changeViewFavorite() {
        if (!StaticData.city_favorite.filter { it.idCity == city_id }.toMutableList().isEmpty()) {
            bind.btnFavorite.setImageResource(R.drawable.ic_favorite_city1)
        } else {
            bind.btnFavorite.setImageResource(R.drawable.ic_favorite_city)
        }
    }

    private fun setListener() {
        bind.btnBack.setOnClickListener {
            StaticData.city_guide_id = -1
            startActivity(Intent(this@CityGuideActivity, MainActivity::class.java))
        }

        bind.btnFavorite.setOnClickListener {
            lifecycleScope.launch {
                runCatching {
                    val newRecord = UserFavorite(
                        id = 0,
                        idUser = pref.getInt("UserId", 0),
                        idCity = city_id
                    )
                    if (StaticData.getFavorite(newRecord)) {
                        Supabase.client().postgrest.from("tbUserFavorite").insert(newRecord)
                        newRecord.id =
                            Supabase.client().postgrest.from("tbUserFavorite").select()
                                .decodeList<UserFavorite>()
                                .filter { it.idCity == newRecord.idCity && it.idUser == it.idUser }
                                .first().id
                        StaticData.city_favorite.add(newRecord)
                    } else {
                        newRecord.id =
                            Supabase.client().postgrest.from("tbUserFavorite").select()
                                .decodeList<UserFavorite>()
                                .filter { it.idCity == newRecord.idCity && it.idUser == it.idUser }
                                .first().id
                        StaticData.city_favorite.remove(newRecord)
                        Supabase.client().postgrest.from("tbUserFavorite")
                            .delete { filter { eq("id", newRecord.id) } }
                    }
                }.onSuccess {
                    changeViewFavorite()
                }.onFailure {
                    Log.e("Supabse", "Ошибка отправки/удаления выбранного города :${it}")
                }
            }
        }
    }

    private suspend fun getData() = withContext(Dispatchers.Main) {
        var flag_guide: Boolean = true
        runCatching {
            city_place.clear()
            city_place = Supabase.client().postgrest.from("tbPlace").select().decodeList<Place>()
                .filter { it.idCity == city_id }.toMutableList()

            if (!city_place.isEmpty()) {
                place_image = Supabase.client().postgrest.from("tbPlaceImage").select()
                    .decodeList<PlaceImage>().toMutableList()
                var _place_i: MutableList<PlaceImage> = mutableListOf()
                for (image in place_image) {
                    var i: Int = city_place.filter { it.id == image.idPlace }.size
                    if (i > 0) _place_i.add(image)
                }
                place_image = _place_i
                city_image =
                    Supabase.client().postgrest.from("tbCityImage").select().decodeList<CityImage>()
                        .filter { it.idCity == city_id }.toMutableList()
            } else flag_guide = false

        }.onSuccess {
            bind.dialog1.visibility = View.GONE
            if (flag_guide) {
                if (city_image.size > 0) loadRVCityImage(city_image)
                else bind.rvCityImage.visibility = View.GONE

                var _object: MutableList<Place> =
                    city_place.filter { it.idCategory == 3 }.toMutableList()
                if (_object.size > 0) {
                    bind.tvObject.visibility = View.VISIBLE
                    bind.rvObject.visibility = View.VISIBLE
                    loadRVObject(_object)
                }

                var _food: MutableList<Place> =
                    city_place.filter { it.idCategory == 6 }.toMutableList()
                if (_food.size > 0) {
                    bind.tvFood.visibility = View.VISIBLE
                    bind.rvFood.visibility = View.VISIBLE
                    loadRVFood(_food)
                }

                var _other: MutableList<Place> =
                    city_place.filter { it.idCategory != 3 && it.idCategory != 6 }.toMutableList()
                if (_other.size > 0) {
                    bind.tvOther.visibility = View.VISIBLE
                    bind.rvOther.visibility = View.VISIBLE
                    loadRVOhter(_other)
                }

            } else {
                bind.dialog2.visibility = View.VISIBLE
            }

        }.onFailure {
            Log.e("Supabase", "Ошибка получения данных : ${it}")
        }
    }

    private fun loadRVCityImage(collection: MutableList<CityImage>) {
        bind.rvCityImage.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        bind.rvCityImage.adapter = AdapterCityImage(collection)
    }

    private fun loadRVObject(collection: MutableList<Place>) {
        bind.rvObject.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        bind.rvObject.adapter = AdapterPlace(collection, place_image)
    }

    private fun loadRVFood(collection: MutableList<Place>) {
        bind.rvFood.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        bind.rvFood.adapter = AdapterPlace(collection, place_image)
    }

    private fun loadRVOhter(collection: MutableList<Place>) {
        bind.rvOther.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        bind.rvOther.adapter = AdapterPlace(collection, place_image)
    }

}