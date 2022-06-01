package com.ezfix.ezfixaplication

import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.ezfix.ezfixaplication.cadastro.ActivityCadastro
import com.ezfix.ezfixaplication.configuration.Constants
import com.ezfix.ezfixaplication.configuration.HttpRequest
import com.ezfix.ezfixaplication.configuration.NetworkChecker
import com.ezfix.ezfixaplication.data.Token
import com.ezfix.ezfixaplication.data.UserLogado
import com.ezfix.ezfixaplication.mainscreen.ActivityMain
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ActivityInicial : AppCompatActivity(){

    private lateinit var preferencias: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicial)

        preferencias = getSharedPreferences("token", MODE_PRIVATE)
        val tokenOff = preferencias.getString("token", null);
        if (tokenOff != null) buscaDadosLogado(tokenOff);

        val btnCadastrar : Button = findViewById(R.id.btn_cadastrar);
        val btnEntrar : Button = findViewById(R.id.btn_entrar_login);

        btnCadastrar.setOnClickListener{
            startActivity( Intent(this, ActivityCadastro::class.java) );
            finish();
        }

        btnEntrar.setOnClickListener{
            startActivity( Intent(this, ActivityLogin::class.java) );
            finish();
        }

    }

    private fun buscaDadosLogado(token : String){
        val http = HttpRequest.requerir();
        http.getLogado("Bearer $token").enqueue(object : Callback<UserLogado> {
            override fun onResponse(call: Call<UserLogado>, response: Response<UserLogado>) {
                if (response.isSuccessful){
                    Constants.userLogado = response.body()!!;
                    Constants.token = Token("Bearer", token);
                    irTelaHome();
                }
            }

            override fun onFailure(call: Call<UserLogado>, t: Throwable) {
                Toast.makeText(baseContext, "onFailure", Toast.LENGTH_LONG).show()
            }
        })

    }

    fun irTelaHome(){
        startActivity(Intent(this, ActivityMain::class.java));
    }

}