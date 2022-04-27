package com.ezfix.ezfixaplication.mainscreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ezfix.ezfixaplication.data.CardPedido
import com.ezfix.ezfixaplication.databinding.FragmentMainPedidosBinding

//class FragmentMainPedidos : Fragment(), PedidosAdapter.OnItemClickListener {

class FragmentMainPedidos : Fragment() {

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

//        recyclerView = binding.recyclerView;

//        setRecycleView()

        return view;
    }


//    fun setRecycleView(){
//        layoutManager = LinearLayoutManager(context)
//        recyclerView.layoutManager = layoutManager;
//        adapter = PedidosAdapter(this)
//        recyclerView.adapter = adapter;
//    }
//
//    override fun onItemClick(card: CardPedido) {
//        Toast.makeText(context, "clicado", Toast.LENGTH_SHORT).show();
//    }

}