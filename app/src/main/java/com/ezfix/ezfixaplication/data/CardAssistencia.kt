package com.ezfix.ezfixaplication.data

import java.io.Serializable

data class CardAssistencia(
    var content: ArrayList<CardAssist>,
    var totalPages : Int
    ) : Serializable;

data class CardAssist (
    var id : Int = 0,
    var nomeFantasia : String = "",
    var avaliacao : Double = 0.0,
    var cidade : String = "",
    var estado : String = "" //Bairro
)
