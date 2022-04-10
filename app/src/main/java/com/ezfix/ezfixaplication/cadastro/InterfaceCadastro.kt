package com.ezfix.ezfixaplication.cadastro

import android.widget.EditText
import com.ezfix.ezfixaplication.cadastro.model.NovoUsuario

interface InterfaceCadastro {
    fun validaCampos(): Boolean;
    fun preencheDados(novoUsuario: NovoUsuario);
}