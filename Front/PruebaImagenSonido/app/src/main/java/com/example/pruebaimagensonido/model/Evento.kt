package com.example.pruebaimagensonido.model

data class Evento(val id_evento: Int,
                  val nombre: String,
                  val fecha: String,
                  val precio: Double,
                  val cartel: Cartel? = null)
