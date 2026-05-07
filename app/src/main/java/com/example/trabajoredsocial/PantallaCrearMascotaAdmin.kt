package com.example.trabajoredsocial

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.trabajoredsocial.ItemCard.ItemCard
import com.example.trabajoredsocial.ViewModel.AdminViewModel
import com.example.trabajoredsocial.ViewModel.ImagenViewModel
import com.example.trabajoredsocial.ViewModel.MascotasViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCrearMascotaAdmin(navController: NavHostController) {

    val imagenViewModel: ImagenViewModel = viewModel()
    val viewModel: MascotasViewModel = viewModel()
    val adminViewModel: AdminViewModel = viewModel()

    val urlPfp by imagenViewModel.urlPfp.observeAsState()
    val imageUri by imagenViewModel.imageUri.observeAsState(Uri.EMPTY)
    val usuarios by adminViewModel.usuarios.observeAsState(emptyList())

    val context = LocalContext.current

    var nombre by remember { mutableStateOf("") }
    var foto by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("") }


    LaunchedEffect(Unit) {
        adminViewModel.cargarUsuarios()
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

    // actualizar foto
    LaunchedEffect(urlPfp) {
        urlPfp?.let {
            foto = it.toString()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva Mascota (Admin)") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(16.dp))


            Text("Selecciona usuario")

            LazyRow {
                items(usuarios) { usuario ->

                    ItemCard(
                        nombre = usuario.nombre ?: "",
                        foto = usuario.fotoUrl,
                        onClick = {
                            DatosCompartidos.usuarioEditado = usuario
                        },
                        onLongClick = { }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Nueva Mascota", fontSize = 24.sp)

            Spacer(modifier = Modifier.height(16.dp))

            //PREVIEW
            if (imageUri != Uri.EMPTY) {
                Image(
                    painter = rememberAsyncImagePainter(imageUri),
                    contentDescription = null,
                    modifier = Modifier.size(150.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") }
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

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = tipo,
                onValueChange = { tipo = it },
                label = { Text("Tipo") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {

                val userId = DatosCompartidos.usuarioEditado?.id

                if (userId.isNullOrEmpty()) {
                    Toast.makeText(context, "Selecciona un usuario", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                viewModel.crearMascota(
                    nombre = nombre,
                    foto = foto,
                    tipo = tipo,
                    usuarioId = userId
                )

                Toast.makeText(context, "Mascota creada", Toast.LENGTH_SHORT).show()

                navController.popBackStack()

            }) {
                Text("Guardar")
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}