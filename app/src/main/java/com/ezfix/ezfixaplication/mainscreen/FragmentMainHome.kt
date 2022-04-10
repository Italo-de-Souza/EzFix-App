package com.ezfix.ezfixaplication.mainscreen

import android.os.Bundle
import android.util.Log
import android.util.Log.d
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ezfix.ezfixaplication.configuration.HttpRequest
import com.ezfix.ezfixaplication.data.CardAssistencia
import com.ezfix.ezfixaplication.databinding.FragmentMainHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentMainHome : Fragment() {

    private lateinit var binding : FragmentMainHomeBinding;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMainHomeBinding.inflate(layoutInflater);
        val view = binding.root;

        val recyclerView = binding.recyclerView;

        val http = HttpRequest.requerir();

        http.getCardsAssistencias().enqueue(object : Callback<CardAssistencia>{
            override fun onResponse(
                call: Call<CardAssistencia>,
                response: Response<CardAssistencia>
            ) {
                var cardAssistencia = response.body();

                recyclerView.apply {
                    adapter = AssistenciaAdapter(cardAssistencia!!.content)
                }
            }

            override fun onFailure(call: Call<CardAssistencia>, t: Throwable) {
                Log.e("api", t.message!!)
            }
        })

        return view;
    }

}