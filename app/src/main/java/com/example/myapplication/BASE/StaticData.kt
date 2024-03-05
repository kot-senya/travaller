package com.example.myapplication.BASE

import com.example.myapplication.DATA_CLASS.Category
import com.example.myapplication.DATA_CLASS.City
import com.example.myapplication.DATA_CLASS.District
import com.example.myapplication.DATA_CLASS.Region
import com.example.myapplication.DATA_CLASS.UserFavorite

object StaticData {
    var category: MutableList<Category> = mutableListOf()
    var district: MutableList<District> = mutableListOf()
    var region: MutableList<Region> = mutableListOf()
    var city: MutableList<City> = mutableListOf()
    var city_popular: MutableList<City> = mutableListOf()
    var city_favorite: MutableList<UserFavorite> = mutableListOf()
    var auth:Boolean = false

    var city_guide_id: Int = -1
    fun getRegion(idRegion: Int): String {
        return region.firstOrNull { it.id == idRegion }!!.name
    }

    fun getFavorite(fav: UserFavorite):Boolean{
        var count:Int = city_favorite.filter { it.idCity == fav.idCity && it.idUser == fav.idUser }
            .toMutableList().size

        if(count > 0) return false
        else return true
    }
}