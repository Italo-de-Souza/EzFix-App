package com.ezfix.ezfixaplication.model

import java.util.ArrayList

class PerfilAssistencia {
    var id : Int = 0;
    var avaliacao :Double = 0.0;
    lateinit var nomeFantasia :String;
    lateinit var cidade :String;
    lateinit var estado :String;
    lateinit var certificados :ArrayList<Certificado>;
//    lateinit var enderecoEspecificos :EnderecoEspecificos;

}

class Certificado {
    var id : Int = 0;
    lateinit var nomeCurso : String;
    var quantidadeHoras : Int = 0;
    lateinit var dataInicio : String;
    lateinit var dataConclusao : String;
    var infoCurso = false;

    fun isVisible():Boolean{
        return infoCurso;
    }

    fun alternaInfo(visibilidade :Boolean){
        infoCurso = visibilidade;
    }

}

//class EnderecoEspecificos {
//    private var id :Int = 0;
//    private var numero :Int = 0;
//    private var cep :Int = 0;
//}
