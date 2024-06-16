package com.example.pruebaimagensonido.view

import android.app.DatePickerDialog
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.pruebaimagensonido.model.Banda
import com.example.pruebaimagensonido.model.Cartel
import com.example.pruebaimagensonido.model.Evento
import com.example.pruebaimagensonido.view.viewModel.MyViewModel
import kotlinx.coroutines.Dispatchers
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.Calendar



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







@Composable
fun CreateEventScreen(navController: NavController) {

    BackHandler {
        // No hace nada
    }
    val viewModel: MyViewModel = viewModel()
    val nombre = remember { mutableStateOf("") }
    val fecha = remember { mutableStateOf("") }
    val precio = remember { mutableStateOf("") }
    val context = LocalContext.current

    val selectedDate = remember { mutableStateOf("") }
    val errorMessage = remember { mutableStateOf("") }
    val precioErrorMessage = remember { mutableStateOf("") }

    Box(Modifier.background(Color(245, 109, 5))) {
        val isFormValid by derivedStateOf { //El bloque se ejecuta cada vez que el valor cambia
            nombre.value.isNotBlank() && fecha.value.isNotBlank() && precio.value.isValidPrice()
        }

        val eventoCreado by viewModel.eventoCreado.observeAsState() //lo transforma a State y esto permite observar los cambios

        LaunchedEffect(eventoCreado) {//vuelve a lanzar el codigo cada vez que cambie evento
            eventoCreado?.let { //si no es null
                navController.navigate("createCartel/${it.id_evento}")
                viewModel.resetEventoCreado()
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = nombre.value,
                onValueChange = { nombre.value = it },
                label = { Text(text = "Nombre del Evento") },
                modifier = Modifier.padding(bottom = 8.dp)
            )

            TextField(
                value = precio.value,
                onValueChange = { newValue ->
                    if (newValue.isValidPrice()) {
                        precio.value = newValue
                        precioErrorMessage.value = ""
                    } else {
                        precioErrorMessage.value = "El precio debe ser un número con máximo dos decimales."
                    }
                },
                label = { Text(text = "Precio del Evento") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if (precioErrorMessage.value.isNotEmpty()) {
                Text(
                    text = precioErrorMessage.value,
                    color = Color.Red,  // Cambiar el color del texto a negro
                    modifier = Modifier
                        .padding(8.dp)
                        .background(Color.LightGray)  // Agregar fondo rojo
                        .padding(8.dp)  // Añadir un padding interno para que el texto no toque los bordes
                )
            }

            Button(onClick = {
                //Se abre el calendario por la fecha del día actual
                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)

                DatePickerDialog(context, { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                    val selectedCalendar = Calendar.getInstance().apply {
                        //Se cambia la fecha a la elegida
                        set(selectedYear, selectedMonth, selectedDay)
                    }

                    //Se compara la fecha
                    if (selectedCalendar.after(calendar)) {
                        //+1 porque calendar indexa los meses desde 0
                        val formattedDate = "${String.format("%02d", selectedDay)}-${String.format("%02d", selectedMonth + 1)}-$selectedYear"
                        selectedDate.value = formattedDate
                        fecha.value = formattedDate
                        errorMessage.value = ""
                    } else {
                        errorMessage.value = "La fecha debe ser mayor que la fecha actual."
                    }
                }, year, month, day).show() //aquí es donde se pasa esa fecha actual recogida
            }) {
                Text(text = "Seleccionar Fecha")
            }

            if (selectedDate.value.isNotEmpty()) {
                Text(
                    text = "Fecha seleccionada: ${selectedDate.value}",
                    modifier = Modifier.padding(8.dp)
                )
            }

            if (errorMessage.value.isNotEmpty()) {
                Text(
                    text = errorMessage.value,
                    color = Color.Red,  // Cambiar el color del texto a negro
                    modifier = Modifier
                        .padding(8.dp)
                        .background(Color.LightGray)  // Agrega un fondo gris
                        .padding(8.dp)
                )
            }

            viewModel.message.value?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    modifier = Modifier
                        .background(Color.LightGray)
                        .padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(260.dp))

            Button(
                onClick = {
                    if (precio.value.isValidPrice()) {
                        val evento = Evento(
                            id_evento = 0,
                            nombre = nombre.value,
                            fecha = fecha.value,
                            precio = precio.value.toDoubleOrNull() ?: 0.0
                        )
                        viewModel.insertarEvento(evento)
                    } else {
                        precioErrorMessage.value = "El precio debe ser un número con máximo dos decimales."
                    }
                },
                enabled = isFormValid
            ) {
                Text(text = "Subir Evento")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.eliminarEvento(eventoCreado?.id_evento ?: 0)
                    navController.navigate("principal")
                }
            ) {
                Text(text = "Salir sin guardar")
            }
        }
    }
}


