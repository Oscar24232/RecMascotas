package com.example.trabajoredsocial

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.loginfirebase_25_26.LoginViewModel
import com.example.trabajoredsocial.Modelo.Usuario


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaRegistro(navController: NavHostController) {
    val viewModel: LoginViewModel = viewModel()
    val context = LocalContext.current
    val loginSuccess by viewModel.loginSuccess.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var fotoUrl by remember { mutableStateOf("") }

    LaunchedEffect(loginSuccess) {
        if (loginSuccess) {
            Toast.makeText(context, "Registro correcto", Toast.LENGTH_SHORT).show()
            navController.navigate(Rutas.pantallaUsuario)
        }else{
            errorMessage
            Toast.makeText(context, "No introducido correctamente", Toast.LENGTH_SHORT).show()

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
                        navController.navigate(Rutas.pantallaLogin)
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

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "¡Bienvenido!",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6200EE)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Crea tu cuenta para continuar",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )


                Spacer(modifier = Modifier.height(16.dp))


                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    placeholder = { Text("Introduce email") },
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))


                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    placeholder = { Text("Introduce contraseña") },
                    //redondearr la caja de textBox
                    shape = RoundedCornerShape(12.dp),
                    visualTransformation = PasswordVisualTransformation()
                )
                Spacer(modifier = Modifier.height(16.dp))


                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    placeholder = { Text("Introduce nombre") },
                    //redondearr la caja de textBox
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))


                OutlinedTextField(
                    value = fotoUrl,
                    onValueChange = { fotoUrl = it },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    placeholder = { Text("Introduce fotoUrl") },
                    //redondearr la caja de textBox
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally)
                 {
                     Button(onClick = {
                         if (email.isEmpty() || password.isEmpty()) {
                             Toast.makeText(context, "Rellena los campos", Toast.LENGTH_SHORT).show()
                             return@Button
                         }

                         if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                             Toast.makeText(context, "Email no válido", Toast.LENGTH_SHORT).show()
                             return@Button
                         }

                         if (password.length < 6) {
                             Toast.makeText(context, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
                             return@Button
                         }

                         viewModel.registerWithEmail(email, password,nombre,fotoUrl,2)
                     }) {
                         Text("REGISTRARSE")
                     }
                    Button(onClick = { navController.navigate(Rutas.pantallaLogin) }) {
                        Text("REGISTRARSE CON GOOGLE")
                    }
                }
            }

        }

    }
}