package com.example.todoapp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TareaDao {

    @Insert
    suspend fun insertarTarea(tarea: Tarea)

    @Delete

    suspend fun borrarTarea(tarea: Tarea)

    @Query("SELECT * FROM tareas")
    suspend fun obtenerTareas(): List<Tarea>

    // Consulta para buscar dinámicamente
    @Query("SELECT * FROM tareas WHERE tarea LIKE '%' || :busqueda || '%'")
    suspend fun buscarTareas(busqueda: String): List<Tarea>
}