package com.example.pruebachuck.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pruebachuck.data.model.ChisteEntity

@Database(entities = [ChisteEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun chisteDao(): ChisteDao
}
