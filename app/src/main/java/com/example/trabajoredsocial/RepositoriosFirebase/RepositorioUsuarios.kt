package com.example.trabajoredsocial.RepositoriosFirebase

import com.example.trabajoredsocial.DatosCompartidos
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
        rol: Int
    ) {
        val result = auth.createUserWithEmailAndPassword(email, password).await()

        val userId = result.user?.uid ?: return

        val usuario = Usuario(
            id = userId,
            nombre = nombre,
            email = email,
            pass = password,
            fotoUrl = fotoUrl,
            rol = rol
        )


        DatosCompartidos.usuario = usuario

        firestore.collection("usuarios")
            .document(userId)
            .set(usuario)
            .await()
    }

    // REGISTRO ADMIN
    suspend fun registrarUsuarioAdmin(
        email: String,
        password: String,
        nombre: String,
        fotoUrl: String,
        rol: Int
    ): Boolean {

        return try {

            val adminActual = DatosCompartidos.usuario

            val result = auth.createUserWithEmailAndPassword(email, password).await()

            val userId = result.user?.uid ?: return false

            val usuario = Usuario(
                id = userId,
                nombre = nombre,
                email = email,
                pass = password,
                fotoUrl = fotoUrl,
                rol = rol
            )

            firestore.collection("usuarios")
                .document(userId)
                .set(usuario)
                .await()

            adminActual?.let {
                auth.signInWithEmailAndPassword(it.email!!, it.pass!!).await()
                DatosCompartidos.usuario = it
            }

            true

        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
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
                rol = rol
            )

            docRef.set(usuario).await()
        }
    }

    suspend fun cerrarSesion() {
        auth.signOut()
    }
}