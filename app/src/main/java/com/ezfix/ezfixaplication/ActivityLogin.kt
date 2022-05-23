package com.ezfix.ezfixaplication

import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.ezfix.ezfixaplication.cadastro.ActivityCadastro
import com.ezfix.ezfixaplication.configuration.Constants
import com.ezfix.ezfixaplication.configuration.HttpRequest
import com.ezfix.ezfixaplication.configuration.NetworkChecker
import com.ezfix.ezfixaplication.data.FormLogin
import com.ezfix.ezfixaplication.data.Token
import com.ezfix.ezfixaplication.data.UserLogado
import com.ezfix.ezfixaplication.databinding.ActivityLoginBinding
import com.ezfix.ezfixaplication.mainscreen.ActivityMain
import org.jetbrains.anko.toast
import retrofit2.Response
import retrofit2.Call
import retrofit2.Callback

class ActivityLogin : AppCompatActivity(){

    private lateinit var binding : ActivityLoginBinding;
    //Obtendo dados das Variaveis de seleção
    private lateinit var btnCadastrar : Button;
    private lateinit var btnEntrar : Button;
    private lateinit var tvLoginReult : TextView;
    private lateinit var etEmail : EditText;
    private lateinit var etSenha : EditText;

    val networkChecker by lazy {
        NetworkChecker(getSystemService(ConnectivityManager::class.java), this);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater);
        val view = binding.root;
        setContentView(view);

        btnCadastrar = binding.btnCadastrese;
        btnEntrar    = binding.btnEntrar;
        tvLoginReult = binding.tvLoginResult;
        etEmail      = binding.etEmail;
        etSenha      = binding.etSenha;

        //Onclick's
        btnCadastrar.setOnClickListener{
            startActivity( Intent(this, ActivityCadastro::class.java) );
            finish();
        }

        btnEntrar.setOnClickListener{
            networkChecker.performActionIfConnected { login() }
        }
    }


    private fun login(){

        val userLogin = FormLogin(etEmail.text.toString(), etSenha.text.toString());
        val http = HttpRequest.requerir();
        http.logar(userLogin).enqueue( object : Callback<Token> {
            override fun onResponse(call: Call<Token>, response: Response<Token>) {
                if (response.code() == 403){
                    tvLoginReult.text = "Email ou senha incorreta";
                } else {
                    var token : Token = response.body()!!;
                    Constants.token = token;
                    buscaDadosLogado(token);
                    intent.putExtra("token", token)
                    tvLoginReult.text = "Usuário logado";
                    irMainActivity();
                }
            }

            override fun onFailure(call: Call<Token>, t: Throwable) {
                Log.e("api", t.message!!)
                Toast.makeText(baseContext, t.message, Toast.LENGTH_LONG).show()
            }
        })

    }

    private fun irMainActivity(){
        startActivity( Intent(this, ActivityMain::class.java) );
        finish();
    }

    private fun buscaDadosLogado(token : Token){
        val http = HttpRequest.requerir();
        http.getLogado("${token.tipo} ${token.token}").enqueue(object : Callback<UserLogado> {
            override fun onResponse(call: Call<UserLogado>, response: Response<UserLogado>) {
                Constants.userLogado = response.body()!!;
            }

            override fun onFailure(call: Call<UserLogado>, t: Throwable) {
                Toast.makeText(baseContext, "onFailure", Toast.LENGTH_LONG).show()
            }
        })

    }


}
