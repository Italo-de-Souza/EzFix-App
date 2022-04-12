package com.ezfix.ezfixaplication.data

import java.io.Serializable

data class CardAssistencia(
    var content: ArrayList<CardAssist>,
    var totalPages : Int,
    var pageable : Pageable
    ) : Serializable;

data class CardAssist (
    var id : Int = 0,
    var nomeFantasia : String = "",
    var avaliacao : Double = 0.0,
    var cidade : String = "",
    var estado : String = "" //Bairro
)

data class Pageable (
    var pageSize    : Int = 0,
    var pageNumber  : Int = 0,
    var offset      : Int = 0,
    var paged       : Boolean = true,
    var unpaged     : Boolean = false
)