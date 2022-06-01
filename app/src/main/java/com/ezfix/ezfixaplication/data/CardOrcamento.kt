package com.ezfix.ezfixaplication.data

data class CardOrcamento(
    val problema : String,
    val produto : String,
    val observacao : String
)

data class Orcamento(
    val descricao : String,
    val produto : IdProduto
)

data class IdProduto (
    val id : Long
)


