package com.example.trabajoredsocial.RepositoriosFirebase

import com.example.trabajoredsocial.Modelo.Mascota
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class RepositorioMascotas {

    private val firestore = Firebase.firestore

    suspend fun crearMascota(nombre: String, foto: String, tipo: String, usuarioId: String) {

        val id = firestore.collection("mascotas").document().id

        val mascota = Mascota(
            id = id,
            nombre = nombre,
            foto = foto,
            tipo = tipo,
            usuarioId = usuarioId
        )

        firestore.collection("mascotas")
            .document(id)
            .set(mascota)
            .await()
    }

    suspend fun obtenerMascotas(): List<Mascota> {
        return firestore.collection("mascotas")
            .get()
            .await()
            .toObjects(Mascota::class.java)
    }
}