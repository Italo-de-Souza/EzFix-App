package com.ezfix.ezfixaplication.cadastro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.ezfix.ezfixaplication.model.NovoUsuario
import com.ezfix.ezfixaplication.databinding.FragmentCadastroContaBinding

class FragmentCadastroConta : Fragment(), InterfaceCadastro {

    private lateinit var binding : FragmentCadastroContaBinding;
    lateinit var etEmailCadastro: EditText;
    lateinit var etSenhaCadastro: EditText;
    lateinit var etConfirmaSenha: EditText;
    private var listaCampos = arrayListOf<EditText>();


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentCadastroContaBinding.inflate(layoutInflater)
        var view = binding.root;


        etEmailCadastro = binding.etEmailCadastro;
        etSenhaCadastro = binding.etSenhaCadastro1;
        etConfirmaSenha = binding.etSenhaCadastro2;

        return view;
    }

    override fun validaCampos():Boolean{
        var validado = true;
        val validaEmail = android.util.Patterns.EMAIL_ADDRESS.matcher(etEmailCadastro.text).matches()
        val confirmaSenha = etSenhaCadastro.text.toString() == etConfirmaSenha.text.toString();

        //Inserindo campos na lista
        if (listaCampos.isEmpty()){
            listaCampos.add(etEmailCadastro);
            listaCampos.add(etSenhaCadastro);
            listaCampos.add(etConfirmaSenha);
        }

        //Verificando se o campo está em branco
        listaCampos.forEach {
            if (it.text.isBlank()) {
                it.error = "Campo obrigatório"
                validado = false;
            } else if (it.id == etEmailCadastro.id && !validaEmail) {
                it.error = "Digite um email válido";
                validado = false;
            } else if (it.id == etSenhaCadastro.id && !validaSenha(it)) {
                validado = false;

            } else if (it.id == etConfirmaSenha.id && !confirmaSenha){
                it.error = "Senhas não conferem"
                validado = false;
            }
        }
        return validado;
    };

    private fun validaSenha (v:EditText):Boolean{
        val senha = v.text.toString();
        return when{
            senha.length < 8 -> {v.error = "Mínimo 8 caracteres"; false };
            !senha.contains("[a-zA-Z]".toRegex()) -> {v.error = "Deve conter letras"; false };
            !senha.contains("[0-9]".toRegex()) -> {v.error = "Deve conter números"; false };
            else -> true;
        }
    }

    override fun preencheDados(novoUsuario: NovoUsuario) {

        novoUsuario.email = etEmailCadastro.text.toString();
        novoUsuario.senha = etSenhaCadastro.text.toString();

    }


}