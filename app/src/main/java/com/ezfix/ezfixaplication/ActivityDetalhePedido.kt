package com.ezfix.ezfixaplication

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.ezfix.ezfixaplication.configuration.Constants
import com.ezfix.ezfixaplication.configuration.HttpRequest
import com.ezfix.ezfixaplication.data.PedidoDetalhe
import com.ezfix.ezfixaplication.databinding.ActivityDetalhePedidoBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityDetalhePedido : AppCompatActivity() {

    lateinit var binding : ActivityDetalhePedidoBinding;
    private var idAssistencia : Long = 0;
    private var idOrcamento   : Long = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalhePedidoBinding.inflate(layoutInflater);
        val view = binding.root;
        setContentView(view)

        setSupportActionBar(binding.toolbar);
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        idAssistencia = intent.getLongExtra("idAssistencia", 0);
        idOrcamento   = intent.getLongExtra("idOrcamento", 0);

        binding.btnConfirmar.setOnClickListener {
            val intent = Intent(baseContext, ActivityConfirmaPedido::class.java);
            intent.putExtra("idAssistencia", idAssistencia);
            intent.putExtra("idOrcamento", idOrcamento);
            startActivity(intent);
            finishAfterTransition();
        }


        buscaDetalhes();

    }


    fun buscaDetalhes(){
        val http = HttpRequest.requerir();
        http.buscarDetalhePedido(idOrcamento, "Bearer ${Constants.token.token}").enqueue(object : Callback<PedidoDetalhe>{
            override fun onResponse(call: Call<PedidoDetalhe>, response: Response<PedidoDetalhe>) {
                if (response.isSuccessful) atribuirTela(response.body()!!);
            }

            override fun onFailure(call: Call<PedidoDetalhe>, t: Throwable) {
                Toast.makeText(baseContext, t.message, Toast.LENGTH_LONG).show();
            }
        })
    }

    fun atribuirTela(detalhes : PedidoDetalhe){
        binding.tvDataPedido.text = detalhes.dataSolicitacao.split("T").toString();
        binding.tvStatusPedido.text = detalhes.status;
        binding.tvTotal.text = detalhes.valorTotal.toString();
//        binding.tvIdPedido.text = "#${detalhes.idOrcamento}";

        binding.toolbar.title = "${getString(R.string.id_pedido)} #${detalhes.idOrcamento}"

        if (detalhes.status != getString(R.string.aguardando_tecnico)){
            binding.llButtons.visibility = View.VISIBLE;
        }

        detalhes.itemOrcamentoList.forEach { item ->
            val textItem  = TextView(baseContext);
            val textDescr = TextView(baseContext);

            textItem.typeface = Typeface.DEFAULT_BOLD;
            textItem.text = "${item.marca} | ${item.modelo}";
            textItem.setTextColor(getColor(R.color.black));
            textItem.textSize = 16F

            textDescr.text = item.descricao;
            textDescr.setTextColor(getColor(R.color.gray));
            binding.llListaItensDetalhes.addView(textItem);
            binding.llListaItensDetalhes.addView(textDescr);
        }
    }

}