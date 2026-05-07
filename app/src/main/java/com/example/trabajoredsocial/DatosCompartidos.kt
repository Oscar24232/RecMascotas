package com.example.trabajoredsocial

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.trabajoredsocial.Modelo.Mascota
import com.example.trabajoredsocial.Modelo.Usuario

object DatosCompartidos {
    var usuario by mutableStateOf<Usuario?>(null)
    var mascotaSeleccionada: Mascota? = null
    var usuarioEditado: Usuario? = null
    var mascotaEditada: Mascota? = null
}