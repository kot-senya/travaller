package com.example.myapplication.DATA_CLASS

import kotlinx.serialization.Serializable

@Serializable
data class Region(
    var id: Int = 1,
    var name: String = "",
    var idDistrict: String = "",
)
