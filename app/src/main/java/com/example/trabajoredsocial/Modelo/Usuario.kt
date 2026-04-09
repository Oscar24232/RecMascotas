package com.example.trabajoredsocial.Modelo

data class Usuario(
    val id: String,
    val nombre: String = "",
    val email: String = "",
    val pass: String ="",
    val fotoUrl: String = "",
    var rol: Int = 2
)