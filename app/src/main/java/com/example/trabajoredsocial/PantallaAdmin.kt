package com.example.trabajoredsocial

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.trabajoredsocial.ItemCard.ItemCard
import com.example.trabajoredsocial.Modelo.Mascota
import com.example.trabajoredsocial.Modelo.Usuario
import com.example.trabajoredsocial.ViewModel.AdminViewModel
import com.example.trabajoredsocial.ViewModel.ImagenViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAdmin(navController: NavHostController) {

    val viewModel: AdminViewModel = viewModel()

    val mascotas by viewModel.mascotas.observeAsState(emptyList())
    val usuarios by viewModel.usuarios.observeAsState(emptyList())

    val context = LocalContext.current
    val currentRoute = navController.currentBackStackEntry?.destination?.route
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var rol by remember { mutableStateOf("") }

    var tipo by remember { mutableStateOf("") }
    var foto by remember { mutableStateOf("") }
    val usuario = DatosCompartidos.usuario
    var mostrarDialogo by remember { mutableStateOf(false) }
    var itemEliminar by remember { mutableStateOf<Any?>(null) }

    val imagenViewModel: ImagenViewModel = viewModel()
    val urlPfp by imagenViewModel.urlPfp.observeAsState()
    val imageUri by imagenViewModel.imageUri.observeAsState(Uri.EMPTY)
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
    LaunchedEffect(Unit) {
        viewModel.cargarMascotas()
        viewModel.cargarUsuarios()
    }
    LaunchedEffect(urlPfp) {
        urlPfp?.let {
            foto = it.toString()
        }
    }
    val imagenMostrar = if (imageUri != Uri.EMPTY) {
        imageUri
    } else {
        Uri.parse(foto)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ADMINISTRADOR") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Yellow,
                    titleContentColor = Color.Black
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(Rutas.pantallaUsuario)
                    }) {
                        Icon(Icons.Default.Celebration, contentDescription = "Inicio")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Rutas.PantallaRegistroUsuarioAdmin)
                    }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Nuevo Usuario")
                    }
                }
            )
        },

        bottomBar = {
            NavigationBar(containerColor = Color.Yellow) {

                NavigationBarItem(
                    selected = currentRoute == Rutas.pantallaUsuario,
                    onClick = {
                        navController.navigate(Rutas.pantallaUsuario) {
                            popUpTo(Rutas.pantallaUsuario)
                            launchSingleTop = true
                        }
                    },
                    icon = { Icon(Icons.Default.Home, null) },
                    label = { Text("Home") }
                )

                NavigationBarItem(
                    selected = currentRoute == Rutas.pantallaPerfilUsuario,
                    onClick = {
                        navController.navigate(Rutas.pantallaPerfilUsuario) {
                            navController.popBackStack()
                            launchSingleTop = true
                        }
                    },
                    icon = { Icon(Icons.Default.Person, null) },
                    label = { Text("Perfil") }
                )

                NavigationBarItem(
                    selected = currentRoute == Rutas.pantallaAdmin,
                    onClick = {
                        if (usuario?.rol==1) {
                            navController.navigate(Rutas.pantallaAdmin) {
                                navController.popBackStack()
                                launchSingleTop = true
                            }
                        }else {
                            Toast.makeText(context, "No tienes permisos", Toast.LENGTH_SHORT).show()
                        }
                    },
                    icon = { Icon(Icons.Default.Person, null) },
                    label = { Text("Admin") }
                )
            }
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Rutas.PantallaCrearMascotaAdmin)
                }
            ) {
                Icon(Icons.Default.Add, null)
            }
        }

    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF2196F3),
                            Color(0xFFF8DB75)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {

            Text("MASCOTAS")

            LazyRow {
                items(mascotas) { mascota ->

                    ItemCard(
                        nombre = mascota.nombre,
                        foto = mascota.foto,
                        onClick = {
                            DatosCompartidos.mascotaEditada = mascota
                            DatosCompartidos.usuarioEditado = null

                            nombre = mascota.nombre
                            tipo = mascota.tipo
                            foto = mascota.foto

                            email = ""
                            pass = ""
                            rol = ""
                        },
                        onLongClick = {
                            viewModel.eliminarMascota(mascota.id)
                        }
                    )
                }
            }

            Text("USUARIOS")

            LazyRow {
                items(usuarios) { usuario ->

                    ItemCard(
                        nombre = usuario.nombre!!,
                        foto = usuario.fotoUrl,
                        onClick = {
                            DatosCompartidos.usuarioEditado = usuario
                            DatosCompartidos.mascotaEditada = null

                            nombre = usuario.nombre!!
                            email = usuario.email!!
                            pass = usuario.pass
                            rol = usuario.rol.toString()

                            tipo = ""
                            foto = ""
                        },
                        onLongClick = {
                            viewModel.eliminarUsuario(usuario.id!!) { }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
            OutlinedTextField(value = pass, onValueChange = { pass = it }, label = { Text("Password") })
            OutlinedTextField(value = rol, onValueChange = { rol = it }, label = { Text("Rol") })

            OutlinedTextField(value = tipo, onValueChange = { tipo = it }, label = { Text("Tipo mascota") })
            OutlinedTextField(value = foto, onValueChange = { foto = it }, label = { Text("Foto URL") })

            Button(onClick = {

                DatosCompartidos.usuarioEditado?.let {
                    viewModel.actualizarUsuario(
                        it.copy(
                            nombre = nombre,
                            email = email,
                            pass = pass,
                            rol = rol.toIntOrNull() ?: 2
                        )
                    )
                    tipo = ""
                    foto = ""

                }

                DatosCompartidos.mascotaEditada?.let {
                    viewModel.actualizarMascota(
                        it.copy(
                            nombre = nombre,
                            tipo = tipo,
                            foto = foto
                        )
                    )

                    email = ""
                    pass = ""
                    rol = ""
                }

                Toast.makeText(context, "Actualizado", Toast.LENGTH_SHORT).show()

            }) {
                Text("Actualizar")
            }

            if (DatosCompartidos.mascotaEditada != null) {

                Button(onClick = {
                    galleryLauncher.launch("image/*")
                }
                ) {
                    Text("Seleccionar imagen")
                }

                Button(onClick = {
                    imagenViewModel.uploadImage(context)
                }) {
                    Text("Subir imagen")
                }
            }

            if (mostrarDialogo) {
                AlertDialog(
                    onDismissRequest = { mostrarDialogo = false },
                    confirmButton = {
                        Button(onClick = {

                            when (itemEliminar) {

                                is Mascota -> {
                                    viewModel.eliminarMascota((itemEliminar as Mascota).id)
                                }

                                is Usuario -> {
                                    viewModel.eliminarUsuario((itemEliminar as Usuario).id!!) { ok ->

                                        if (!ok) {
                                            Toast.makeText(
                                                context,
                                                "No se puede borrar, tiene mascotas",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }
                            }

                            mostrarDialogo = false
                        }) {
                            Text("Eliminar")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { mostrarDialogo = false }) {
                            Text("Cancelar")
                        }
                    },
                    title = { Text("¿Eliminar?") }
                )
            }
        }
    }
}