package com.ezfix.ezfixaplication

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import com.ezfix.ezfixaplication.configuration.Constants
import com.ezfix.ezfixaplication.configuration.HttpRequest
import com.ezfix.ezfixaplication.data.Comentario
import com.ezfix.ezfixaplication.data.Id
import com.ezfix.ezfixaplication.data.StatusPedido
import com.ezfix.ezfixaplication.databinding.RateDialogAlertBinding
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RateDialogAlert(context : Context, assistencia : Long, orcamento : Long) : Dialog(context) {

    private lateinit var binding : RateDialogAlertBinding;
    val http = HttpRequest.requerir();
    private var assistencia = assistencia;
    private var orcamento   = orcamento;
    private var avaliacao : Float = 0F;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RateDialogAlertBinding.inflate(layoutInflater);
        val view = binding.root;
        setContentView(view)

        avaliacao = binding.rateBar.rating;
        binding.btnEnviar.setOnClickListener { enviarComentario() }


    }

    fun enviarComentario(){
        val comentario = Comentario(binding.etComentario.text.toString(),
                                    binding.rateBar.rating.toDouble(),
                                    Id(assistencia),
                                    Id(orcamento)
        )
        http.enviarComentario("Bearer ${Constants.token.token}", comentario)
            .enqueue(object : Callback<ResponseBody>{
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) atualizaStatus();
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(context, t.message, Toast.LENGTH_LONG).show();
                }
            })
    }

    fun atualizaStatus(){
        val status = StatusPedido(context.getString(R.string.concluido))
        http.atualizaStatus("Bearer ${Constants.token.token}",
            orcamento, status).enqueue(object : Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "Comentário enviado", Toast.LENGTH_LONG).show();
                    dismiss();
                };
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show();
            }
        })
    }

}