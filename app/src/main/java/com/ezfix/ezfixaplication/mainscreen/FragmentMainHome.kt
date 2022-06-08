package com.ezfix.ezfixaplication.mainscreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ezfix.ezfixaplication.configuration.HttpRequest
import com.ezfix.ezfixaplication.data.CardAssist
import com.ezfix.ezfixaplication.data.CardAssistencia
import com.ezfix.ezfixaplication.databinding.FragmentMainHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentMainHome : Fragment(), AssistenciaAdapter.OnItemClickListener {

    private lateinit var binding        : FragmentMainHomeBinding;
    private lateinit var recyclerView   : RecyclerView;
    private lateinit var adapter        : AssistenciaAdapter;
    private lateinit var layoutManager  : LinearLayoutManager;
    private lateinit var cardAssistencia: CardAssistencia;
    private lateinit var refresh        : SwipeRefreshLayout;
    private var page = 0;
    private var totalPages = 0;
    private var isScrolling = false;

    override fun onAttach(context: Context) {
        super.onAttach(context)
        page = 0;
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMainHomeBinding.inflate(layoutInflater);
        val view = binding.root;

        refresh = binding.swipeRefresh;
        recyclerView = binding.recyclerView;

        refresh.setOnRefreshListener {
            page = 0;
            adapter.limpaLista();
            buscarAssistencias();
        }

        setRecycleView()
        buscarAssistencias();

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling = true;
                }
            }
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                var itensVisiveis   = layoutManager.childCount;
                var itensTotais     = layoutManager.itemCount;
                var itensInvisiveis = layoutManager.findFirstVisibleItemPosition();

                if (page < totalPages){
                    if (isScrolling && (itensVisiveis + itensInvisiveis == itensTotais)){
                        isScrolling = false;
                        buscarAssistencias();
                    }
                }
            }
        })

        return view;
    }


    fun buscarAssistencias(){

        refresh.isRefreshing = true;
        val http = HttpRequest.requerir();
        http.getCardsAssistencias(page).enqueue(object : Callback<CardAssistencia>{
            override fun onResponse(
                call: Call<CardAssistencia>,
                response: Response<CardAssistencia>
            ) {
                if (response.body() != null){
                    if (page == 0){
                        cardAssistencia = response?.body()!!;
                        totalPages      = response?.body()!!.totalPages;
                    } else {
                        cardAssistencia.content = response?.body()!!.content;
                    }
                    adapter.addLista(cardAssistencia.content)
                    page++;
                }
                refresh.isRefreshing = false;
            }

            override fun onFailure(call: Call<CardAssistencia>, t: Throwable) {
                Log.e("api", t.message!!)
                refresh.isRefreshing = false;
            }
        })
    }

    fun setRecycleView(){
        layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager;
        adapter = AssistenciaAdapter(this)
        recyclerView.adapter = adapter;
    }

    override fun onItemClick(card: CardAssist) {
        Toast.makeText(context, "clicado: ${card.nomeFantasia}", Toast.LENGTH_LONG).show();
        var intent = Intent(context, ActivityPerfilAssistencia::class.java);
        intent.putExtra("id", card.id)
        startActivity(intent)
    }

}