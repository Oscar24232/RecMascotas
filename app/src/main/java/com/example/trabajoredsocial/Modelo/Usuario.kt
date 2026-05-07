package com.example.trabajoredsocial.Modelo

data class Usuario(
    var id: String? = "",
    var nombre: String? = "",
    var email: String? = "",
    var pass: String = "",
    var fotoUrl: String = "",
    var rol: Int = 2
)