 package com.example.trabajoredsocial


import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.loginfirebase_25_26.LoginViewModel
import com.example.trabajoredsocial.ui.theme.TrabajoRedSocialTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

 class MainActivity : ComponentActivity() {
     private val viewModel = LoginViewModel()
     private lateinit var googleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //configuracion de googleSign
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        setContent {
            TrabajoRedSocialTheme {
               val navController= rememberNavController()
                NavHost(
                    navController=navController,
                    startDestination = Rutas.pantallaLogin
                ) {
                    composable(Rutas.pantallaLogin){
                        MiPantalla(navController,googleSignInClient,viewModel)
                    }
                    composable(Rutas.pantallaUsuario) {
                        PantallaHome(navController)
                    }
                    composable(Rutas.pantallaRegistro) {
                        PantallaRegistro(navController)
                    }
                }
            }
        }
    }
}
 @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
 @OptIn(ExperimentalMaterial3Api::class)
 @Composable
 fun MiPantalla(
     navController: NavHostController,
     googleSignInClient: GoogleSignInClient,
     viewModel: LoginViewModel
 ) {
     val isLoading by viewModel.isLoading.collectAsState()
     val loginSuccess by viewModel.loginSuccess.collectAsState()
     val errorMessage by viewModel.errorMessage.collectAsState()

     val context = LocalContext.current
     var usuario by remember { mutableStateOf("") }
     var password by remember { mutableStateOf("") }
     var isRegistering by remember { mutableStateOf(false) }

     val googleLauncher = rememberLauncherForActivityResult(
         contract = ActivityResultContracts.StartActivityForResult()
     ) { result ->
         if (result.resultCode == Activity.RESULT_OK) {
             val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
             try {
                 val account = task.getResult(ApiException::class.java)
                 val idToken = account?.idToken
                 if (!idToken.isNullOrEmpty()) {
                     viewModel.loginWithGoogle(idToken)
                 } else {
                     Toast.makeText(context, "No se obtuvo ID token", Toast.LENGTH_SHORT).show()
                 }
             } catch (e: ApiException) {
                 Toast.makeText(context, "Error Google Sign-In: ${e.statusCode}", Toast.LENGTH_SHORT).show()
             }
         } else {
             Toast.makeText(context, "Login cancelado", Toast.LENGTH_SHORT).show()
         }
     }
     LaunchedEffect(loginSuccess) {
         if (loginSuccess) {
             navController.navigate(Rutas.pantallaUsuario)
         }
     }

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

         ) {

             Column (
                    modifier = Modifier.fillMaxSize(),
                 Arrangement.Center,Alignment.CenterHorizontally
             ) {

                 Text(
                     "Iniciar Sesión",fontSize = 30.sp,fontWeight = FontWeight.Bold,
                     color = Color(0xFF6200EE)

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
                             viewModel.loginWithEmail(usuario.trim(), password.trim())
                         } else {
                             Toast.makeText(context, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
                         }
                     }) {
                         Text("ENTRAR")
                     }
                     Spacer(modifier = Modifier.height(70.dp))
                     Button(onClick = { navController.navigate(Rutas.pantallaRegistro) }) {
                         Text("REGISTRARSE")
                     }
                 }
                 Row {
                     Spacer(modifier = Modifier.height(16.dp))

                     Button(
                         onClick = {
                             googleLauncher.launch(googleSignInClient.signInIntent)
                             navController.navigate(Rutas.pantallaUsuario)
                         },
                         modifier = Modifier.fillMaxWidth()
                     ) {
                         Text("Iniciar sesión con Google")
                     }
                 }
             }
         }
     }
 }

