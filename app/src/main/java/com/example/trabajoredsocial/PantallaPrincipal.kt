package com.example.trabajoredsocial

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun PantallaHome(navController: NavHostController) {
    Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
    )   {
            Text("Pantalla Home 🐶🔥")
        }
}
@Composable
fun RVUsuariosFilas() {
    val context = LocalContext.current

    LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        items(/*generarUsuarios()*/) {
            ItemUsuario(u = it) { // Llamada a la función donde es clickable el card en ItemUsuario
                Toast.makeText(
                    context,
                    "Usuario seleccionado: $it",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}

@Composable
fun ItemUsuario(u: Usuario, onClick: (Usuario) -> Unit) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick(u) }
    ) {

        Image(
            painter = painterResource(id = u.imagen),
            contentDescription = "Foto usuario",
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(text = u.nombre)
    }
}
