package com.ezfix.ezfixaplication

import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.ezfix.ezfixaplication.cadastro.ActivityCadastro
import com.ezfix.ezfixaplication.configuration.NetworkChecker


class ActivityInicial : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicial)

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

}