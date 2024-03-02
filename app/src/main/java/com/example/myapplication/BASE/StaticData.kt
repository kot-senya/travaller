package com.example.myapplication.BASE

import com.example.myapplication.DATA_CLASS.City
import com.example.myapplication.DATA_CLASS.District
import com.example.myapplication.DATA_CLASS.Region

object StaticData {
    var district: MutableList<District> = mutableListOf()
    var region: MutableList<Region> = mutableListOf()
    var city: MutableList<City> = mutableListOf()
    var city_popular: MutableList<City> = mutableListOf()

    fun getRegion(idRegion: Int): String {
        return region.firstOrNull { it.id == idRegion }!!.name
    }
}