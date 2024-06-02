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
import com.example.pruebaimagensonido.model.BandaDto
import com.example.pruebaimagensonido.model.Cartel
import com.example.pruebaimagensonido.model.CartelDto
import com.example.pruebaimagensonido.model.Evento
import com.example.pruebaimagensonido.model.EventoDto
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class MyViewModel : ViewModel() {


    private val _eventos = MutableLiveData<List<EventoDto>>()
    val eventos: LiveData<List<EventoDto>> get() = _eventos

    private val _cartel = MutableLiveData<CartelDto>()
    val cartel: LiveData<CartelDto> get() = _cartel

    private val _bandas = MutableLiveData<List<BandaDto>>()
    val bandas: LiveData<List<BandaDto>> get() = _bandas

    val message = MutableLiveData<String>()
    val eventoCreado = MutableLiveData<Evento?>()

    private val _nombresBandas = MutableLiveData<List<String>>()
    val nombresBandas: LiveData<List<String>> get() = _nombresBandas

    private var mediaPlayer: MediaPlayer? = null

    fun insertarEvento(evento: Evento) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.insertarEvento(evento)
                if (response.isSuccessful) {
                    eventoCreado.value = response.body()
                    message.value = "Evento insertado con éxito, ID: ${eventoCreado.value?.id_evento}"
                } else {
                    message.value = "Error al insertar evento: ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                message.value = "Excepción al insertar evento: ${e.message}"
            }
        }
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






    fun reproducirOPararFragmento(nombreBanda: String, context: Context) {
        viewModelScope.launch {
            try {
                if (mediaPlayer?.isPlaying == true) {
                    mediaPlayer?.stop()
                    mediaPlayer?.reset()
                    mediaPlayer?.release()
                    mediaPlayer = null
                } else {
                    val response = RetrofitInstance.api.obtenerFragmentoPorNombreBanda(nombreBanda)
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            val fragmentoBytes = responseBody.bytes()
                            val tempFile = File.createTempFile("fragmento", ".mp3", context.cacheDir)
                            tempFile.writeBytes(fragmentoBytes)
                            mediaPlayer = MediaPlayer().apply {
                                setDataSource(tempFile.absolutePath)
                                prepare()
                                start()
                            }
                        }
                    } else {
                        Log.e("MyAppLog", "Error al obtener fragmento: ${response.errorBody()?.string()}")
                    }
                }
            } catch (e: Exception) {
                Log.e("MyAppLog", "Excepción al obtener fragmento: ${e.message}")
            }
        }
    }


    private suspend fun createTempFile(data: ByteArray, context: Context): File {
        return withContext(Dispatchers.IO) {
            val tempFile = File.createTempFile("fragmento", "mp3", context.cacheDir)
            FileOutputStream(tempFile).use { fos ->
                fos.write(data)
            }
            tempFile
        }
    }

    private fun playAudio(file: File) {
        try {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer()
            }
            mediaPlayer?.reset()
            mediaPlayer?.setDataSource(file.absolutePath)
            mediaPlayer?.prepare()
            mediaPlayer?.start()

            Log.d("MyAppLog", "Reproducción iniciada")

            mediaPlayer?.setOnCompletionListener {
                it.release()
                mediaPlayer = null
                Log.d("MyAppLog", "Reproducción completada")
            }

        } catch (e: Exception) {
            Log.e("MyAppLog", "Error al reproducir audio: ${e.message}")
        }
    }

    override fun onCleared() {
        mediaPlayer?.release()
        mediaPlayer = null
        super.onCleared()
    }



