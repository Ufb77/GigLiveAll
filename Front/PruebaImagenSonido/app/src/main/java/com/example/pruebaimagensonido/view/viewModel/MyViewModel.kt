package com.example.pruebaimagensonido.view.viewModel

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pruebaimagensonido.dao.RetrofitInstance
import com.example.pruebaimagensonido.model.Banda
import com.example.pruebaimagensonido.model.Cartel
import com.example.pruebaimagensonido.model.CartelDto
import com.example.pruebaimagensonido.model.Evento
import com.example.pruebaimagensonido.model.EventoDto
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Response
import java.io.File

class MyViewModel : ViewModel() {


    private val _eventos = MutableLiveData<List<EventoDto>>()
    val eventos: LiveData<List<EventoDto>> get() = _eventos //es similar a un getter

    private val _cartel = MutableLiveData<CartelDto>()
    val cartel: LiveData<CartelDto> get() = _cartel

    val message = MutableLiveData<String>()
    val eventoCreado = MutableLiveData<Evento?>()

    private val _nombresBandas = MutableLiveData<List<String>>()
    val nombresBandas: LiveData<List<String>> get() = _nombresBandas

    //private var mediaPlayer: MediaPlayer? = null


    fun eliminarEvento(eventoId: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.eliminarDesdeEvento(eventoId)
                if (response.isSuccessful) {
                    message.value = "Evento eliminado con éxito."
                } else {
                    message.value = "Error al eliminar evento: ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                message.value = "Excepción al eliminar evento: ${e.message}"
            }
        }
    }

    fun insertarEvento(evento: Evento) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.insertarEvento(evento)
                if (response.isSuccessful) {
                    eventoCreado.value = response.body()
                    message.value =
                        "Evento insertado con éxito, ID: ${eventoCreado.value?.id_evento}"
                } else {
                    message.value = "Error al insertar evento: ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                message.value = "Excepción al insertar evento: ${e.message}"
            }
        }
    }

    // Restablecer el estado de eventoCreado a null
    fun resetEventoCreado() {
        eventoCreado.value = null
    }

    suspend fun insertarCartel(cartel: Cartel): Response<Cartel> {
        return try {
            RetrofitInstance.api.insertarCartel(cartel)
        } catch (e: Exception) {
            message.value = "Excepción al insertar cartel: ${e.message}"
            throw e
        }
    }

    suspend fun subirImagenCartel(cartelId: Int, file: MultipartBody.Part): Response<Void> {
        return try {
            RetrofitInstance.api.subirImagenCartel(cartelId, file)
        } catch (e: Exception) {
            message.value = "Excepción al subir imagen: ${e.message}"
            throw e
        }
    }

    suspend fun insertarBanda(banda: Banda): Response<Banda> {
        return try {
            RetrofitInstance.api.insertarBanda(banda)
        } catch (e: Exception) {
            message.value = "Excepción al insertar banda: ${e.message}"
            throw e
        }
    }

    suspend fun subirFragmentoCancion(bandaId: Int, file: MultipartBody.Part): Response<Void> {
        return try {
            RetrofitInstance.api.subirFragmentoCancion(bandaId, file)
        } catch (e: Exception) {
            message.value = "Excepción al subir fragmento: ${e.message}"
            throw e
        }
    }


    private val _bandaEnReproduccion = MutableLiveData<String?>()
    val bandaEnReproduccion: LiveData<String?> get() = _bandaEnReproduccion

    private val mediaPlayer = MediaPlayer()

    fun reproducirOPararFragmento(nombreBanda: String, context: Context) {
        viewModelScope.launch {
            try {
                if (_bandaEnReproduccion.value == nombreBanda) {
                    mediaPlayer.stop()
                    mediaPlayer.reset()
                    _bandaEnReproduccion.value = null
                } else {
                    if (_bandaEnReproduccion.value != null) {
                        mediaPlayer.stop()
                        mediaPlayer.reset()
                    }

                    val response = RetrofitInstance.api.obtenerFragmentoPorNombreBanda(nombreBanda)
                    if (response.isSuccessful) {
                        val fragmentoBody = response.body()
                        val tempFile = File.createTempFile("fragmento", "mp3", context.cacheDir)
                        fragmentoBody?.byteStream()?.use { input ->
                            tempFile.outputStream().use { output ->
                                input.copyTo(output)
                            }
                        }
                        mediaPlayer.apply {
                            setDataSource(tempFile.absolutePath)
                            prepare()
                            start()
                        }
                        _bandaEnReproduccion.value = nombreBanda
                    } else {
                        Log.e(
                            "MyAppLog",
                            "Error al obtener fragmento: ${response.errorBody()?.string()}"
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("MyAppLog", "Excepción al obtener fragmento: ${e.message}")
            }
        }
    }

    fun pararReproduccion() {
        if (_bandaEnReproduccion.value != null) {
            mediaPlayer.stop()
            mediaPlayer.reset()
            _bandaEnReproduccion.value = null
        }
    }


    fun obtenerTodosLosEventosDTO() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.obtenerTodosLosEventosDTO()
                if (response.isSuccessful) {
                    _eventos.value = response.body()
                } else {
                    message.value = "Error al obtener eventos: ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                message.value = "Excepción al obtener eventos: ${e.message}"
            }
        }
    }

    fun obtenerCartelPorId(cartelId: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.obtenerTodosLosCartelesDTO()
                if (response.isSuccessful) {
                    _cartel.value = response.body()?.find { it.id_cartelDto == cartelId }
                } else {
                    message.value = "Error al obtener cartel: ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                message.value = "Excepción al obtener cartel: ${e.message}"
            }
        }
    }

    fun obtenerNombresBandasPorCartelId(cartelId: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.obtenerNombresBandasPorCartelId(cartelId)
                if (response.isSuccessful) {
                    _nombresBandas.value = response.body()
                } else {
                    message.value =
                        "Error al obtener nombres de bandas: ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                message.value = "Excepción al obtener nombres de bandas: ${e.message}"
            }
        }
    }
}

