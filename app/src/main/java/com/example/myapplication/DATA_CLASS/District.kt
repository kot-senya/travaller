package com.example.myapplication.DATA_CLASS

import kotlinx.serialization.Serializable

@Serializable
data class District(
    var id: Int = 1,
    var name: String = ""
)
