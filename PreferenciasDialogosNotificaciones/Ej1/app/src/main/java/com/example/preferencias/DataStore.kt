package com.example.preferencias

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Instancia única de DataStore
val Context.dataStore by preferencesDataStore("settings")

object SettingsPreferences {
    private val SELECTED_COLOR = stringPreferencesKey("color_mode")

    fun getColorMode(context: Context): Flow<String> =
        context.dataStore.data.map { prefs -> prefs[SELECTED_COLOR] ?: "Rojo" }

    suspend fun setColorMode(context: Context, value: String) {
        context.dataStore.edit { prefs ->
            prefs[SELECTED_COLOR] = value
        }
    }
}