fun String.isValidPrice(): Boolean {
    return this.matches(Regex("^\\d*(\\.\\d{0,2})?\$"))
}


//Creación/inserción de cartel
@Composable
fun CreateCartelScreen(navController: NavController, eventoId: Int) {

    BackHandler {
        //No hace nada
    }
    val viewModel: MyViewModel = viewModel()
    val coroutineScope = rememberCoroutineScope()
    val cartelId = remember { mutableStateOf<Int?>(null) }
    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    Box(Modifier.background(Color(245, 109, 5))) {
        // Paso 1: Confirmar creación del cartel sin imagen
        if (cartelId.value == null) {
            coroutineScope.launch(Dispatchers.IO) { //lanza un hilo para evitar bloqueos al insertar
                try {
                    val cartel = Cartel(
                        id_cartel = 0, // ID autogenerado
                        imagen = null, // No imagen aún
                        evento = Evento(
                            id_evento = eventoId,
                            nombre = "",
                            fecha = "",
                            precio = 0.0
                        ),
                        bandas = mutableListOf()
                    )

                    val response = viewModel.insertarCartel(cartel)
                    if (response.isSuccessful) {
                        cartelId.value = response.body()?.id_cartel
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        viewModel.message.value = "Excepción al insertar cartel: ${e.message}"
                    }
                    e.printStackTrace()
                }
            }
        }
        // Paso 2: Seleccionar y subir la imagen
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(text = "Selecciona una imagen como cartel para el evento a crear")
            Spacer(modifier = Modifier.height(35.dp))
            //Lanza un launcher
            val imagePickerLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent() //Obtiene un contrato que se usa para obtener contenido
            ) { uri: Uri? ->
                imageUri.value = uri
            }

            Button(onClick = { imagePickerLauncher.launch("image/jpeg") }) {
                Text(text = "Seleccionar Imagen")
            }

            Spacer(modifier = Modifier.height(16.dp))

            imageUri.value?.let { uri ->
                Image(
                    painter = rememberImagePainter(uri),
                    contentDescription = null,
                    modifier = Modifier.size(300.dp)
                )

                Spacer(modifier = Modifier.height(170.dp))

                Button(onClick = {
                    coroutineScope.launch(Dispatchers.IO) {
                        try {
                            //Crea un archivo temporal y copia los datos ahí.
                            val contentResolver = context.contentResolver //permite realizar operaciones IO sobre el contexto
                            val file = File(context.cacheDir, "temp_image.jpg")
                            val inputStream = contentResolver.openInputStream(uri)
                            file.outputStream().use { outputStream ->
                                inputStream?.copyTo(outputStream)
                            }

                            //Se convierte en un cuerpo de solicitud y se comprueba que sea del formato jpeg
                            val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                            val body =
                                MultipartBody.Part.createFormData("file", file.name, requestFile) //Nombre que se le ha dado en el back(file)

                            val response = viewModel.subirImagenCartel(cartelId.value!!, body)
                            if (response.isSuccessful) {
                                withContext(Dispatchers.Main) {//Vuelve al hilo principal
                                    navController.navigate("createBandaFragmento/${cartelId.value}/${eventoId}")
                                }
                            } else {
                                withContext(Dispatchers.Main) {
                                    viewModel.message.value =
                                        "Error al subir imagen: ${response.errorBody()?.string()}"
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

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.eliminarEvento(eventoId)
                    navController.navigate("principal")
                }
            ) {
                Text(text = "Salir sin guardar")
            }
        }
    }
}


@Composable
fun CreateBandaFragmentoScreen(navController: NavController, cartelId: Int, eventoId: Int) {

    BackHandler {

    }
    val viewModel: MyViewModel = viewModel()
    val coroutineScope = rememberCoroutineScope()
    val nombreBanda = remember { mutableStateOf("") }
    val audioUri = remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val hasAddedBanda = remember { mutableStateOf(false) }

    val audioPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        audioUri.value = uri
    }

    val isFormValid by derivedStateOf {
        nombreBanda.value.isNotBlank() && audioUri.value != null
    }

    Box(Modifier.background(Color(245, 109, 5))) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(text = "Introduce el nombre de una de las bandas del cartel. Después su canción. Pulsa en salir cuando no quieras añadir más")
            Spacer(modifier = Modifier.height(35.dp))
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


            Button(
                onClick = {
                    coroutineScope.launch(Dispatchers.IO) {
                        try {
                            val cartel = Cartel(
                                id_cartel = cartelId,
                                imagen = null,
                                evento = Evento(
                                    id_evento = eventoId,
                                    nombre = "",
                                    fecha = "",
                                    precio = 0.0
                                ),
                                bandas = mutableListOf()
                            )
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
                                val inputStream = contentResolver.openInputStream(audioUri.value!!)
                                file.outputStream().use { outputStream ->
                                    inputStream?.copyTo(outputStream)
                                }

                                val requestFile =
                                    file.asRequestBody("audio/mpeg".toMediaTypeOrNull())
                                val body = MultipartBody.Part.createFormData(
                                    "file",
                                    file.name,
                                    requestFile
                                )

                                val fragmentoResponse =
                                    viewModel.subirFragmentoCancion(bandaId, body)
                                if (fragmentoResponse.isSuccessful) {
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(
                                            context,
                                            "Banda y Fragmento de Canción insertados con éxito",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        // Limpiar campos después de la insertar
                                        nombreBanda.value = ""
                                        audioUri.value = null
                                        hasAddedBanda.value =
                                            true // Poner que se ha añadido una banda
                                    }
                                } else {
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(
                                            context,
                                            "Error al insertar fragmento: ${
                                                fragmentoResponse.errorBody()?.string()
                                            }",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            } else {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        context,
                                        "Error al insertar banda: ${
                                            bandaResponse.errorBody()?.string()
                                        }",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    context,
                                    "Excepción al insertar banda y fragmento: ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                },
                enabled = isFormValid // Botón habilitado solo si el formulario es válido
            ) {
                Text(text = "Guardar Banda y Fragmento")
            }

            Spacer(modifier = Modifier.height(200.dp))

            Button(
                onClick = {
                    navController.navigate("principal")
                },
                enabled = hasAddedBanda.value // Botón habilitado solo si se ha añadido una banda
            ) {
                Text(text = "Guardar evento y volver al menú principal")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.eliminarEvento(eventoId)
                    navController.navigate("principal")
                }
            ) {
                Text(text = "Salir sin guardar")
            }
        }
    }
}



//Metodo encargado de mostrar todos los eventos
@Composable
fun PantallaMostrarEvento(navController: NavController) {
    val viewModel: MyViewModel = viewModel()
    val eventos by viewModel.eventos.observeAsState(emptyList())
    val context = LocalContext.current

    Box(Modifier.background(Color(245, 109, 5))) {
        LaunchedEffect(Unit) {
            viewModel.obtenerTodosLosEventosDTO()
        }

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            if (eventos.isEmpty()) {
                Text(
                    text = "No hay eventos disponibles.",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .weight(0.75f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(eventos) { evento ->
                        EventCard(
                            name = evento.eventName,
                            date = evento.eventDate,
                            price = "${evento.eventPrice}€",
                            imageBase64 = evento.image,
                            onClick = { navController.navigate("evento/${evento.id_eventoDto}/${evento.id_cartel}") },
                            onDeleteClick = {
                                viewModel.eliminarEvento(evento.id_eventoDto)
                            }
                        )
                    }
                }
            }

            Button(
                onClick = { navController.navigate("principal") },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
            ) {
                Text(text = "Volver al menú principal")
            }
        }
    }


    val message by viewModel.message.observeAsState()
    message?.let {
        LaunchedEffect(it) {
            if (it == "Evento eliminado con éxito.") {
                viewModel.obtenerTodosLosEventosDTO()
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                viewModel.message.value = null
            }
        }
    }
}

//Metodo encargado de hacer las cards de los eventos
@Composable
fun EventCard(name: String, date: String, price: String, imageBase64: String?, onClick: () -> Unit, onDeleteClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable (onClick= onClick),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(5, 141, 245))
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(10.dp) ) {
            imageBase64?.let {
                val imageBytes = Base64.decode(it, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.size(175.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = name, style = MaterialTheme.typography.displayLarge)
                Text(text = date, style = MaterialTheme.typography.displayMedium)
                Text(text = price, style = MaterialTheme.typography.displayMedium)
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar Evento",
                        tint = Color.Red
                    )
                }
            }

        }
    }
}

