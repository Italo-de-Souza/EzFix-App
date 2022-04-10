package com.ezfix.ezfixaplication.cadastro

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.ezfix.ezfixaplication.ActivityLogin
import com.ezfix.ezfixaplication.ActivityMain
import com.ezfix.ezfixaplication.R
import com.ezfix.ezfixaplication.cadastro.model.NovoUsuario
import com.ezfix.ezfixaplication.configuration.HttpRequest
import com.ezfix.ezfixaplication.databinding.ActivityCadastroBinding
import com.google.gson.Gson
import com.shuhart.stepview.StepView
import org.jetbrains.anko.doAsync
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.invoke.MethodHandles
import java.util.*
import kotlin.concurrent.schedule

class ActivityCadastro : AppCompatActivity() {
//  Declaração Fragments
    private lateinit var fragmentConta : FragmentCadastroConta;
    private lateinit var fragmentDados : FragmentCadastroDados;
    private lateinit var fragmentEndereco: FragmentCadastroEndereco;
//  VARIAVEIS
    private lateinit var btnAvancar : Button;
    private lateinit var btnVoltar : Button;
    private lateinit var stepView  : StepView;
//    BINDING
    private lateinit var binding : ActivityCadastroBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroBinding.inflate(layoutInflater);
        val view = binding.root;
        setContentView(view);


//      ATRIBUINDO VALORES
        btnAvancar = binding.btnAvancar;
        btnVoltar = binding.btnVoltar;

//      AJUSTANDO STEPVIEW
        stepView = binding.stepView;
        val steps = arrayListOf<String>("Conta","Dados Pessoais", "Endereço")

        stepView.state.steps(steps)
            .typeface(ResourcesCompat.getFont(this, R.font.poppins_bold))
            .nextTextColor(ContextCompat.getColor(this, R.color.gray))
            .commit();


//      Instanciando FRAGMENTS
        fragmentConta    = FragmentCadastroConta();
        fragmentDados    = FragmentCadastroDados();
        fragmentEndereco = FragmentCadastroEndereco();


//      ONCLICKS
        btnAvancar.setOnClickListener{
            if (checkValidacao(stepView.currentStep)){
                setFragment(stepView.currentStep + 1);
                stepView.go(stepView.currentStep + 1, true);
            }
        }
        btnVoltar.setOnClickListener{
            stepView.go(stepView.currentStep - 1, true);
            setFragment(stepView.currentStep - 1);

        }


//      INICIALIZANDO STEVIEW
        if (savedInstanceState == null){
            setFragment(stepView.currentStep);
        }

    }

    //Função para setar o fragment de acordo com o step atual
    private fun setFragment(step : Int){
        val fragmentTransaction = supportFragmentManager.beginTransaction();
        val flFragments = binding.flFragments;

        when(step){
            0 -> {
                fragmentTransaction.replace(flFragments.id, fragmentConta).commit()
                btnVoltar.visibility = View.INVISIBLE;
            };
            1 -> {
                fragmentTransaction.replace(flFragments.id, fragmentDados).commit()
                btnVoltar.visibility = View.VISIBLE;
                btnAvancar.text = "Avançar";
            };
            2 -> {
                fragmentTransaction.replace(flFragments.id, fragmentEndereco).commit();
                btnAvancar.text = "Concluir";
            }
            3 -> {
                criaUsuario();
                Timer().schedule(2000) {
                    startActivity( Intent(this@ActivityCadastro, ActivityLogin::class.java) );
                    finish();
                }
            }
        }

    }

    //Função que retorna validações dos campo da view
    private fun checkValidacao(stepAtual : Int):Boolean{
        when(stepAtual){
            0 -> return fragmentConta.validaCampos();
            1 -> return fragmentDados.validaCampos();
            2 -> return fragmentEndereco.validaCampos();
            else -> return false;
        }

    }

//Criação do usuário
    fun criaUsuario(){
        val novoUsuario = NovoUsuario();
        val alert = AlertDialog.Builder(this);

        fragmentConta.preencheDados(novoUsuario);
        fragmentDados.preencheDados(novoUsuario);
        fragmentEndereco.preencheDados(novoUsuario);

        alert.setTitle("Aguarde!")
            .setMessage("Criando usuário...")
            .setCancelable(false)
            .create().show();

        val http = HttpRequest.requerir();
        http.criarUsuario(novoUsuario).enqueue(object : Callback<Void>{

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.code() == 201) {
                    alert.setTitle("Usuário criado!").setMessage("Realize login.").show();
                } else {
                    alert.setTitle("Erro!").setMessage("CPF ou Email já cadastrado.\nRealize o login")
                        .show();
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("api", t.message!!)
                alert.setTitle("Erro!").setMessage(t.message).show();
            }
        })
    }

}
