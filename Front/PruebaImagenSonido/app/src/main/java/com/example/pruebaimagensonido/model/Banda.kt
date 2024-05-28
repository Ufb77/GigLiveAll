package com.example.pruebaimagensonido.model

data class Banda(
    val id_banda: Int,
    val nombreBanda: String,
    val carteles: MutableList<Cartel> = mutableListOf(),
    val fragmentoCancion: FragmentoCancion? = null
)
