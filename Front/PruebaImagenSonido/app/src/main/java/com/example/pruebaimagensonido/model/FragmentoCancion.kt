package com.example.pruebaimagensonido.model

data class FragmentoCancion(val id: Int,
                            val fragmento: ByteArray,
                            val banda: Banda? = null)
