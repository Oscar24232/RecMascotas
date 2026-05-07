package com.example.trabajoredsocial.ItemCard

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun ItemCard(
    nombre: String,
    foto: String?,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {

    var mostrarDialogo by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .width(160.dp)
            .padding(8.dp)
            .combinedClickable(
                onClick = { onClick() },
                onLongClick = {
                    mostrarDialogo = true
                }
            ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Column {

            if (!foto.isNullOrEmpty()) {
                AsyncImage(
                    model = foto,
                    contentDescription = "Imagen",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Text(
                text = nombre,
                modifier = Modifier.padding(8.dp)
            )
        }
    }

    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            confirmButton = {
                Button(onClick = {
                    onLongClick()
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
            title = { Text("Eliminar") }
        )
    }
}