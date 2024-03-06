package com.example.myapplication.DATA_CLASS

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("tbPlace")
data class Place(
    @SerialName("id")
    var id:Int = 1,
    @SerialName("name")
    var name: String = "",
    @SerialName("description")
    var description: String? = null,
    @SerialName("coordinates")
    var coordinates: String? = null,
    @SerialName("workingHours")
    var workingHours: String = "",
    @SerialName("idCategory")
    var idCategory: Int = 0,
    @SerialName("idCity")
    var idCity: Int = 0,
    @SerialName("cost")
    var cost: Float = 0f,
    @SerialName("adress")
    var adress: String? = null
)