//Metodo encargado de mostrar el cartel del evento junto con las bandas
@Composable
fun EventoScreen(navController: NavController, eventoId: Int, cartelId: Int) {
    val viewModel: MyViewModel = viewModel()
    val cartel by viewModel.cartel.observeAsState()
    val nombresBandas by viewModel.nombresBandas.observeAsState(emptyList())
    val context = LocalContext.current
    val bandaEnReproduccion by viewModel.bandaEnReproduccion.observeAsState()

    Box(Modifier.background(Color(245, 109, 5))) {

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
                            .height(450.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                Box(modifier = Modifier.weight(1f)) {
                    LazyColumn {
                        items(nombresBandas) { nombreBanda ->
                            val isPlaying = bandaEnReproduccion == nombreBanda
                            Text(
                                text = nombreBanda,
                                style = if (isPlaying) {
                                    MaterialTheme.typography.titleLarge.copy(
                                        color = Color.Green,
                                        fontWeight = FontWeight.Bold
                                    )
                                } else {
                                    MaterialTheme.typography.titleLarge
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.reproducirOPararFragmento(nombreBanda, context)
                                    }
                                    .padding(3.dp)
                                    .background(
                                        Color(5, 141, 245),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clip(RoundedCornerShape(8.dp))
                            )
                        }
                    }
                }

                Button(
                    onClick = {
                        viewModel.pararReproduccion()
                        navController.navigate("mostrarEventos")
                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp)
                ) {
                    Text(text = "Volver a la lista de eventos")
                }
            }
        } else {
            Text(text = "Cartel no encontrado")
        }
    }
}




//Metodo para mostrar la ventana principal
@Composable
fun Principal(navController: NavController) {
    Box (Modifier.background(Color(245, 109, 5))){
        
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {
            
            Spacer(modifier = Modifier.height(40.dp))
            Text(text = "BIENVENID@ A GIGLIVE",  style = MaterialTheme.typography.displayMedium.copy(fontSize = 50.sp))
        }

        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = { navController.navigate("createEvent") }) {
                Text(text = "Crear Evento")
            }
            Spacer(modifier = Modifier.height(30.dp))
            Button(onClick = { navController.navigate("mostrarEventos") }) {
                Text(text = "Mostrar eventos")
            }
        }
    }

}


