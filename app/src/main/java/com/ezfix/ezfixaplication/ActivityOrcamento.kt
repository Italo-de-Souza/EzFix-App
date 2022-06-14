package com.ezfix.ezfixaplication

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.ezfix.ezfixaplication.databinding.ActivityOrcamentoBinding
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ezfix.ezfixaplication.adapters.OrcamentoAdapter
import com.ezfix.ezfixaplication.configuration.Constants
import com.ezfix.ezfixaplication.configuration.HttpRequest
import com.ezfix.ezfixaplication.data.CardOrcamento
import com.ezfix.ezfixaplication.data.IdProduto
import com.ezfix.ezfixaplication.data.InfoProduto
import com.ezfix.ezfixaplication.data.Orcamento
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList


class ActivityOrcamento : AppCompatActivity() {

    private lateinit var binding : ActivityOrcamentoBinding;
    private lateinit var layoutManager: LinearLayoutManager;
    private lateinit var adapter : OrcamentoAdapter;
//    TELA ADD ITEM
    private lateinit var rlAddItem    : RelativeLayout;
    private lateinit var tvProduto    : EditText;
    private lateinit var tvMarca      : EditText;
    private lateinit var tvModelo     : EditText;
    private lateinit var tvObservacao : EditText;
    private lateinit var btnAdicionar : Button;
//    TELA DE LISTA
    private lateinit var rlListaItens : RelativeLayout;
    private lateinit var recyclerView : RecyclerView;
    private lateinit var icAdd        : ImageView;
    private lateinit var btnConcluir  : Button;
    private var voltar = false;

    private var produto : HashMap<String, Long> = HashMap<String, Long>();
    private var marca : HashMap<String, Long> = HashMap<String, Long>();
    private var modelo : HashMap<String, Long> = HashMap<String, Long>();

    private var idTipo    : Long = 0;
    private var idMarca   : Long = 0;
    private var idModelo  : Long = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrcamentoBinding.inflate(layoutInflater);
        val view = binding.root;
        setContentView(view);

        setSupportActionBar(binding.toolbar);
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setRecyclerView();

        rlAddItem  = binding.rlAddItem;
        tvProduto  = binding.tvProduto;
        tvMarca    = binding.tvMarca;
        tvModelo   = binding.tvModelo;
        tvObservacao = binding.tvObservacao;
        btnAdicionar = binding.btnAdicionar;
        btnConcluir  = binding.btnConcluir;
        icAdd        = binding.icAdd;

        rlListaItens = binding.rlListaItens;


//        ONCLIKCS
        btnAdicionar.setOnClickListener {
            if (verificaCampos()){
                adicionarItem();
                rlAddItem.visibility    = View.GONE;
                rlListaItens.visibility = View.VISIBLE;
            }
        }

        icAdd.setOnClickListener {
            limpaCampos();
            rlAddItem.visibility    = View.VISIBLE;
            rlListaItens.visibility = View.GONE;
        }

        btnConcluir.setOnClickListener { enviaOrcamento() }

