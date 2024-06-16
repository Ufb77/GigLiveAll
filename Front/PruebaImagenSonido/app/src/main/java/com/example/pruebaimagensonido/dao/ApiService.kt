package com.example.pruebaimagensonido.dao

import com.example.pruebaimagensonido.model.Banda
import com.example.pruebaimagensonido.model.ByteArrayConverterFactory
import com.example.pruebaimagensonido.model.Cartel
import com.example.pruebaimagensonido.model.CartelDto
import com.example.pruebaimagensonido.model.Evento
import com.example.pruebaimagensonido.model.EventoDto
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    @POST("/banda")
    suspend fun insertarBanda(@Body banda: Banda): Response<Banda>

    @POST("/cartel")
    suspend fun insertarCartel(@Body cartel: Cartel): Response<Cartel>

    @POST("/evento")
    suspend fun insertarEvento(@Body evento: Evento): Response<Evento>
    //PROBAR A SACAR SOLO UN ID Y NO UN RESPONSE<EVENTO>

    @GET("/evento/todosDTO")
    suspend fun obtenerTodosLosEventosDTO(): Response<List<EventoDto>>

    @GET("/cartel/todosDTO")
    suspend fun obtenerTodosLosCartelesDTO(): Response<List<CartelDto>>


    @GET("/cartelbanda/nombresPorCartelId/{id_cartel}")
    suspend fun obtenerNombresBandasPorCartelId(@Path("id_cartel") idCartel: Int): Response<List<String>>


    @GET("/fragmentoCancion/obtenerFragmento/{nombreBanda}")
    suspend fun obtenerFragmentoPorNombreBanda(@Path("nombreBanda") nombreBanda: String): Response<ResponseBody>


    @Multipart
    @POST("/fragmentoCancion/upload")
    suspend fun subirFragmentoCancion(@Part("bandaId") bandaId: Int, @Part file: MultipartBody.Part): Response<Void>


    @Multipart
    @POST("/cartel/{id}/imagen")
    suspend fun subirImagenCartel(@Path("id") id: Int, @Part file: MultipartBody.Part): Response<Void>


    @DELETE("/evento/{id_evento}")
    suspend fun eliminarDesdeEvento(@Path("id_evento") idEvento: Int): Response<Void>
}

object RetrofitInstance {
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .client(client)
            .addConverterFactory(ByteArrayConverterFactory()) //Para recuperar el sonido
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}