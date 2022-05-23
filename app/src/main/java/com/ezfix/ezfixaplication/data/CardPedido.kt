package com.ezfix.ezfixaplication.data

import android.graphics.Bitmap
import java.io.Serializable
import java.util.ArrayList

class CardPedido {
    var idOrcamento: Int = 0;
    var idAssistencia: Int = 0;
    lateinit var nomeAssistencia: String;
    lateinit var status: String;
    lateinit var itens: ArrayList<Itens>
    var btnClick = false;

    fun setBtnClick(){
        btnClick = true;
    }


    fun isBtnClicked():Boolean{
        return btnClick;
    }
}

class Itens {
    lateinit var marca : String;
    lateinit var modelo : String
}
