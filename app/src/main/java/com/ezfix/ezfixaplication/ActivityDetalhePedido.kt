package com.ezfix.ezfixaplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ezfix.ezfixaplication.databinding.ActivityDetalhePedidoBinding

class ActivityDetalhePedido : AppCompatActivity() {

    lateinit var binding : ActivityDetalhePedidoBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalhePedidoBinding.inflate(layoutInflater);
        val view = binding.root;

        val idAssistencia = intent.getLongExtra("idAssistencia", 0);
        val idOrcamento   = intent.getLongExtra("idOrcamento", 0);



        setContentView(view)
    }
}