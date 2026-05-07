package com.example.trabajoredsocial.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.trabajoredsocial.Modelo.Mascota
import com.example.trabajoredsocial.Modelo.Usuario
import com.google.firebase.firestore.FirebaseFirestore

class AdminViewModel : ViewModel() {

    val mascotas = MutableLiveData<List<Mascota>>()
    val usuarios = MutableLiveData<List<Usuario>>()

    private val db = FirebaseFirestore.getInstance()

    fun cargarMascotas() {
        db.collection("mascotas")
            .get()
            .addOnSuccessListener { result ->
                val lista = result.documents.mapNotNull {
                    it.toObject(Mascota::class.java)
                }
                mascotas.value = lista
            }
    }

    fun cargarUsuarios() {
        db.collection("usuarios")
            .get()
            .addOnSuccessListener { result ->
                val lista = result.documents.mapNotNull {
                    it.toObject(Usuario::class.java)
                }
                usuarios.value = lista
            }
    }

    fun actualizarUsuario(usuario: Usuario) {
        db.collection("usuarios")
            .document(usuario.id!!)
            .set(usuario)

        cargarUsuarios()
    }

    fun actualizarMascota(mascota: Mascota) {
        db.collection("mascotas")
            .document(mascota.id)
            .set(mascota)

        cargarMascotas()
    }

    fun eliminarMascota(id: String) {
        db.collection("mascotas")
            .document(id)
            .delete()

        cargarMascotas()
    }

    fun eliminarUsuario(id: String, onResult: (Boolean) -> Unit) {

        db.collection("mascotas")
            .whereEqualTo("usuarioId", id)
            .get()
            .addOnSuccessListener { result ->

                if (!result.isEmpty) {
                    onResult(false)
                } else {

                    db.collection("usuarios")
                        .document(id)
                        .delete()
                        .addOnSuccessListener {
                            onResult(true)
                            cargarUsuarios()
                        }
                }
            }
    }
}