package com.ezfix.ezfixaplication.mainscreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ezfix.ezfixaplication.configuration.Constants
import com.ezfix.ezfixaplication.configuration.HttpRequest
import com.ezfix.ezfixaplication.data.CardPedido
import com.ezfix.ezfixaplication.databinding.FragmentMainPedidosBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class FragmentMainPedidos : Fragment() {

//class FragmentMainPedidos : Fragment() {

    private lateinit var binding : FragmentMainPedidosBinding;
    private lateinit var recyclerView: RecyclerView;
    private lateinit var adapter : PedidosAdapter;
    private lateinit var layoutManager: LinearLayoutManager;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMainPedidosBinding.inflate(layoutInflater);
        val view = binding.root;

        recyclerView = binding.recyclerView;

        setRecycleView();
        getPedidos()

        return view;
    }

    fun getPedidos(){
        val http = HttpRequest.requerir();
        val token = Constants.token.token
        http.getPedidos("Bearer $token").enqueue(object : Callback<ArrayList<CardPedido>>{
            override fun onResponse(
                call: Call<ArrayList<CardPedido>>,
                response: Response<ArrayList<CardPedido>>
            ) {
                if (response.isSuccessful){
                    var listaPedidos = response.body();
                    adapter.addLista(listaPedidos!!)
                }
            }

            override fun onFailure(call: Call<ArrayList<CardPedido>>, t: Throwable) {
                Toast.makeText(context, "${t.message}", Toast.LENGTH_LONG).show();
            }
        })
    }

    fun setRecycleView(){
        layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager;
        adapter = PedidosAdapter(context);
        recyclerView.adapter = adapter;
    }

//    override fun onItemClick(card: CardPedido) {
//        if (card.isBtnClicked()){
//            Toast.makeText(context, "${card.idOrcamento}", Toast.LENGTH_LONG).show();
//        }
//
//    }

}