package com.example.myapplication.DATA_CLASS

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Region(
    @SerialName("id")
    var id: Int = 1,
    @SerialName("name")
    var name: String = "",
    @SerialName("idDistrict")
    var idDistrict: Int = 0,
)
