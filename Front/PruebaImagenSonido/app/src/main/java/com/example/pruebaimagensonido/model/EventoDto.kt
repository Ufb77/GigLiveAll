package com.example.pruebaimagensonido.model

data class EventoDto(
    val eventName: String,
    val eventDate: String,
    val eventPrice: Double,
    val image: String,
    val id_eventoDto: Int,
    val id_cartel: Int
)