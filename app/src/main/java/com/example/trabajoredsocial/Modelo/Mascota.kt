package com.example.trabajoredsocial.Modelo

data class Mascota(
    var id: String = "",
    var nombre: String = "",
    var foto: String = "",
    var usuarioId: String = "",
    var tipo: String = "",
    val likes: List<String> = emptyList()
)