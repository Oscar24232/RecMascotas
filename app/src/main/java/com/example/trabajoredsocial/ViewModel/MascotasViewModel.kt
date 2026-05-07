package com.example.trabajoredsocial.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trabajoredsocial.DatosCompartidos
import com.example.trabajoredsocial.Modelo.Mascota
import com.example.trabajoredsocial.RepositoriosFirebase.RepositorioMascotas
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class MascotasViewModel: ViewModel() {
    private val repoMascotas = RepositorioMascotas()
    private val _mascotas = MutableLiveData<List<Mascota>>(emptyList())
    val mascotas: LiveData<List<Mascota>> = _mascotas

    fun crearMascota(nombre: String, foto: String, tipo: String, usuarioId: String) {
        viewModelScope.launch {
            Log.d("MASCOTA", "VIEWMODEL")
            Log.d("MASCOTA ID",usuarioId)
            repoMascotas.crearMascota(nombre, foto, tipo, usuarioId)
        }
    }
    fun toggleLike(mascotaId: String, usuario: String?) {
        val db = FirebaseFirestore.getInstance()
        val ref = db.collection("mascotas").document(mascotaId)

        db.runTransaction { transaction ->
            val snapshot = transaction.get(ref)

            val currentLikes = snapshot.get("likes") as? List<String> ?: emptyList()

            val newLikes = if (currentLikes.contains(usuario)) {
                currentLikes - usuario
            } else {
                currentLikes + usuario
            }

            transaction.update(ref, "likes", newLikes)
        }
    }
    fun cargarMascotas() {
        val db = FirebaseFirestore.getInstance()

        db.collection("mascotas")
            .addSnapshotListener { snapshot, _ ->
                val lista = snapshot?.documents?.mapNotNull { doc ->
                    val mascota = doc.toObject(Mascota::class.java)
                    mascota?.copy(id = doc.id)
                } ?: emptyList()

                _mascotas.value = lista
            }
    }
    fun cargarMascotasPorUsuario(userId: String) {
        FirebaseFirestore.getInstance()
            .collection("mascotas")
            .whereEqualTo("usuarioId", userId)
            .get()
            .addOnSuccessListener { result ->

                val lista = result.documents.mapNotNull {
                    it.toObject(Mascota::class.java)
                }
                Log.d("DEBUG", "Buscando mascotas de: $userId")

                result.documents.forEach {
                    Log.d("DEBUG", it.data.toString())
                }
                _mascotas.value = lista
            }
    }
    fun actualizarMascota(mascota: Mascota) {
        FirebaseFirestore.getInstance()
            .collection("mascotas")
            .document(mascota.id)
            .set(mascota)
    }
    fun eliminarMascota(id: String) {
        FirebaseFirestore.getInstance()
            .collection("mascotas")
            .document(id)
            .delete()
            .addOnSuccessListener {
                cargarMascotasPorUsuario(DatosCompartidos.usuario!!.id.toString())
            }
    }
}