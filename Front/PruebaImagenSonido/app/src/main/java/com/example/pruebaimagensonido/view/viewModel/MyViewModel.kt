package com.example.pruebaimagensonido.view.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pruebaimagensonido.dao.RetrofitInstance
import com.example.pruebaimagensonido.model.Banda
import com.example.pruebaimagensonido.model.Cartel
import com.example.pruebaimagensonido.model.Evento
import com.example.pruebaimagensonido.model.FragmentoCancion
import kotlinx.coroutines.launch
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pruebaimagensonido.model.BandaDto
import com.example.pruebaimagensonido.model.CartelDto
import com.example.pruebaimagensonido.model.EventoDto
import okhttp3.MultipartBody
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException

class MyViewModel : ViewModel() {


    private val _eventos = MutableLiveData<List<EventoDto>>()
    val eventos: LiveData<List<EventoDto>> get() = _eventos

    private val _cartel = MutableLiveData<CartelDto>()
    val cartel: LiveData<CartelDto> get() = _cartel

    private val _bandas = MutableLiveData<List<BandaDto>>()
    val bandas: LiveData<List<BandaDto>> get() = _bandas

    val message = MutableLiveData<String>()
    val eventoCreado = MutableLiveData<Evento?>()

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

