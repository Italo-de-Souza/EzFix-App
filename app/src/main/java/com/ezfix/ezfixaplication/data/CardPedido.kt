package com.ezfix.ezfixaplication.data

import android.graphics.Bitmap
import java.io.Serializable

data class CardPedido (
    var id : Int,
    var imgAssistencia : Bitmap,
    var nomeAssistencia : String,
    var statuPedido : String,
) : Serializable;