package com.example.pruebachuck.ui

import android.app.Application
import android.util.Log
import androidx.compose.ui.layout.LookaheadScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.pruebachuck.data.local.AppDatabase
import com.example.pruebachuck.data.model.Chiste
import com.example.pruebachuck.data.model.ChisteEntity
import com.example.pruebachuck.data.network.RetrofitClient
import com.example.pruebachuck.data.repository.ChisteRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.launch


class ChisteViewModel(application: Application) : AndroidViewModel(application) {

    private val api = RetrofitClient.api
    private val dao = AppDatabase.getDatabase(application).chisteDao()
    private val repo = ChisteRepository(dao)
    // Lista chistes para mostrar en la UI
    val listaChistes: LiveData<List<ChisteEntity>> = repo.obtenerChistesLocal().asLiveData()


    fun cargarChisteAleatorio() {
        viewModelScope.launch {

            val chiste = api.getChisteAleatorio()

            val entity = ChisteEntity(
                id = chiste.id,
                texto = chiste.texto,
                iconoUrl = chiste.iconoUrl
            )

            repo.insertarChiste(entity)
        }
    }

    fun borrarChiste(chiste: ChisteEntity) {
        viewModelScope.launch {
            repo.borrarChiste(chiste)
        }
    }

    fun sincronizarSiHayUsuario(){

        val user = FirebaseAuth.getInstance().currentUser ?: return
        viewModelScope.launch {
            try{
                repo.sincronizarChistes()
            }catch (e: Exception){
                Log.e("SYNC", "Error al sincronizar chistes", e)
            }
        }

    }

}