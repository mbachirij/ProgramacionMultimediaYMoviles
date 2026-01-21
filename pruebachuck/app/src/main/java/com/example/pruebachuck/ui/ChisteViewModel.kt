package com.example.pruebachuck.ui

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.example.pruebachuck.data.local.AppDatabase
import com.example.pruebachuck.data.model.Chiste
import com.example.pruebachuck.data.network.RetrofitClient


class ChisteViewModel(application: Application) : ViewModel() {

    // La Api
    private val api = RetrofitClient.api

    // Construyo la base de datos
    private val db = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "chiste_database"
    ).build()

    private val dao = db.chisteDao()

    private val _chiste = MutableLiveData<Chiste>(null)
    val chiste: LiveData<Chiste> = _chiste



}