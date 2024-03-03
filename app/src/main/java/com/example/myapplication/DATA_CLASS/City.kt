package com.example.myapplication.DATA_CLASS

import androidx.annotation.Keep
import kotlinx.serialization.ContextualSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.serializer

//@Keep
@Serializable
data class City(
    @SerialName("id")
    var id: Int = 0 ,
    @SerialName("name")
    var name: String = "",
    @SerialName("image")
    var image: String? = null,
    @SerialName("popular")
    var popular: Boolean = false,
    @SerialName("idRegion")
    var idRegion: Int = 0
)