//    fun obtenerTodosLosEventos(): Response<List<Evento>> {
//        viewModelScope.launch {
//            try {
//                 val response = RetrofitInstance.api.obtenerTodosLosEventos()
//                if (response.isSuccessful) {
//                    _eventos.postValue(response.body() ?: emptyList())
//                    Log.d("MyViewModel", "Eventos recuperados: ${response.body()?.size}")
//                } else {
//                    message.postValue("Error al obtener eventos: ${response.errorBody()?.string()}")
//                    Log.e("MyViewModel", "Error al obtener eventos: ${response.errorBody()?.string()}")
//                }
//
//
//            } catch (e: Exception) {
//                message.postValue("Excepción al obtener eventos: ${e.message}")
//                Log.e("MyViewModel", "Excepción al obtener eventos: ${e.message}")
//            }
//        }
//    }

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
                    message.value = "Error al obtener nombres de bandas: ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                message.value = "Excepción al obtener nombres de bandas: ${e.message}"
            }
        }
    }

    fun obtenerBandasPorCartelId(cartelId: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.obtenerBandasPorCartelId(cartelId)
                if (response.isSuccessful) {
                    val bandaIds = response.body() ?: emptyList()
                    val bandasResponse = RetrofitInstance.api.obtenerTodasLasBandasDTO()
                    if (bandasResponse.isSuccessful) {
                        val bandasCompletas = bandasResponse.body() ?: emptyList()


                        _bandas.value = bandasCompletas.filter { it.id_bandaDto in bandaIds }
                    } else {
                        message.value = "Error al obtener bandas: ${bandasResponse.errorBody()?.string()}"
                    }
                } else {
                    message.value = "Error al obtener bandas por cartel: ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                message.value = "Excepción al obtener bandas por cartel: ${e.message}"
            }
        }
    }
}

//    val message = mutableStateOf("")
//    val eventoCreado = mutableStateOf<Evento?>(null)
//    val eventos = mutableStateOf<List<Evento>>(listOf())
//
//
//
//
//    fun insertarEvento(evento: Evento) {
//        viewModelScope.launch {
//            try {
//                val response = RetrofitInstance.api.insertarEvento(evento)
//                if (response.isSuccessful) {
//                    eventoCreado.value = response.body()
//                    message.value = "Evento insertado con éxito, ID: ${eventoCreado.value?.id_evento}"
//                } else {
//                    message.value = "Error al insertar evento: ${response.errorBody()?.string()}"
//                }
//            } catch (e: Exception) {
//                message.value = "Excepción al insertar evento: ${e.message}"
//            }
//        }
//    }
//
//
//
//    suspend fun insertarCartel(cartel: Cartel): Response<Cartel> {
//        return try {
//            RetrofitInstance.api.insertarCartel(cartel)
//        } catch (e: Exception) {
//            message.value = "Excepción al insertar cartel: ${e.message}"
//            throw e
//        }
//    }
//
//    suspend fun subirImagenCartel(cartelId: Int, file: MultipartBody.Part): Response<Void> {
//        return try {
//            RetrofitInstance.api.subirImagenCartel(cartelId, file)
//        } catch (e: Exception) {
//            message.value = "Excepción al subir imagen: ${e.message}"
//            throw e
//        }
//    }
//
//
//    suspend fun insertarBanda(banda: Banda): Response<Banda> {
//        return try {
//            RetrofitInstance.api.insertarBanda(banda)
//        } catch (e: Exception) {
//            message.value = "Excepción al insertar banda: ${e.message}"
//            throw e
//        }
//    }
//
//    suspend fun subirFragmentoCancion(bandaId: Int, file: MultipartBody.Part): Response<Void> {
//        return try {
//            RetrofitInstance.api.subirFragmentoCancion(bandaId, file)
//        } catch (e: Exception) {
//            message.value = "Excepción al subir fragmento: ${e.message}"
//            throw e
//        }
//    }
//
//    suspend fun obtenerTodosLosEventos() {
//        try {
//            val response = RetrofitInstance.api.obtenerTodosLosEventos()
//            if (response.isSuccessful) {
//                eventos.value = response.body() ?: listOf()
//            } else {
//                message.value = "Error al obtener eventos: ${response.errorBody()?.string()}"
//            }
//        } catch (e: Exception) {
//            message.value = "Excepción al obtener eventos: ${e.message}"
//        }
//    }

