package com.example.myapplication.DATA_CLASS

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("tbPlaceImage")
data class PlaceImage(
    @SerialName("id")
    var id:Int = 0,
    @SerialName("idPlace")
    var idPlace: Int = 0,
    @SerialName("image")
    var image: String = ""
)
