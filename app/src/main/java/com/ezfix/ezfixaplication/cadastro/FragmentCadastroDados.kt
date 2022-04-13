package com.ezfix.ezfixaplication.cadastro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.ezfix.ezfixaplication.MaskFormatUtil
import com.ezfix.ezfixaplication.model.NovoUsuario
import com.ezfix.ezfixaplication.databinding.FragmentCadastroDadosBinding

class FragmentCadastroDados : Fragment(), InterfaceCadastro{

    private lateinit var binding         : FragmentCadastroDadosBinding;
    private lateinit var etNome          : EditText;
    private lateinit var etCpf           : EditText;
    private lateinit var etNascimento    : EditText;
    private lateinit var etTelPrimario   : EditText;
    private lateinit var etTelSecundario : EditText;
    private var listaCampos = arrayListOf<EditText>();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentCadastroDadosBinding.inflate(layoutInflater);
        var view = binding.root;

//        Inicializando variaveis
        etNome          = binding.etNome;
        etCpf           = binding.etCpf;
        etNascimento    = binding.etNascimento;
        etTelPrimario   = binding.etTelefonePrimario;
        etTelSecundario = binding.etTelefoneSecundario;

//        SETANDO MÁSCARAS
        etCpf.addTextChangedListener(
            MaskFormatUtil.mask(binding!!.etCpf, MaskFormatUtil.FORMAT_CPF) );

        etNascimento.addTextChangedListener(
            MaskFormatUtil.mask(etNascimento, MaskFormatUtil.FORMAT_DATE) );

        etTelPrimario.addTextChangedListener(
            MaskFormatUtil.mask(etTelPrimario, MaskFormatUtil.FORMAT_FONE_COD_AREA) );

        etTelSecundario.addTextChangedListener(
            MaskFormatUtil.mask(etTelSecundario, MaskFormatUtil.FORMAT_FONE_COD_AREA) );


        return view;
    }

    override fun validaCampos(): Boolean {
        var validado = true;
        val validaCpf = etCpf.text.length == 14;                // xxx.xxx.xxx-xx
        val validaNascimento = etNascimento.text.length == 10;  // xx/xx/xxxx
        val validaTelPrimario = etTelPrimario.text.length == 14;   // (xx)xxxxx-xxxx
        val validaTelSecundario = etTelSecundario.text.length in 1..13;   // (xx)xxxxx-xxxx

        //Inserindo campos na lista
        if (listaCampos.isEmpty()){
            listaCampos.add(etNome);
            listaCampos.add(etCpf);
            listaCampos.add(etNascimento);
            listaCampos.add(etTelPrimario);
        }

        //Verificando se o campo está em branco
        listaCampos.forEach {
            if (it.text.isBlank()) {
                it.error = "Campo obrigatório";
                validado = false;
            } else if (it.id == etNome.id && !checkLetters(it.text.toString())){
                it.error = "Nome Inválido";
                validado = false;
            } else if (it.id == etCpf.id && !validaCpf){
                it.error = "Faltam dígitos";
                validado = false;
            } else if (it.id == etNascimento.id && !validaNascimento){
                it.error = "Data Inválida";
                validado = false;
            } else if (it.id == etTelPrimario.id && !validaTelPrimario){
                it.error = "Faltam dígitos";
                validado = false;
            }
        }
        if (validaTelSecundario){
            etTelSecundario.error = "Faltam dígitos";
            validado = false;
        }

        return validado;
    }

    override fun preencheDados(novoUsuario: NovoUsuario) {

        novoUsuario.cpf                 = MaskFormatUtil.unmask(etCpf.text.toString());
        novoUsuario.telefonePrimario    = MaskFormatUtil.unmask(etTelPrimario.text.toString());
        novoUsuario.telefoneSecundario  = MaskFormatUtil.unmask(etTelSecundario.text.toString());
        novoUsuario.nome                = etNome.text.toString();
        novoUsuario.dataNascimento      = etNascimento.text.toString();

    }

    private fun checkLetters(str: String): Boolean {
        return str.matches("[a-zA-Z\\u00C0-\\u00FF ]+".toRegex())
    }


}