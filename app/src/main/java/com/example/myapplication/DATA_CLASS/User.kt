package com.example.myapplication.DATA_CLASS

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
@SerialName("tbUsers")
data class User(
    @SerialName("id")
    var id: Int = 0,
    @SerialName("email")
    var email:String = ""
)
