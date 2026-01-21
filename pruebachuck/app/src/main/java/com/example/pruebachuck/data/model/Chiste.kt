package com.example.pruebachuck.data.model

import com.google.gson.annotations.SerializedName

// Uso SerializedName para leer el JSON
data class Chiste (

    @SerializedName("id") val id: String,
    @SerializedName("value") val texto: String,
    @SerializedName("icon_url") val iconoUrl: String

)