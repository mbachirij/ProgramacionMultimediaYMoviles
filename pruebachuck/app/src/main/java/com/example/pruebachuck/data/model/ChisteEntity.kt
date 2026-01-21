package com.example.pruebachuck.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tabla_chistes")
data class ChisteEntity(

    @PrimaryKey val id: String, // Clave primaria
    val texto: String,          // aquí guardamos el texto del chiste
    val iconoUrl: String        // aquí guardamos el url de la imagen
)