        getProdutos();
        tvProduto.doAfterTextChanged {
            if (!voltar) idTipo = produto[tvProduto.text.toString()]!!; getMarcas(idTipo) }
        tvMarca.doAfterTextChanged {
            if (!voltar)idMarca = marca[tvMarca.text.toString()]!!; getModelos(idTipo, idMarca) }
        tvModelo.doAfterTextChanged {
            if (!voltar)idModelo = modelo[tvModelo.text.toString()]!!; }


    }

    fun limpaCampos(){
        voltar = true;
        tvProduto.text.clear();
        tvMarca.text.clear();
        tvModelo.text.clear();
        tvObservacao.text.clear();
        voltar = false;
    }

    private fun enviaOrcamento() {
        var idAssistencia = intent.getLongExtra("idAssistencia", 0)
        val http = HttpRequest.requerir();
        http.sendOrcamento(adapter.getListaOrcamento(),
                           "${Constants.token.tipo} ${Constants.token.token}",
                            idAssistencia).enqueue(object : Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) showAlert();
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(baseContext, t.message, Toast.LENGTH_LONG).show()
            }
        });
    }

    //    FUNCTIONS
    fun setRecyclerView(){
        recyclerView = binding.recyclerView;
        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager;
        adapter = OrcamentoAdapter();
        recyclerView.adapter = adapter;
    }

    fun adicionarItem(){
        var produto = "${tvMarca.text} ${tvModelo.text}".replace("[0-9]".toRegex(), "")
        var orcamento = Orcamento(tvObservacao.text.toString(), IdProduto(idModelo))
        var card = CardOrcamento(tvProduto.text.toString(), produto, tvObservacao.text.toString())
        adapter.addLista(card, orcamento);
    }

    fun verificaCampos():Boolean{
        var validado = true;
        when {
            tvProduto.text.isBlank() -> {
                tvProduto.error = getString(R.string.erro_campo)
                validado = false
            }
            tvMarca.text.isBlank() -> {
                tvMarca.error = getString(R.string.erro_campo)
                validado = false
            }
            tvModelo.text.isBlank() -> {
                tvModelo.error = getString(R.string.erro_campo)
                validado = false
            }
            tvObservacao.text.isBlank() -> {
                tvModelo.error = getString(R.string.erro_campo)
                validado = false
            }
        };

        return validado;
    }


    fun getProdutos(){
        val http = HttpRequest.requerir();
        http.getProdTipos().enqueue(object : Callback<ArrayList<InfoProduto>>{
            override fun onResponse(
                call: Call<ArrayList<InfoProduto>>,
                response: Response<ArrayList<InfoProduto>>
            ) {
                if (response.isSuccessful) setOptionTipos(response.body());
            }

            override fun onFailure(call: Call<ArrayList<InfoProduto>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    fun getMarcas(idProduto : Long){
        val http = HttpRequest.requerir();
        http.getProdMarcas(idProduto).enqueue(object : Callback<ArrayList<InfoProduto>>{
            override fun onResponse(
                call: Call<ArrayList<InfoProduto>>,
                response: Response<ArrayList<InfoProduto>>
            ) {
                if (response.isSuccessful) setOptionMarcas(response.body());
            }

            override fun onFailure(call: Call<ArrayList<InfoProduto>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    fun getModelos(idTipo : Long, idMarca : Long){
        val http = HttpRequest.requerir();
        http.getProdModelos(idTipo, idMarca).enqueue(object : Callback<ArrayList<InfoProduto>>{
            override fun onResponse(
                call: Call<ArrayList<InfoProduto>>,
                response: Response<ArrayList<InfoProduto>>
            ) {
                if (response.isSuccessful) setOptionModelos(response.body());
            }

            override fun onFailure(call: Call<ArrayList<InfoProduto>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    fun setOptionTipos(listaTipos: ArrayList<InfoProduto>?){
        var listaMut = mutableListOf<String>()
        listaTipos?.forEach { listaMut.add(it.nome); produto.set(it.nome, it.id) }

        val adapterProduto = ArrayAdapter(this, R.layout.list_text, listaMut)
        (tvProduto as? AutoCompleteTextView)?.setAdapter(adapterProduto)
    }

    fun setOptionMarcas(listaMarcas: ArrayList<InfoProduto>?){
        var listaMut = mutableListOf<String>()
        listaMarcas?.forEach { listaMut.add(it.nome); marca.set(it.nome, it.id) }


        val adapterProduto = ArrayAdapter(this, R.layout.list_text, listaMut)
        (tvMarca as? AutoCompleteTextView)?.setAdapter(adapterProduto)
    }

    fun setOptionModelos(listaModelos: ArrayList<InfoProduto>?){
        var listaMut = mutableListOf<String>()
        listaModelos?.forEach { listaMut.add(it.nome); modelo.set(it.nome, it.id) }

        val adapterProduto = ArrayAdapter(this, R.layout.list_text, listaMut)
        (tvModelo as? AutoCompleteTextView)?.setAdapter(adapterProduto)
    }

    fun showAlert(){
        val alert = AlertDialog.Builder(this)
        alert.setTitle("Enviado!")
            .setMessage("Orçamento enviado com sucesso!")
            .setCancelable(false)
            .setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i -> this.finish() } )
            .create().show();
    }
}