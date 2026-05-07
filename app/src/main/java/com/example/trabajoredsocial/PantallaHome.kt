package com.example.trabajoredsocial

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.trabajoredsocial.ItemCard.MascotaCard
import com.example.trabajoredsocial.ViewModel.MascotasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaHome(navController: NavHostController) {

    val viewModelMascotas: MascotasViewModel = viewModel()
    val mascotas by viewModelMascotas.mascotas.observeAsState(emptyList())
    val usuario = DatosCompartidos.usuario
    val context = LocalContext.current
    // ruta actual
    val currentRoute = navController.currentBackStackEntry?.destination?.route
    LaunchedEffect(Unit) {
        viewModelMascotas.cargarMascotas()
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
                        navController.navigate(Rutas.pantallaUsuario)
                    }) {
                        Icon(Icons.Default.Celebration, contentDescription = "Inicio")
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
                    navController.navigate(Rutas.pantallaCrearMascota)
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
        ) {

            if (usuario == null) {
                Text("Cargando usuario...")
                return@Box
            }

            LazyColumn {

                item {
                    Text(
                        "PUBLICACIONES DE MASCOTAS",
                        modifier = Modifier.padding(16.dp)
                    )
                }

                items(mascotas) { mascota ->

                    MascotaCard(
                        nombre = mascota.nombre,
                        foto = mascota.foto,
                        usuarioActual = usuario.nombre ?: "",
                        titulo = "Publicación de ${mascota.nombre}",
                        mascotaId = mascota.id,
                        likes = mascota.likes,
                        viewModel = viewModelMascotas
                    )
                }
            }
        }
    }
}