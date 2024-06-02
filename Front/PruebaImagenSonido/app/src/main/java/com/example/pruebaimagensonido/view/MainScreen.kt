package com.example.pruebaimagensonido.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.pruebaimagensonido.dao.RetrofitInstance
import com.example.pruebaimagensonido.model.Banda
import com.example.pruebaimagensonido.model.Cartel
import com.example.pruebaimagensonido.model.Evento
import com.example.pruebaimagensonido.model.FragmentoCancion
import com.example.pruebaimagensonido.view.viewModel.MyViewModel
import kotlinx.coroutines.Dispatchers
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import com.example.pruebaimagensonido.model.BandaDto
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File

@Composable
fun ImagePickerScreen() {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { imagePickerLauncher.launch("image/jpeg") }) {
            Text(text = "Seleccionar Imagen")
        }

        Spacer(modifier = Modifier.height(16.dp))

        imageUri?.let { uri ->
            Image(
                painter = rememberImagePainter(uri),
                contentDescription = null,
                modifier = Modifier.size(200.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                coroutineScope.launch {
                    //uploadImage(context, uri)
                }
            }) {
                Text(text = "Subir Imagen")
            }
        }

        //MusicPlayer()

        //ImageLoader()
        //AudioPickerScreen()


    }
}

@Composable
fun AudioPickerScreen() {
    var audioUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val audioPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        audioUri = uri
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { audioPickerLauncher.launch("audio/*") }) {
            Text(text = "Seleccionar Audio")
        }

        Spacer(modifier = Modifier.height(16.dp))

        audioUri?.let { uri ->
            Text(text = "Audio seleccionado: ${uri.path}", modifier = Modifier.padding(16.dp))

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                coroutineScope.launch {
                    //uploadAudio(context, uri)
                }
            }) {
                Text(text = "Subir Audio")
            }
        }
    }
}

/*suspend fun uploadAudio(context: Context, audioUri: Uri) {
    try {
        val contentResolver = context.contentResolver
        val file = File(context.cacheDir, "temp_audio.mp3")
        val inputStream = contentResolver.openInputStream(audioUri)
        file.outputStream().use { outputStream ->
            inputStream?.copyTo(outputStream)
        }

        val requestFile = file.asRequestBody("audio/mpeg".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        val response = RetrofitInstance.api.uploadAudio(body)

        withContext(Dispatchers.Main) {
            if (response.isSuccessful) {
                Toast.makeText(context, "Audio subido exitosamente", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Error al subir audio", Toast.LENGTH_SHORT).show()
            }
        }
    } catch (e: Exception) {
        withContext(Dispatchers.Main) {
            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}*/

//suspend fun uploadImage(context: Context, imageUri: Uri) {
//    try {
//        val contentResolver = context.contentResolver
//        val file = File(context.cacheDir, "temp_image.jpg")
//        val inputStream = contentResolver.openInputStream(imageUri)
//        file.outputStream().use { outputStream ->
//            inputStream?.copyTo(outputStream)
//        }
//
//        val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
//        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
//
//        val response = RetrofitInstance.api.subirImagenCartel(body)
//
//        withContext(Dispatchers.Main) {
//            if (response.isSuccessful) {
//                Toast.makeText(context, "Imagen subida exitosamente", Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(context, "Error al subir imagen", Toast.LENGTH_SHORT).show()
//            }
//        }
//    } catch (e: Exception) {
//        withContext(Dispatchers.Main) {
//            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
//        }
//    }
//}


