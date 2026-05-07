package com.example.trabajoredsocial

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.loginfirebase_25_26.LoginViewModel
import com.example.trabajoredsocial.ItemCard.MascotaCardPerfil
import com.example.trabajoredsocial.ViewModel.MascotasViewModel
import com.example.trabajoredsocial.ViewModel.UsuarioViewModel
import com.google.firebase.firestore.FirebaseFirestore


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPerfil(navController: NavHostController) {
    val viewModeLogin: LoginViewModel=viewModel()
    val viewModel: MascotasViewModel = viewModel()
    val mascotas by viewModel.mascotas.observeAsState(emptyList())
    val usuario = DatosCompartidos.usuario
    var nombre by remember { mutableStateOf(usuario?.nombre ?: "") }
    var email by remember { mutableStateOf(usuario?.email ?: "") }
    var pass by remember { mutableStateOf(usuario?.pass ?: "") }
    var fotoUrl by remember { mutableStateOf(usuario?.fotoUrl ?: "") }
    var viewModelUsu: UsuarioViewModel =viewModel()
    val context = LocalContext.current
    val currentRoute = navController.currentBackStackEntry?.destination?.route

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
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
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
                            popUpTo(Rutas.pantallaUsuario)
                            launchSingleTop = true
                        }
                    },
                    icon = { Icon(Icons.Default.Person, null) },
                    label = { Text("Perfil") }
                )

                NavigationBarItem(
                    selected = currentRoute == Rutas.pantallaAdmin,
                    onClick = {
                        navController.navigate(Rutas.pantallaAdmin) {
                            popUpTo(Rutas.pantallaUsuario)
                            launchSingleTop = true
                        }
                    },
                    icon = { Icon(Icons.Default.AccountCircle, null) },
                    label = { Text("Ajustes") }
                )
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

        LaunchedEffect(usuario?.id) {
            usuario?.id?.let {
                viewModel.cargarMascotasPorUsuario(it)
            }
        }

        if (usuario == null) {
            Text("Cargando...")
            return@Scaffold
        }

        val mascotasOrdenadas = mascotas.sortedByDescending {
            it.likes?.size ?: 0
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {

            //LISTA DE MASCOTAS
            Text(
                "MIS MASCOTAS",
                modifier = Modifier.padding(16.dp)
            )

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp)
            ) {
                items(mascotasOrdenadas) { mascota ->

                    MascotaCardPerfil(
                        mascota = mascota,
                        navController = navController,
                        viewModel = viewModel
                    )
                }
            }

            //DATOS USUARIO
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                Text("DATOS DE MI PERFIL")

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre=it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email=it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = usuario.fotoUrl ?: "",
                    onValueChange = { },
                    label = { Text("Foto") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = pass,
                    onValueChange = {pass= it},
                    label = { Text("Contraseña") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(15.dp))
                Row {

                    Button(onClick = {
                        viewModelUsu.actualizarUsuario(usuario, nombre, email, fotoUrl, pass)
                        Toast.makeText(context, "Actualizado correctamente", Toast.LENGTH_SHORT)
                            .show()

                    },Modifier.weight(1f) ) {

                        Text("Actualizar perfil")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = {
                        viewModeLogin.signOut(context)
                        Toast.makeText(context, "Cerrando sesion", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    },Modifier.weight(1f)) {
                        Text("Cerrar Sesion")
                    }
                }
            }
        }
    }
}