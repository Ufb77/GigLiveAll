package com.example.pruebaimagensonido.dao

import com.example.pruebaimagensonido.model.Banda
import com.example.pruebaimagensonido.model.BandaDto
import com.example.pruebaimagensonido.model.Cartel
import com.example.pruebaimagensonido.model.CartelDto
import com.example.pruebaimagensonido.model.Evento
import com.example.pruebaimagensonido.model.EventoDto
import com.example.pruebaimagensonido.model.FragmentoCancion
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("/banda")
    suspend fun insertarBanda(@Body banda: Banda): Response<Banda>

    @POST("/cartel")
    suspend fun insertarCartel(@Body cartel: Cartel): Response<Cartel>

    @POST("/evento")
    suspend fun insertarEvento(@Body evento: Evento): Response<Evento>
    //PROBAR A SACAR SOLO UN ID Y NO UN RESPONSE<EVENTO>
    @GET("/evento/nombre/{nombre}")
    suspend fun obtenerEventoPorNombre(@Path("nombre") nombre: String): Response<Evento>

    @GET("/evento/todosDTO")
    suspend fun obtenerTodosLosEventosDTO(): Response<List<EventoDto>>

    @GET("/cartel/todosDTO")
    suspend fun obtenerTodosLosCartelesDTO(): Response<List<CartelDto>>

    @GET("/banda/todosDTO")
    suspend fun obtenerTodasLasBandasDTO(): Response<List<BandaDto>>

    @GET("/cartelbanda/bandaIdsByCartelId")
    suspend fun obtenerBandasPorCartelId(@Query("idCartel") idCartel: Int): Response<List<Int>>

    @Multipart
    @POST("/fragmentoCancion/upload")
    suspend fun subirFragmentoCancion(@Part("bandaId") bandaId: Int, @Part file: MultipartBody.Part): Response<Void>


    @GET("/evento/todos")
    suspend fun obtenerTodosLosEventos(): Response<List<Evento>>

    @Multipart
    @POST("/cartel/{id}/imagen")
    suspend fun subirImagenCartel(@Path("id") id: Int, @Part file: MultipartBody.Part): Response<Void>

    @GET("canciones/escuchar/{id}")
    suspend fun escucharCancion(@Path("id") id: Int): ResponseBody

    @GET("imagenes/ver/{id}")
    suspend fun obtenerImagen(@Path("id") id: Int): Response<ResponseBody>
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
            .baseUrl("http://10.0.2.2:8080/") // Reemplaza con tu URL base
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}