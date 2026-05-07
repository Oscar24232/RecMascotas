package com.example.trabajoredsocial.ItemCard

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import coil.compose.AsyncImage
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import com.example.trabajoredsocial.ViewModel.MascotasViewModel


@Composable
fun MascotaCard(
    nombre: String,
    foto: String,
    usuarioActual: String?,
    titulo: String,
    mascotaId: String,
    likes: List<String>,
    viewModel: MascotasViewModel
) {

    val isLiked = likes.contains(usuarioActual)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Column {

            Text(
                text = titulo,
                modifier = Modifier.padding(12.dp)
            )

            AsyncImage(
                model = foto,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentScale = ContentScale.Crop
            )

            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    viewModel.toggleLike(mascotaId, usuarioActual)
                }) {
                    Icon(
                        imageVector = if (isLiked)
                            Icons.Default.Favorite
                        else
                            Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        tint = if (isLiked) Color.Red else Color.Gray
                    )
                }

                Text("${likes.size} likes")
            }

            Text(nombre, modifier = Modifier.padding(horizontal = 12.dp))

            if (likes.isNotEmpty()) {
                Text(
                    text = "Les gusta a: ${likes.joinToString(", ")}",
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}