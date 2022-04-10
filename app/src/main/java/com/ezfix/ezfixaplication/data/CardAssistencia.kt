package com.ezfix.ezfixaplication.data

import java.io.Serializable

data class CardAssistencia(
    var content: ArrayList<CardAssist>
//    val id : Int,
//    val nomeFantasia : String,
//    val avaliacao : Double,
//    val cidade : String,
//    val estado : String Bairro
    ) : Serializable;

class CardAssist {
    var id : Int = 0;
    var nomeFantasia : String = "";
    var avaliacao : Double = 0.0;
    var cidade : String = "";
    var estado : String = "" //Bairro
}