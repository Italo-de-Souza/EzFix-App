package com.ezfix.ezfixaplication.model

class PerfilAssistencia {
    var id : Int = 0;
    var avaliacao :Double = 0.0;
    lateinit var nomeFantasia :String;
    lateinit var telefonePrimario :String;
    lateinit var telefoneSecundario :String;
    lateinit var enderecoEspecificos :EnderecoEspecificos;
}

class EnderecoEspecificos {
    private var id :Int = 0;
    private var numero :Int = 0;
    private var cep :Int = 0;
}
