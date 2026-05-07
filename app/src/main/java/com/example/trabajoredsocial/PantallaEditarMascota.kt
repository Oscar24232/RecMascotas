package com.example.trabajoredsocial

import com.example.trabajoredsocial.Modelo.Mascota

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
fun PantallaEditarMascota(navController: NavHostController) {

    val mascota = DatosCompartidos.mascotaSeleccionada
    val viewModel: MascotasViewModel = viewModel()

    val imagenViewModel: ImagenViewModel = viewModel()
    val urlPfp by imagenViewModel.urlPfp.observeAsState()
    val imageUri by imagenViewModel.imageUri.observeAsState(Uri.EMPTY)

    val context = LocalContext.current

    var nombre by remember { mutableStateOf(mascota?.nombre ?: "") }
    var tipo by remember { mutableStateOf(mascota?.tipo ?: "") }
    var foto by remember { mutableStateOf(mascota?.foto ?: "") }

    // imagen a mostrar
    val imagenMostrar = if (imageUri != Uri.EMPTY) {
        imageUri
    } else {
        mascota?.foto?.let { Uri.parse(it) } ?: Uri.EMPTY
    }


    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val inputStream = context.contentResolver.openInputStream(it)
            val tempFile = File.createTempFile("GAL_", ".jpg", context.cacheDir)

            tempFile.outputStream().use { outputStream ->
                inputStream?.copyTo(outputStream)
            }

            imagenViewModel.setImageFile(tempFile)
            imagenViewModel.updateImageUri(Uri.fromFile(tempFile))
        }
    }

    // cuando se sube imagen actualiza foto
    LaunchedEffect(urlPfp) {
        urlPfp?.let {
            foto = it.toString()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Mascota") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Yellow,
                    titleContentColor = Color.Black
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(40.dp))


            if (imagenMostrar != Uri.EMPTY) {
                Image(
                    painter = rememberAsyncImagePainter(imagenMostrar),
                    contentDescription = "Imagen mascota",
                    modifier = Modifier.size(150.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(16.dp))


            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                placeholder = { Text("Nombre") }
            )

            Spacer(modifier = Modifier.height(16.dp))


            OutlinedTextField(
                value = tipo,
                onValueChange = { tipo = it },
                placeholder = { Text("Tipo") }
            )

            Spacer(modifier = Modifier.height(16.dp))


            Button(onClick = {
                galleryLauncher.launch("image/*")
            }) {
                Text("Seleccionar imagen")
            }

            Spacer(modifier = Modifier.height(8.dp))


            Button(onClick = {
                imagenViewModel.uploadImage(context)
            }) {
                Text("Subir imagen")
            }

            Spacer(modifier = Modifier.height(24.dp))


            Button(onClick = {

                mascota?.let {
                    viewModel.actualizarMascota(
                        it.copy(
                            nombre = nombre,
                            tipo = tipo,
                            foto = foto
                        )
                    )
                }

                navController.popBackStack()

            }) {
                Text("Guardar cambios")
            }
        }
    }
}