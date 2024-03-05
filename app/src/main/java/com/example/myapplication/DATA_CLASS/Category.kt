package com.example.myapplication.DATA_CLASS

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("tbPlaceImage")
data class Category (
    @SerialName("id")
    var id:Int = 0,
    @SerialName("name")
    var name: String = ""
)
