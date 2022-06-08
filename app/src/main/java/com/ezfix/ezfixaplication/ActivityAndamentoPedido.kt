package com.ezfix.ezfixaplication

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.ezfix.ezfixaplication.configuration.Constants
import com.ezfix.ezfixaplication.configuration.HttpRequest
import com.ezfix.ezfixaplication.data.StatusPedido
import com.ezfix.ezfixaplication.databinding.ActivityAndamentoPedidoBinding
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class ActivityAndamentoPedido : AppCompatActivity() {

    private lateinit var binding : ActivityAndamentoPedidoBinding;
    private lateinit var statusPedido : String;
    private var idAssistencia : Long = 0;
    private var idOrcamento   : Long = 0;
    private var map = HashMap<String, Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAndamentoPedidoBinding.inflate(layoutInflater);
        val view = binding.root;
        setContentView(view);


        statusPedido  = intent.getStringExtra("statusPedido")!!;
        idAssistencia = intent.getLongExtra("idAssistencia", 0);
        idOrcamento   = intent.getLongExtra("idOrcamento", 0);


        binding.btnConfirmar.setOnClickListener { atualizaStatus() }
        binding.btnAvaliacao.setOnClickListener { alertAvaliacao() }


        binding.toolbar.title = "${getString(R.string.id_pedido)} #$idOrcamento"
        setSupportActionBar(binding.toolbar);
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setHashMap()
        checkStatus(statusPedido)


    }

    fun atualizaStatus(){
        val http = HttpRequest.requerir();
        val status = StatusPedido(getString(R.string.aguardando_avaliacao))
        http.atualizaStatus("Bearer ${Constants.token.token}",
                            idOrcamento, status).enqueue(object : Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) checkStatus(status.status);
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(baseContext, t.message, Toast.LENGTH_LONG).show();
            }
        })
    }

    fun checkStatus(status : String){

        var statusAtual = map[status]

        if (statusAtual!! >= 2) {
            binding.circleStep1.setColorFilter(getColor(R.color.dark_blue))
            binding.vlineStep1.setBackgroundColor(getColor(R.color.dark_blue))
        }

        if (statusAtual!! >=3) {
            binding.circleStep2.setColorFilter(getColor(R.color.dark_blue))
            binding.vlineStep2.setBackgroundColor(getColor(R.color.dark_blue))
            binding.btnConfirmar.visibility = View.VISIBLE
        }

        if (statusAtual!! >= 4) {
            binding.circleStep3.setColorFilter(getColor(R.color.dark_blue))
            binding.vlineStep3.setBackgroundColor(getColor(R.color.dark_blue))
            binding.btnConfirmar.visibility = View.GONE;
            binding.btnAvaliacao.visibility = View.VISIBLE;
        }

        if (statusAtual!! == 5) {
            binding.circleStep4.setColorFilter(getColor(R.color.dark_blue))
            binding.btnAvaliacao.visibility = View.GONE;
        }

    }

    fun alertAvaliacao(){
        var dialog = RateDialogAlert(this, idAssistencia, idOrcamento);
        dialog.window?.setBackgroundDrawable(ColorDrawable(getColor(android.R.color.transparent)))
        dialog.setCancelable(false)
        dialog.show();
        dialog.setOnDismissListener { finish() }
    }

    fun setHashMap(){
        map[getString(R.string.aguardando_envio)] = 1;
        map[getString(R.string.reparo_andamento)] = 2;
        map[getString(R.string.reparo_concluido)] = 3;
        map[getString(R.string.aguardando_avaliacao)] = 4;
        map[getString(R.string.concluido)] = 5;
    }
}