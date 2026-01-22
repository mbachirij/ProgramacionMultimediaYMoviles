package com.example.pruebachuck.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pruebachuck.data.model.ChisteEntity

@Database(entities = [ChisteEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun chisteDao(): ChisteDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "chiste_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
