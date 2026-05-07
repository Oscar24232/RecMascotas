package com.example.trabajoredsocial

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.trabajoredsocial.ViewModel.ImagenViewModel
import com.example.trabajoredsocial.ViewModel.MascotasViewModel
import java.io.File
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCrearMascota(navController: NavHostController) {
    val imagenViewModel: ImagenViewModel = viewModel()
    val urlPfp by imagenViewModel.urlPfp.observeAsState()
    val imageUri by imagenViewModel.imageUri.observeAsState(Uri.EMPTY)
    val context = LocalContext.current
    var nombre by remember { mutableStateOf("") }
    var foto by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("") }
    var viewModel: MascotasViewModel= viewModel()
    //galeria
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> uri?.let {
            val inputStream = context.contentResolver.openInputStream(it)
            val tempFile = File.createTempFile("GAL_", ".jpg", context.cacheDir)

            tempFile.outputStream().use { outputStream ->
                inputStream?.copyTo(outputStream)
            }

            imagenViewModel.setImageFile(tempFile)
            imagenViewModel.updateImageUri(Uri.fromFile(tempFile))
        }
    }
    //actualizo la foto cuando se sube
    LaunchedEffect(urlPfp) {
        urlPfp?.let {
            foto = it.toString()
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Red Social Animal") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Yellow,
                    titleContentColor = Color.Black
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )

        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF2196F3), // azul arriba
                            Color(0xFFF8DB75)  // amarillo claro abajo
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        )
        {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),

                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(80.dp))

                Text("Nueva Mascota", fontSize = 24.sp)

                Spacer(modifier = Modifier.height(16.dp))
                if (imageUri != Uri.EMPTY) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Image(
                        painter = rememberAsyncImagePainter(imageUri),
                        contentDescription = "Imagen seleccionada",
                        modifier = Modifier
                            .size(150.dp),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    placeholder = { Text("Introduce Nombre de la mascota") },
                    shape = RoundedCornerShape(12.dp)
                )



                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {

                    galleryLauncher.launch("image/*")
                }) {
                    Text("Seleccionar imagen")
                }

                Button(onClick = {
                    imagenViewModel.uploadImage(context)
                }) {
                    Text("Subir imagen")
                }

                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(
                    value = tipo,
                    onValueChange = { tipo = it },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    placeholder = { Text("Tipo (perro, gato...)") },
                    shape = RoundedCornerShape(12.dp)
                )
                Button(onClick = {
                    val userId = DatosCompartidos.usuario?.id ?: ""

                    viewModel.crearMascota(
                        nombre=nombre,
                        foto=foto,
                        tipo=tipo,
                        usuarioId = userId

                    )

                    navController.popBackStack()
                }) {
                    Text("Guardar")
                }
            }
        }
    }
}