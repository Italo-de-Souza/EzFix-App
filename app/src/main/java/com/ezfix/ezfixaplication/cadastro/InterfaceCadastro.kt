package com.ezfix.ezfixaplication.cadastro

import com.ezfix.ezfixaplication.model.NovoUsuario

interface InterfaceCadastro {
    fun validaCampos(): Boolean;
    fun preencheDados(novoUsuario: NovoUsuario);
}