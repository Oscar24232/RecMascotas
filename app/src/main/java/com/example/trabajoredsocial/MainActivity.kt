 package com.example.trabajoredsocial


import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Cyan
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.trabajoredsocial.ui.theme.TrabajoRedSocialTheme



 class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TrabajoRedSocialTheme {
               val navController= rememberNavController()
                NavHost(
                    navController=navController,
                    startDestination = Rutas.pantallaLogin
                ) {
                    composable(Rutas.pantallaLogin){
                        MiPantalla(navController)
                    }
                    composable(Rutas.pantallaUsuario) {
                        PantallaHome(navController)
                    }
                    composable(Rutas.pantallaAdmin) {  }
                }
            }
        }
    }
}

 @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
 @OptIn(ExperimentalMaterial3Api::class)
 @Composable
 fun MiPantalla(navController: NavHostController) {
     val context = LocalContext.current
     var usuario by remember { mutableStateOf("") }
     var password by remember { mutableStateOf("") }
     Scaffold(
         topBar = {
             TopAppBar(
                 title = { Text("Red Social Animal") },
                 colors = TopAppBarDefaults.topAppBarColors(
                     containerColor = Color.Yellow,
                     titleContentColor = Color.Black
                 )

             )
         }
     ) { padding ->

         Box(
             modifier = Modifier
                 .fillMaxSize()
                 .padding(padding),
             contentAlignment = Alignment.Center

         ) {

             Column (
                    modifier = Modifier.fillMaxSize().background(Cyan),
                 Arrangement.Center,Alignment.CenterHorizontally
             ) {

                 Text(
                     "Iniciar Sesión",fontSize = 30.sp,fontWeight = FontWeight.Bold

                 )

                 Spacer(modifier = Modifier.height(16.dp))


                 OutlinedTextField(
                     value = usuario,
                     onValueChange = {usuario=it},
                     colors = TextFieldDefaults.colors(
                         focusedContainerColor = Color.White,
                         unfocusedContainerColor = Color.White
                     ),
                     placeholder = { Text("Introduce usuario") },
                     shape = RoundedCornerShape(12.dp)
                 )

                 Spacer(modifier = Modifier.height(16.dp))


                 OutlinedTextField(
                     value = password,
                     onValueChange = {password=it},
                     colors = TextFieldDefaults.colors(
                         focusedContainerColor = Color.White,
                         unfocusedContainerColor = Color.White
                     ),
                     placeholder = { Text("Introduce contraseña") },
                     //redondearr la caja de textBox
                     shape = RoundedCornerShape(12.dp),
                     visualTransformation = PasswordVisualTransformation()
                 )

                 Spacer(modifier = Modifier.height(20.dp))

                 Row(
                     horizontalArrangement = Arrangement.spacedBy(16.dp)
                 ) {
                     Button(onClick = {
                         if (usuario.isNotEmpty() && password.isNotEmpty()) {
                             navController.navigate(Rutas.pantallaUsuario)
                         } else {
                             Toast.makeText(
                                 context,
                                 "Rellena todos los campos",
                                 Toast.LENGTH_SHORT
                             ).show()
                         }
                     }) {
                         Text("ENTRAR")
                     }

                     Button(onClick = { }) {
                         Text("REGISTRARSE")
                     }
                 }
             }
         }
     }
 }

