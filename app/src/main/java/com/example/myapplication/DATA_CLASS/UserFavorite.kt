package com.example.myapplication.DATA_CLASS

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("tbUserFavorite")
data class UserFavorite(
    @SerialName("id")
    var id: Int = 0,
    @SerialName("idUser")
    var idUser: Int = 0,
    @SerialName("idCity")
    var idCity: Int = 0
)
