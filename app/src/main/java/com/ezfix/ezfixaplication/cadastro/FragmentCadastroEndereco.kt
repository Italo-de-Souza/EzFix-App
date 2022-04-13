package com.ezfix.ezfixaplication.cadastro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import com.ezfix.ezfixaplication.MaskFormatUtil
import com.ezfix.ezfixaplication.model.NovoUsuario
import com.ezfix.ezfixaplication.databinding.FragmentCadastroEnderecoBinding
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import java.io.BufferedReader
import java.net.HttpURLConnection
import java.net.URL

class FragmentCadastroEndereco : Fragment(), InterfaceCadastro {

    private lateinit var binding : FragmentCadastroEnderecoBinding;
    private lateinit var etCep         : EditText;
    private lateinit var etLogradouro  : EditText;
    private lateinit var etNumero      : EditText;
    private lateinit var etComplemento : EditText;
    private lateinit var etBairro      : EditText;
    private lateinit var etCidade      : EditText;
    private lateinit var etEstado      : EditText;
    private var listaCampos = arrayListOf<EditText>();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentCadastroEnderecoBinding.inflate(layoutInflater);
        val view = binding.root;

        etCep           = binding.etCep;
        etLogradouro    = binding.etLogradouro;
        etNumero        = binding.etNumero;
        etComplemento   = binding.etComplemento;
        etBairro        = binding.etBairro;
        etCidade        = binding.etCidade;
        etEstado        = binding.etEstado;

//        SETANDO MÁSCARA
        etCep.addTextChangedListener(
            MaskFormatUtil.mask(binding!!.etCep, MaskFormatUtil.FORMAT_CEP) );

        etCep.doAfterTextChanged {
            if (it.toString().length == 9){
                getCep(etCep.text.toString())
            }
        }


        return view;
    }

//    Buscar endereço usando CEP
    fun getCep(cep : String){
        val url = "https://viacep.com.br/ws/${cep}/json/";

        doAsync {
            val url = URL(url);
            val urlConnection = url.openConnection() as HttpURLConnection;
            val content : String = urlConnection.inputStream.bufferedReader().use(BufferedReader::readText)
            var json = JSONObject(content);
            urlConnection.connectTimeout = 7000;

            uiThread {
                if (json.has("erro")){
                    etCep.error = "CEP Inválido";
                } else {
                    etLogradouro.setText( json.getString("logradouro") );
                    etBairro.setText( json.getString("bairro") );
                    etCidade.setText( json.getString("localidade") );
                    etEstado.setText( json.getString("uf") );

                }
            }
        }
    }

    override fun validaCampos(): Boolean {
        var validado = true;
        val validaCep = etCep.text.length == 9; // xxxxx-xxx

        //Inserindo campos na lista
        if (listaCampos.isEmpty()){
            listaCampos.add(etCep);
            listaCampos.add(etNumero);
        }

        //Verificando se o campo está em branco
        listaCampos.forEach {
            if (it.text.isBlank()) {
                it.error = "Campo obrigatório";
                validado = false;
            } else if (it.id == etCep.id && !validaCep){
                it.error = "Falta dígitos";
                validado = false;
            }
        }

        return validado;
    }

    override fun preencheDados(novoUsuario: NovoUsuario){

        novoUsuario.cep         = MaskFormatUtil.unmask(etCep.text.toString());
        novoUsuario.logradouro  = etLogradouro.text.toString();
        novoUsuario.numero      = etNumero.text.toString();
        novoUsuario.bairro      = etBairro.text.toString();
        novoUsuario.cidade      = etCidade.text.toString();
        novoUsuario.estado      = etEstado.text.toString();
        novoUsuario.complemento = etComplemento.text.toString();

    }

}