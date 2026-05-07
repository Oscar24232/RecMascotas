package com.example.trabajoredsocial.ViewModel

import androidx.lifecycle.ViewModel
import com.example.trabajoredsocial.DatosCompartidos
import com.example.trabajoredsocial.Modelo.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UsuarioViewModel : ViewModel() {
    fun actualizarUsuario(
        usuarioActual: Usuario,
        nombre: String,
        email: String,
        fotoUrl: String,
        pass: String
    ) {
        val userAuth = FirebaseAuth.getInstance().currentUser
        userAuth?.updatePassword(pass)
        val usuarioActualizado = usuarioActual.copy(
            nombre = nombre,
            email = email,
            fotoUrl = fotoUrl,
            pass = pass
        )

        // actualizo en memoria
        DatosCompartidos.usuario = usuarioActualizado

        // actualizo en Firebase
        FirebaseFirestore.getInstance()
            .collection("usuarios")
            .document(usuarioActual.id!!)
            .set(usuarioActualizado)
    }
}