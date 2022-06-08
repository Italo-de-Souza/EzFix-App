package com.ezfix.ezfixaplication.mainscreen

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.d
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ezfix.ezfixaplication.ActivityOrcamento
import com.ezfix.ezfixaplication.R
import com.ezfix.ezfixaplication.configuration.HttpRequest
import com.ezfix.ezfixaplication.databinding.ActivityPerfilAssistenciaBinding
import com.ezfix.ezfixaplication.model.Certificado
import com.ezfix.ezfixaplication.model.PerfilAssistencia
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityPerfilAssistencia : AppCompatActivity(), CertificadoAdapter.OnItemClickListener {

    private lateinit var binding : ActivityPerfilAssistenciaBinding;
    private lateinit var tvAvaliacao : TextView;
    private lateinit var tvNomeFantasia : TextView;
    private lateinit var ivImgAssistencia : ImageView;
    private lateinit var btnOrcamento : Button;
    private lateinit var recyclerView: RecyclerView;

    private lateinit var adapter : CertificadoAdapter;
    private lateinit var layoutManager: LinearLayoutManager;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerfilAssistenciaBinding.inflate(layoutInflater);
        val view = binding.root;
        setContentView(view)

        setSupportActionBar(binding.toolbar);
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        var id = intent.getLongExtra("id", 0);

        recyclerView = binding.recyclerView;

        tvAvaliacao = binding.tvRateStar;
        tvNomeFantasia = binding.tvNomeAssistencia;
        ivImgAssistencia = binding.ivImgAssistencia;
        btnOrcamento = binding.btnOcarmento;

        btnOrcamento.setOnClickListener {
            var intent = Intent(this, ActivityOrcamento::class.java)
            intent.putExtra("idAssistencia", id.toLong());
            startActivity(intent)
        }

        setRecycleView();
        buscaDados(id);
    }

    fun preencheCampos(perfilAssistencia: PerfilAssistencia){
        tvAvaliacao.text = perfilAssistencia.avaliacao.toString();
        tvNomeFantasia.text = perfilAssistencia.nomeFantasia;
    }

    fun buscaDados(id:Long){

        var http = HttpRequest.requerir()
        http.getAssistencia(id).enqueue(object : Callback<PerfilAssistencia> {
            override fun onResponse(
                call: Call<PerfilAssistencia>,
                response: Response<PerfilAssistencia>
            ) {
                if (response.isSuccessful){
                    d("api", "OnResponse")
                    var perfilAssistencia: PerfilAssistencia = response.body()!!;
                    preencheCampos(perfilAssistencia);
                    if (perfilAssistencia.certificados !== null){
                        adapter.addLista(perfilAssistencia.certificados);
                    }
                }
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

    fun setRecycleView(){
        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager;
        adapter = CertificadoAdapter(this)
        recyclerView.adapter = adapter;
    }

    override fun onItemClick(card: Certificado) {
        card.alternaInfo(!card.isVisible());
        adapter.notifyDataSetChanged();
    }


}