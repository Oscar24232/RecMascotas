package com.example.trabajoredsocial.ItemCard

import android.util.Log
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.trabajoredsocial.DatosCompartidos
import com.example.trabajoredsocial.Modelo.Mascota
import com.example.trabajoredsocial.Rutas
import com.example.trabajoredsocial.ViewModel.MascotasViewModel

@Composable
fun MascotaCardPerfil(
    mascota: Mascota,
    navController: NavHostController,
    viewModel: MascotasViewModel
) {

    var mostrarDialogo by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .combinedClickable(
                onClick = {
                    // EDITAR
                    DatosCompartidos.mascotaSeleccionada = mascota
                    Log.d("DEBUG MASCOTA","${DatosCompartidos.mascotaSeleccionada!!.id},${DatosCompartidos.mascotaSeleccionada!!.nombre}")
                    navController.navigate(Rutas.pantallaEditarMascota)
                },
                onLongClick = {
                    // MOSTRAR DIÁLOGO
                    mostrarDialogo = true
                }
            ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Column {

            AsyncImage(
                model = mascota.foto,
                contentDescription = "Imagen mascota",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

            Text(
                text = mascota.nombre,
                modifier = Modifier.padding(12.dp),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }

    // DIÁLOGO DE CONFIRMACIÓN
    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            confirmButton = {
                Button(onClick = {
                    viewModel.eliminarMascota(mascota.id)
                    mostrarDialogo = false
                }) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                Button(onClick = {
                    mostrarDialogo = false
                }) {
                    Text("Cancelar")
                }
            },
            title = { Text("Eliminar mascota") },
            text = { Text("¿Seguro que quieres eliminar a ${mascota.nombre}?") }
        )
    }
}