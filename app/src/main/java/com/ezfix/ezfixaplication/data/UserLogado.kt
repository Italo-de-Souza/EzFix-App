package com.ezfix.ezfixaplication.data

import java.io.Serializable

data class UserLogado(

//    val cpf : String,
//    val nome : String,
//    val dataNascimento : String,
//    val telefonePrimario : String,
//    val telefoneSecundario: String,
////    var enderecoEspecificos: ArrayList<EnderecoEspecifico>,
//    val usuario : Usuario

    val cpf : String,
    val email : String,
    val nome : String,
//    val dataNascimento : String,
    val telefonePrimario : String,
    val telefoneSecundario: String,
    var enderecoEspecificos: ArrayList<EnderecoEspecifico>,

): Serializable

//data class Usuario (
//    val email : String
//    )

data class EnderecoEspecifico (

    var id : Int,
    var numero : Int,
    var complemento : String,
    var enderecoGeral : EnderecoGeral

    )

data class EnderecoGeral (

    var cep : Int,
    var logradouro : String,
    var bairro : String,
    var cidade : String,
    var estado : String

)


//"cpf": "22984651855",
//"nome": "Italo De Souza",
//"dataNascimento": "1999-04-26",
//"telefonePrimario": "11951562574",
//"telefoneSecundario": "",
//"enderecoEspecificos": [
//{
//    "id": 1,
//    "numero": 285,
//    "complemento": "",
//    "enderecoGeral": {
//    "cep": 9182440,
//    "logradouro": "Rua Antônio Zanetti",
//    "bairro": "Jardim Las Vegas",
//    "cidade": "Santo André",
//    "estado": "SP"
//}
//}
//],
//"usuario": {
//    "email": "italou@live.com"
//}