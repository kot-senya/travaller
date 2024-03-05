package com.example.myapplication

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView.*
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.ADAPTER.AdapterCity
import com.example.myapplication.ADAPTER.HolderCity
import com.example.myapplication.BASE.StaticData
import com.example.myapplication.BASE.Supabase
import com.example.myapplication.DATA_CLASS.Category
import com.example.myapplication.DATA_CLASS.City
import com.example.myapplication.DATA_CLASS.District
import com.example.myapplication.DATA_CLASS.Region
import com.example.myapplication.DATA_CLASS.User
import com.example.myapplication.DATA_CLASS.UserFavorite
import com.example.myapplication.OTHER.Other
import com.example.myapplication.databinding.ActivityMainBinding
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {
    private var _bind: ActivityMainBinding? = null
    private val bind: ActivityMainBinding get() = _bind!!
    private var _pref: SharedPreferences? = null
    private val pref: SharedPreferences get() = _pref!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _bind = ActivityMainBinding.inflate(layoutInflater)
        _pref = getSharedPreferences("Travel", MODE_PRIVATE)
        setContentView(bind.root)

        enabledView(false)
        visibilityLL(View.GONE)
        bind.imageDialog1.startAnimation(Other.rotateAnimation())
        Other.countDownTimer_load(bind.textviewDialog1).start()

        StaticData.auth = pref.getBoolean("Auth", false)

        lifecycleScope.launch {
            getData()
        }

        setListener()
    }

    private fun visibilityLL(value: Int) {
        bind.ll2.visibility = value
        bind.ll2.visibility = value
        bind.rvCity.visibility = value
        bind.tv.visibility = value
    }

    private fun enabledView(value: Boolean) {
        bind.btnFavPermission.isEnabled = value
        bind.sv.isEnabled = value
        bind.btnFilter.isEnabled = value
        bind.rvCity.isEnabled = value
        bind.rvPopular.isEnabled = value
    }

    private fun setListener() {

        bind.btnFavPermission.setOnClickListener {
            if (!pref.getBoolean("Auth", false)) {
                enabledView(false)
                bind.dialog2.visibility = View.VISIBLE
                dialog2()
            } else Toast.makeText(this, "Вы уже авторизировались", Toast.LENGTH_SHORT).show()
        }

        bind.btnFilter.setOnClickListener {
            enabledView(false)
            bind.dialog3.visibility = View.VISIBLE
            dialog3()
        }

        bind.sv.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.toString().length > 0) bind.ll2.visibility = View.GONE
                else bind.ll2.visibility = View.VISIBLE

                var city: MutableList<City> =
                    StaticData.city.filter { it.name.contains(newText.toString(), true) }
                        .toMutableList()
                loadRVCity(city)
                return true
            }
        })
    }

    private suspend fun getData() = withContext(Dispatchers.Main) {
        runCatching {
            StaticData.category = Supabase.client().postgrest.from("tbCategory").select().decodeList<Category>()
                .toMutableList()
            StaticData.region =
                Supabase.client().postgrest.from("tbRegion").select().decodeList<Region>()
                    .toMutableList()
            StaticData.district =
                Supabase.client().postgrest.from("tbDistrict").select().decodeList<District>()
                    .toMutableList()
            StaticData.city = Supabase.client().postgrest.from("tbCity").select().decodeList<City>()
                .toMutableList()
            StaticData.city.sortBy { it.name }
            if (pref.getInt("UserId", 0) == 0) {
                var u: User =
                    Supabase.client().postgrest.from("tbUsers").select().decodeList<User>()
                        .filter { it.email == pref.getString("Email", null) }.first()
                val pref_editor: SharedPreferences.Editor = pref.edit()
                pref_editor.putInt("UserId", u.id)
                pref_editor.commit()
            }
            StaticData.city_favorite = Supabase.client().postgrest.from("tbUserFavorite").select()
                .decodeList<UserFavorite>().filter { it.idUser == pref.getInt("UserId", 0) }
                .toMutableList()
        }.onSuccess {
            StaticData.city_popular = StaticData.city.filter { it.popular }.toMutableList()
            loadRVCity(StaticData.city)
            loadRVPopularCity(StaticData.city_popular)
            bind.dialog1.visibility = View.GONE
            enabledView(true)
            visibilityLL(View.VISIBLE)
        }.onFailure {
            Log.e("Supabse", "Ошибка получения данных :${it}")
        }
    }

    private fun loadRVPopularCity(collection: MutableList<City>) {
        var adapterCity: AdapterCity = AdapterCity(collection, false)

        bind.rvPopular.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        bind.rvPopular.adapter = adapterCity

        adapterCity.setLookListener(object : AdapterCity.LookGuide {
            override fun onClick(holder: HolderCity, position: Int) {
                lifecycleScope.launch {
                    var c = StaticData.city.filter { it.id ==  StaticData.city_popular[position].id}.first()
                    StaticData.city_guide_id = c.id
                    startActivity(Intent(this@MainActivity, CityGuideActivity::class.java))
                }
            }
        })
    }

    private fun loadRVCity(collection: MutableList<City>) {
        var adapterCity: AdapterCity = AdapterCity(collection, true)
        bind.rvCity.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        bind.rvCity.adapter = adapterCity

        adapterCity.setFavoriteListener(object : AdapterCity.FavoriteListener {
            override fun onClick(holder: HolderCity, position: Int) {
                lifecycleScope.launch {
                    runCatching {
                        val newRecord = UserFavorite(
                            id = 0,
                            idUser = pref.getInt("UserId", 0),
                            idCity = StaticData.city[position].id
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
                        if (!StaticData.city_favorite.filter { it.idCity == collection[position].id }
                                .toMutableList().isEmpty()) {
                            holder.binding.btnFavorite.setBackgroundResource(R.drawable.ic_favorite_city1)
                        } else holder.binding.btnFavorite.setBackgroundResource(R.drawable.ic_favorite_city)
                    }.onFailure {
                        Log.e("Supabse", "Ошибка отправки/удаления выбранного города :${it}")
                    }
                }
            }
        })

        adapterCity.setLookListener(object : AdapterCity.LookGuide {
            override fun onClick(holder: HolderCity, position: Int) {
                lifecycleScope.launch {
                    StaticData.city_guide_id = StaticData.city[position].id
                    startActivity(Intent(this@MainActivity, CityGuideActivity::class.java))
                }
            }
        })
    }

    private fun dialog2() {
        bind.dialog2BtnClose.setOnClickListener {
            bind.dialog2.visibility = View.GONE
            enabledView(true)
        }

        bind.dialog2BtnAuth.setOnClickListener {
            //код добавления почты
            lifecycleScope.launch {
                addUser()
            }
        }

        bind.email.addTextChangedListener(textWatcher_email)
    }

    private fun dialog3() {
        bind.dialog3BtnClose.setOnClickListener {
            bind.dialog3.visibility = View.GONE
            enabledView(true)
        }
    }

    private val textWatcher_email: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            checkEmail()
        }

        override fun afterTextChanged(p0: Editable?) {
        }

    }

    private fun checkEmail() {
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(bind.email.text.toString())
                .matches()
        ) bind.dialog2BtnAuth.isEnabled = true
        else bind.dialog2BtnAuth.isEnabled = false
    }

    private suspend fun addUser() = withContext(Dispatchers.IO) {
        runCatching {
            var new_user = User(id = 0, email = bind.email.text.toString())
            Supabase.client().postgrest.from("tbUsers").insert(new_user)
        }.onSuccess {
            val pref_editor: SharedPreferences.Editor = pref.edit()
            pref_editor.putString("Email", bind.email.text.toString())
            pref_editor.putBoolean("Auth", true)
            pref_editor.commit()
            bind.dialog2.visibility = View.GONE
            loadRVCity(StaticData.city)
            enabledView(true)
            true
        }.onFailure {
            Log.e("Supabse", "Ошибка отправки почты :${it}")
            false
        }
    }
}