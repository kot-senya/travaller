package com.example.myapplication.DATA_CLASS

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class District(
    @SerialName("id")
    var id: Int = 1,
    @SerialName("name")
    var name: String = ""
)
