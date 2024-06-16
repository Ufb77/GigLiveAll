package com.example.pruebaimagensonido



import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pruebaimagensonido.ui.theme.PruebaImagenSonidoTheme
import com.example.pruebaimagensonido.view.CreateBandaFragmentoScreen
import com.example.pruebaimagensonido.view.CreateCartelScreen
import com.example.pruebaimagensonido.view.CreateEventScreen
import com.example.pruebaimagensonido.view.EventoScreen
import com.example.pruebaimagensonido.view.PantallaMostrarEvento
import com.example.pruebaimagensonido.view.Principal

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PruebaImagenSonidoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "principal") {
                        composable("principal") {
                            Principal(navController = navController)
                        }
                        composable("createEvent") {
                            CreateEventScreen(navController)
                        }
                        composable("createCartel/{eventoId}") { backStackEntry ->
                            val eventoId = backStackEntry.arguments?.getString("eventoId")?.toIntOrNull() ?: 0
                            CreateCartelScreen(navController, eventoId)
                        }
                        composable("createBandaFragmento/{cartelId}/{eventoId}") { backStackEntry ->
                            val cartelId = backStackEntry.arguments?.getString("cartelId")?.toIntOrNull() ?: 0
                            val eventoId = backStackEntry.arguments?.getString("eventoId")?.toIntOrNull() ?: 0
                            CreateBandaFragmentoScreen(navController, cartelId, eventoId)
                        }
                        composable("mostrarEventos") {
                            PantallaMostrarEvento(navController)
                        }
                        composable("evento/{eventoId}/{cartelId}") { backStackEntry ->
                            val eventoId = backStackEntry.arguments?.getString("eventoId")?.toIntOrNull() ?: 0
                            val cartelId = backStackEntry.arguments?.getString("cartelId")?.toIntOrNull() ?: 0
                            EventoScreen(navController, eventoId, cartelId)
                        }
                    }
                }
            }
        }
    }
}


// @Composable
// fun obtenerImagen(name: String, modifier: Modifier = Modifier) {
// Box(){
// Image(painter = painterResource(id = R.drawable.ic_launcher_background),
// contentDescription = null,
// contentScale = ContentScale.FillBounds,
// modifier = Modifier.fillMaxSize())
//
// val imageUri = rememberSaveable {
// mutableStateOf("")
// }
//
// val painter = rememberAsyncImagePainter(imageUri.value.ifEmpty { R.drawable.ic_launcher_foreground })
//
// val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri?-> uri?.let { imageUri.value = it.toString() }
//
// }
//
// Column {
// Image(painter = painter, contentDescription = null,
// modifier = Modifier.clickable { launcher.launch("image/*") })
// }
// }
// }
//
// @Preview(showBackground = true)
// @Composable
// fun GreetingPreview() {
// PruebaImagenSonidoTheme {
// obtenerImagen("Android")
// }
// }
// *