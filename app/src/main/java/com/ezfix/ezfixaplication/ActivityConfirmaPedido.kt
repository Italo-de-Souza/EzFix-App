package com.ezfix.ezfixaplication

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.ezfix.ezfixaplication.configuration.Constants
import com.ezfix.ezfixaplication.configuration.HttpRequest
import com.ezfix.ezfixaplication.data.PedidoDetalhe
import com.ezfix.ezfixaplication.data.StatusPedido
import com.ezfix.ezfixaplication.databinding.ActivityConfirmaPedidoBinding
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityConfirmaPedido : AppCompatActivity() {

    private lateinit var binding       : ActivityConfirmaPedidoBinding;
    private var idAssistencia : Long = 0;
    private var idOrcamento   : Long = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmaPedidoBinding.inflate(layoutInflater);
        val view = binding.root;
        setContentView(view);

        setSupportActionBar(binding.toolbar);
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        idAssistencia = intent.getLongExtra("idAssistencia", 0);
        idOrcamento   = intent.getLongExtra("idOrcamento", 0);


        binding.btnFinalizar.setOnClickListener {
            if (binding.rbEntregar.isChecked) confirmarOrcamento();
        }

        buscaDetalhes();
    }

    fun confirmarOrcamento(){
        val http = HttpRequest.requerir();
        val status = StatusPedido(getString(R.string.aguardando_envio));

        http.atualizaStatus("Bearer ${Constants.token.token}", idOrcamento, status)
            .enqueue( object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>){
                    if (response.isSuccessful) finish();
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(baseContext, t.message, Toast.LENGTH_LONG).show();
                }
            }
        )
    }

    fun buscaDetalhes(){
        val http = HttpRequest.requerir();
        http.buscarDetalhePedido(idOrcamento, "Bearer ${Constants.token.token}").enqueue(object :
            Callback<PedidoDetalhe> {
            override fun onResponse(call: Call<PedidoDetalhe>, response: Response<PedidoDetalhe>) {
                if (response.isSuccessful) atribuirTela(response.body()!!);
            }

            override fun onFailure(call: Call<PedidoDetalhe>, t: Throwable) {
                Toast.makeText(baseContext, t.message, Toast.LENGTH_LONG).show();
            }
        })
    }


    fun atribuirTela(detalhes : PedidoDetalhe){
        binding.tvTotal.text = detalhes.valorTotal.toString();
        binding.tvResumo.text = "${getString(R.string.resumo)} #${detalhes.idOrcamento}";

        binding.toolbar.title = "${getString(R.string.id_pedido)} #$idOrcamento"

        detalhes.itemOrcamentoList.forEach { item ->
            val textItem  = TextView(baseContext);

            textItem.typeface = Typeface.DEFAULT_BOLD;
            textItem.text = "${item.marca} | ${item.modelo}";
            textItem.setTextColor(getColor(R.color.black));
            textItem.textSize = 16F
            binding.llListaItensDetalhes.addView(textItem);
        }
    }



}