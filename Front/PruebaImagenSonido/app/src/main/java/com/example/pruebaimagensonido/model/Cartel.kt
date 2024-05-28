package com.example.pruebaimagensonido.model

data class Cartel(
    val id_cartel: Int,
    val imagen: ByteArray?,
    val evento: Evento,
    val bandas: MutableList<Banda> = mutableListOf()
)
