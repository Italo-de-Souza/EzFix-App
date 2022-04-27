package com.ezfix.ezfixaplication.mainscreen

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.d
import android.widget.ImageView
import android.widget.TextView
import com.ezfix.ezfixaplication.configuration.HttpRequest
import com.ezfix.ezfixaplication.databinding.ActivityPerfilAssistenciaBinding
import com.ezfix.ezfixaplication.model.PerfilAssistencia
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityPerfilAssistencia : AppCompatActivity() {

    private lateinit var binding : ActivityPerfilAssistenciaBinding;
    private lateinit var tvAvaliacao : TextView;
    private lateinit var tvNomeFantasia : TextView;
    private lateinit var ivImgAssistencia : ImageView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPerfilAssistenciaBinding.inflate(layoutInflater);
        val view = binding.root;

        var id = intent.getIntExtra("id", 0);

        tvAvaliacao = binding.tvRateStar;
        tvNomeFantasia = binding.tvNomeAssistencia;
        ivImgAssistencia = binding.ivImgAssistencia;

        buscaDados(id);

        setContentView(view)
    }

    fun preencheCampos(perfilAssistencia: PerfilAssistencia){
        tvAvaliacao.text = perfilAssistencia.avaliacao.toString();
        tvNomeFantasia.text = perfilAssistencia.nomeFantasia;
    }

    fun buscaDados(id:Int){

        var http = HttpRequest.requerir()
        http.getAssistencia(id).enqueue(object : Callback<PerfilAssistencia> {
            override fun onResponse(
                call: Call<PerfilAssistencia>,
                response: Response<PerfilAssistencia>
            ) {
                d("api", "OnResponse")
                var perfilAssistencia = PerfilAssistencia()
                perfilAssistencia = response.body()!!;
                preencheCampos(perfilAssistencia);
            }

            override fun onFailure(call: Call<PerfilAssistencia>, t: Throwable) {
                d("api", "OnFailure")
            }
        })

        http.getImagem(id).enqueue(object : Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.code() == 200){
                    val bmp = BitmapFactory.decodeStream(response.body()!!.byteStream());
                    ivImgAssistencia.setImageBitmap(bmp);
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                d("api", "erro: ${t.message}")
            }
        })
    }
}