@Composable
fun MusicPlayer() {
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    val coroutineScope = rememberCoroutineScope()
    var songByteArray by remember { mutableStateOf<ByteArray?>(null) }

    Column {
        Button(
            onClick = {
                coroutineScope.launch(Dispatchers.IO) {
                    try {
                        // Llama a la API para obtener la canción
                        songByteArray = RetrofitInstance.api.escucharCancion(1).bytes() // Cambia el ID según sea necesario
                       // if (response.isSuccessful()) {
                       //     songByteArray = response.body()?.bytes()
                     //   }
                    } catch (e: Exception) {
                        // Manejar errores de red o de la API
                    }
                }
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Obtener y Reproducir Canción")
        }

        if (songByteArray != null) {
            val songString = songByteArray!!.let { Base64.encodeToString(it, Base64.DEFAULT) }
            Text(
                text = "Canción recuperada:\n$songString",
                modifier = Modifier.padding(16.dp)
            )
        }
        // Botón para reproducir la canción
        /*if (songByteArray != null) {
            Button(
                onClick = {
                    mediaPlayer?.release() // Libera el reproductor anterior si existe
                    mediaPlayer = MediaPlayer().apply {
                        val inputStream = ByteArrayInputStream(songByteArray)
                        setDataSource(inputStream)
                        prepare()
                        start()
                    }
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Reproducir Canción")
            }
        }*/
    }
}

@Composable
fun ImageLoader() {
    val coroutineScope = rememberCoroutineScope()
    var imageByteArray by remember { mutableStateOf<ByteArray?>(null) }
    var imageBitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }

    Column {
        Button(
            onClick = {
                coroutineScope.launch(Dispatchers.IO) {
                    try {
                        // Llama a la API para obtener la imagen
                        val response = RetrofitInstance.api.obtenerImagen(1) // Cambia el ID según sea necesario
                        if (response.isSuccessful) {
                            imageByteArray = response.body()?.bytes()
                            imageByteArray?.let {
                                imageBitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                            }
                        }
                    } catch (e: Exception) {
                        // Manejar errores de red o de la API
                    }
                }
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Obtener y Mostrar Imagen")
        }

        // Mostrar la imagen si se ha recuperado y decodificado
        imageBitmap?.let { bitmap ->
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun CreateEventScreen(navController: NavController) {
    val viewModel: MyViewModel = viewModel()
    val nombre = remember { mutableStateOf("") }
    val fecha = remember { mutableStateOf("") }
    val precio = remember { mutableStateOf("") }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        TextField(
            value = nombre.value,
            onValueChange = { nombre.value = it },
            label = { Text(text = "Nombre del Evento") },
            modifier = Modifier.padding(bottom = 8.dp)
        )
        TextField(
            value = fecha.value,
            onValueChange = { fecha.value = it },
            label = { Text(text = "Fecha del Evento") },
            modifier = Modifier.padding(bottom = 8.dp)
        )
        TextField(
            value = precio.value,
            onValueChange = { precio.value = it },
            label = { Text(text = "Precio del Evento") },
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Button(onClick = {
            val evento = Evento(
                id_evento = 0, // ID autogenerado
                nombre = nombre.value,
                fecha = fecha.value,
                precio = precio.value.toDoubleOrNull() ?: 0.0
            )
            viewModel.insertarEvento(evento)
        }) {
            Text(text = "Subir Evento")
        }

        viewModel.eventoCreado.value?.let {
            LaunchedEffect(it) {
                navController.navigate("createCartel/${it.id_evento}")
            }
        }

        viewModel.message.value?.let { Text(text = it) }
    }
}


@Composable
fun CreateCartelScreen(navController: NavController, eventoId: Int) {
    val viewModel: MyViewModel = viewModel()
    val coroutineScope = rememberCoroutineScope()
    val cartelId = remember { mutableStateOf<Int?>(null) }
    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    // Paso 1: Confirmar creación del cartel sin imagen
    if (cartelId.value == null) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "¿Quieres crear un cartel para el evento?")

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                coroutineScope.launch(Dispatchers.IO) {
                    try {
                        val cartel = Cartel(
                            id_cartel = 0, // ID autogenerado
                            imagen = null, // No imagen aún
                            evento = Evento(id_evento = eventoId, nombre = "", fecha = "", precio = 0.0),
                            bandas = mutableListOf()
                        )

                        val response = viewModel.insertarCartel(cartel)
                        if (response.isSuccessful) {
                            cartelId.value = response.body()?.id_cartel
                        } else {
                            withContext(Dispatchers.Main) {
                                viewModel.message.value = "Error al insertar cartel: ${response.errorBody()?.string()}"
                            }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            viewModel.message.value = "Excepción al insertar cartel: ${e.message}"
                        }
                        e.printStackTrace()
                    }
                }
            }) {
                Text(text = "Sí")
            }

            Button(onClick = {
                navController.navigate("principal")
            }) {
                Text(text = "No")
            }
        }
    } else {
        // Paso 2: Seleccionar y subir la imagen
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val imagePickerLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent()
            ) { uri: Uri? ->
                imageUri.value = uri
            }

            Button(onClick = { imagePickerLauncher.launch("image/*") }) {
                Text(text = "Seleccionar Imagen")
            }

            Spacer(modifier = Modifier.height(16.dp))

            imageUri.value?.let { uri ->
                Image(
                    painter = rememberImagePainter(uri),
                    contentDescription = null,
                    modifier = Modifier.size(200.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    coroutineScope.launch(Dispatchers.IO) {
                        try {
                            val contentResolver = context.contentResolver
                            val file = File(context.cacheDir, "temp_image.jpg")
                            val inputStream = contentResolver.openInputStream(uri)
                            file.outputStream().use { outputStream ->
                                inputStream?.copyTo(outputStream)
                            }

                            val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

                            val response = viewModel.subirImagenCartel(cartelId.value!!, body)
                            if (response.isSuccessful) {
                                withContext(Dispatchers.Main) {
                                    navController.navigate("createBandaFragmento/${cartelId.value}")
                                }
                            } else {
                                withContext(Dispatchers.Main) {
                                    viewModel.message.value = "Error al subir imagen: ${response.errorBody()?.string()}"
                                }
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                viewModel.message.value = "Excepción al subir imagen: ${e.message}"
                            }
                            e.printStackTrace()
                        }
                    }
                }) {
                    Text(text = "Subir Imagen")
                }
            }

            viewModel.message.value?.let { Text(text = it) }
        }
    }
}


