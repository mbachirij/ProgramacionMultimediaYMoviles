package com.example.pruebachuck.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pruebachuck.data.model.ChisteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface  ChisteDao {

    // onConflict es para que no falle si guardo el mismo chiste dos veces
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarChiste(chiste: ChisteEntity)

    @Delete
    suspend fun borrarChiste(chiste: ChisteEntity)

    // Flow es para que se actualice automáticamente la lista cuando borras o insertas algo
    @Query("SELECT * FROM tabla_chistes")
    fun obtenerChistes(): Flow<List<ChisteEntity>>

    // Consulta para buscar dinámicamente
    @Query("SELECT * FROM tabla_chistes WHERE texto LIKE '%' || :texto || '%' ")
    suspend fun buscarChiste(texto: String): List<ChisteEntity>

}