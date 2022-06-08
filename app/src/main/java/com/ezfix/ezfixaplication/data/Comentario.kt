package com.ezfix.ezfixaplication.data

data class Comentario (
    val comentario : String,
    val avaliacao  : Double,
    val assistencia : Id,
    val orcamento   : Id
)

data class Id (
    val id : Long
)
