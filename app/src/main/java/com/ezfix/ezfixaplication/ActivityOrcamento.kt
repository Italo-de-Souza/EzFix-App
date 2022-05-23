package com.ezfix.ezfixaplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.ezfix.ezfixaplication.databinding.ActivityOrcamentoBinding
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ezfix.ezfixaplication.configuration.HttpRequest
import com.ezfix.ezfixaplication.data.CardOrcamento
import com.ezfix.ezfixaplication.data.Produtos
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
    private lateinit var btnConcluir  : Button;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOrcamentoBinding.inflate(layoutInflater);
        val view = binding.root;

        setRecyclerView();
        getProdutos();

        rlAddItem  = binding.rlAddItem;
        tvProduto  = binding.tvProduto;
        tvMarca    = binding.tvMarca;
        tvModelo   = binding.tvModelo;
        tvObservacao = binding.tvObservacao;
        btnAdicionar = binding.btnAdicionar;
        btnConcluir  = binding.btnConcluir;

        rlListaItens = binding.rlListaItens;

        val listaProdutos = resources.getStringArray(R.array.produtos)
        val listaMarcas = resources.getStringArray(R.array.marca)
        val listaModelos = resources.getStringArray(R.array.problema)


        val adapterProduto = ArrayAdapter(this, R.layout.list_text, listaProdutos)
        (tvProduto as? AutoCompleteTextView)?.setAdapter(adapterProduto)

        val adapterMarca = ArrayAdapter(this, R.layout.list_text, listaMarcas)
        (tvMarca as? AutoCompleteTextView)?.setAdapter(adapterMarca)

        val adapterModelo = ArrayAdapter(this, R.layout.list_text, listaModelos)
        (tvModelo as? AutoCompleteTextView)?.setAdapter(adapterModelo)


        btnAdicionar.setOnClickListener {
            if (verificaCampos()){
                adicionarItem();
                rlAddItem.visibility    = View.GONE;
                rlListaItens.visibility = View.VISIBLE;
            }
        }

        btnConcluir.setOnClickListener {
            limpaCampos()
            rlAddItem.visibility    = View.VISIBLE;
            rlListaItens.visibility = View.GONE;
        }


        setContentView(view);
    }

    fun setRecyclerView(){
        recyclerView = binding.recyclerView;
        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager;
        adapter = OrcamentoAdapter();
        recyclerView.adapter = adapter;
    }

    fun adicionarItem(){

        var produto = "${tvMarca.text} ${tvModelo.text}"
        adapter.addLista(CardOrcamento(
            tvObservacao.text.toString(),
            produto,
            tvObservacao.text.toString()))
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
        };

        return validado;
    }
    
    fun limpaCampos(){
        tvProduto.text.clear();
        tvMarca.text.clear();
        tvModelo.text.clear();
        tvObservacao.text.clear();
    }

    fun getProdutos(){
        val http = HttpRequest.requerir();
        http.getProdutos().enqueue(object : Callback<ArrayList<Produtos>>{
            override fun onResponse(
                call: Call<ArrayList<Produtos>>,
                response: Response<ArrayList<Produtos>>
            ) {
                var produtos = response.body();
//                setOptions(produtos!!);
                Toast.makeText(baseContext, "sucesso", Toast.LENGTH_LONG).show();
            }

            override fun onFailure(call: Call<ArrayList<Produtos>>, t: Throwable) {
                Toast.makeText(baseContext, "${t.message}", Toast.LENGTH_LONG).show();
            }
        })
    }

//    fun setOptions(produtos : ArrayList<Produtos>){
//        val listaProdutos = resources.getStringArray(R.array.produtos)
//        val listaMarcas   = resources.getStringArray(R.array.marca)
//        val listaModelos  = resources.getStringArray(R.array.problema)
//        produtos.ma
//        produtos.forEach {
//            it.
//        }
//    }
}