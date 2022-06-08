package com.ezfix.ezfixaplication.data

data class PedidoDetalhe (
    val idOrcamento : Long,
    val valorTotal : Double,
    val status : String,
    val nomeAssistencia : String,
    val dataSolicitacao : String,
    val itemOrcamentoList : ArrayList<ItensList>
)

data class ItensList(
    val descricao : String,
    val marca     : String,
    val tipo      : String,
    val modelo    : String
)
