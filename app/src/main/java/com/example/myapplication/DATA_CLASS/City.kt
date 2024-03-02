package com.example.myapplication.DATA_CLASS

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("tbCity")
data class City(
    var id: Int =0 ,
    var name: String = "",
    var image: String? = "",
    var popular: Boolean = false,
    var idRegion: Int = 0
)
