package com.ezfix.ezfixaplication.data

import java.util.ArrayList

class Produtos {
    var id : Int = 0
    lateinit var modelo : InfoProduto
    lateinit var marca : InfoProduto
    lateinit var tipo : InfoProduto
}


class InfoProduto{
    var id : Long = 0
    lateinit var nome : String
}
