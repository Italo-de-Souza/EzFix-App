package com.ezfix.ezfixaplication.data

import android.graphics.Bitmap
import java.io.Serializable
import java.util.ArrayList

class CardPedido {
    var idOrcamento: Long = 0;
    var idAssistencia: Long = 0;
    lateinit var nomeAssistencia: String;
    lateinit var status: String;
    lateinit var itens: ArrayList<Itens>
}

class Itens {
    lateinit var marca : String;
    lateinit var modelo : String
}