@Composable
fun CreateBandaFragmentoScreen(navController: NavController, cartelId: Int) {
    val viewModel: MyViewModel = viewModel()
    val coroutineScope = rememberCoroutineScope()
    val nombreBanda = remember { mutableStateOf("") }
    val audioUri = remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    val audioPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        audioUri.value = uri
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TextField(
            value = nombreBanda.value,
            onValueChange = { nombreBanda.value = it },
            label = { Text(text = "Nombre de la Banda") },
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Button(onClick = { audioPickerLauncher.launch("audio/*") }) {
            Text(text = "Seleccionar Audio")
        }

        Spacer(modifier = Modifier.height(16.dp))

        audioUri.value?.let { uri ->
            Text(text = "Audio seleccionado: ${uri.path}", modifier = Modifier.padding(16.dp))

            Button(onClick = {
                coroutineScope.launch(Dispatchers.IO) {
                    try {
                        val cartel = Cartel(id_cartel = cartelId, imagen = null, evento = Evento(id_evento = 0, nombre = "", fecha = "", precio = 0.0), bandas = mutableListOf())
                        val banda = Banda(
                            id_banda = 0, // ID autogenerado
                            nombreBanda = nombreBanda.value,
                            carteles = mutableListOf(cartel)
                        )
                        val bandaResponse = viewModel.insertarBanda(banda)
                        if (bandaResponse.isSuccessful) {
                            val bandaId = bandaResponse.body()?.id_banda ?: 0
                            val contentResolver = context.contentResolver
                            val file = File(context.cacheDir, "temp_audio.mp3")
                            val inputStream = contentResolver.openInputStream(uri)
                            file.outputStream().use { outputStream ->
                                inputStream?.copyTo(outputStream)
                            }

                            val requestFile = file.asRequestBody("audio/mpeg".toMediaTypeOrNull())
                            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

                            val fragmentoResponse = viewModel.subirFragmentoCancion(bandaId, body)
                            if (fragmentoResponse.isSuccessful) {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "Banda y Fragmento de Canción insertados con éxito", Toast.LENGTH_SHORT).show()
                                    // Limpiar campos después de la inserción
                                    nombreBanda.value = ""
                                    audioUri.value = null
                                }
                            } else {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "Error al insertar fragmento: ${fragmentoResponse.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "Error al insertar banda: ${bandaResponse.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Excepción al insertar banda y fragmento: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }) {
                Text(text = "Guardar Banda y Fragmento")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            navController.navigate("principal")
        }) {
            Text(text = "Salir")
        }
    }
}

//ESTA FUNCIONABA
//@Composable
//fun PantallaMostrarEvento(navController: NavController) {
//    val viewModel: MyViewModel = viewModel()
//    val eventos by viewModel.eventos.observeAsState(emptyList())
//
//    LaunchedEffect(Unit) {
//        viewModel.obtenerTodosLosEventosDTO()
//    }
//
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        if (eventos.isEmpty()) {
//            Text(text = "No hay eventos disponibles.")
//        } else {
//            LazyColumn(
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                items(eventos) { evento ->
//                    EventCard(
//                        name = evento.eventName,
//                        date = evento.eventDate,
//                        price = "${evento.eventPrice}€",
//                        imageBase64 = evento.image,
//                        onClick = { navController.navigate("evento/${evento.id_eventoDto}") }
//                    )
//                }
//            }
//        }
//    }
//}

@Composable
fun PantallaMostrarEvento(navController: NavController) {
    val viewModel: MyViewModel = viewModel()
    val eventos by viewModel.eventos.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        viewModel.obtenerTodosLosEventosDTO()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (eventos.isEmpty()) {
            Text(text = "No hay eventos disponibles.")
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(eventos) { evento ->
                    EventCard(
                        name = evento.eventName,
                        date = evento.eventDate,
                        price = "${evento.eventPrice}€",
                        imageBase64 = evento.image,
                        onClick = { navController.navigate("evento/${evento.id_eventoDto}/${evento.id_cartel}") }
                    )
                }
            }
        }
    }
}

@Composable
fun EventCard(name: String, date: String, price: String, imageBase64: String?, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            imageBase64?.let {
                val imageBytes = Base64.decode(it, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.size(64.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = name, style = MaterialTheme.typography.titleLarge)
                Text(text = date, style = MaterialTheme.typography.bodySmall)
                Text(text = price, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}


@Composable
fun EventoScreen(navController: NavController, eventoId: Int, cartelId: Int) {
    val viewModel: MyViewModel = viewModel()
    val cartel by viewModel.cartel.observeAsState()
    val nombresBandas by viewModel.nombresBandas.observeAsState(emptyList())
    val context = LocalContext.current

    LaunchedEffect(cartelId) {
        viewModel.obtenerCartelPorId(cartelId)
        viewModel.obtenerNombresBandasPorCartelId(cartelId)
    }

    if (cartel != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            cartel?.imageCartelDto?.let { base64Image ->
                val imageBytes = Base64.decode(base64Image, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(nombresBandas) { nombreBanda ->
                    Text(
                        text = nombreBanda,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.reproducirOPararFragmento(nombreBanda, context)
                            }
                            .padding(8.dp)
                    )
                }
            }
        }
    } else {
        Text(text = "Cartel no encontrado")
    }
}

@Composable
fun BandCard(banda: BandaDto, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Column {
                Text(text = banda.nombreBandaDto, style = MaterialTheme.typography.titleLarge)
            }
        }
    }
}

@Composable
fun Principal(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize()) {
        Button(onClick = { navController.navigate("createEvent") }) {
            Text(text = "Crear Evento")
        }
        Button(onClick = { navController.navigate("mostrarEventos") }) {
            Text(text = "Mostrar eventos")
        }
    }
}


