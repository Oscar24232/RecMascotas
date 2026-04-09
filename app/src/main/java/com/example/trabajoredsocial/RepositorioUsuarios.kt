package com.example.trabajoredsocial

import com.example.trabajoredsocial.Modelo.Usuario
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await


class RepositorioUsuarios {

    private val firestore = Firebase.firestore
    private val auth = FirebaseAuth.getInstance()

    suspend fun registrarUsuario(
        email: String,
        password: String,
        nombre: String,
        fotoUrl: String,
        rol:Int
    ) {
        // Creo usuario en Authentication
        val result = auth.createUserWithEmailAndPassword(email, password).await()

        val userId = result.user?.uid ?: return

        // Crear objeto usuario
        val usuario = Usuario(
            id = userId,
            nombre = nombre,
            email = email,
            pass=password,
            fotoUrl = fotoUrl,
            rol=2
        )

        // Guardar en Firestore
        firestore.collection("usuarios")
            .document(userId)
            .set(usuario)
            .await()
    }

    suspend fun obtenerUsuarioActual(): Usuario? {
        val userId = auth.currentUser?.uid ?: return null

        val snapshot = firestore.collection("usuarios")
            .document(userId)
            .get()
            .await()

        return snapshot.toObject(Usuario::class.java)
    }
    suspend fun registraGmailAutentificado(
        uid: String,
        nombre: String,
        email: String,
        fotoUrl: String,
        rol: Int
    ) {
        val docRef = firestore.collection("usuarios").document(uid)
        val snapshot = docRef.get().await()

        if (!snapshot.exists()) {
            val usuario = Usuario(
                id = uid,
                nombre = nombre,
                email = email,
                fotoUrl = fotoUrl,
                rol = 2
            )

            docRef.set(usuario).await()
        }
    }
    suspend fun cerrarSesion() {
        auth.signOut()
    }
}