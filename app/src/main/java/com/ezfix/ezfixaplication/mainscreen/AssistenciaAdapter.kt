package com.ezfix.ezfixaplication.mainscreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ezfix.ezfixaplication.R
import com.ezfix.ezfixaplication.configuration.HttpRequest
import com.ezfix.ezfixaplication.data.CardAssist
import com.ezfix.ezfixaplication.data.CardAssistencia
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//class AssistenciaAdapter(private val cardAssistencia: ArrayList<CardAssist>) :

class AssistenciaAdapter :
    RecyclerView.Adapter<AssistenciaAdapter.ViewHolder>() {

    private var cardAssistencia = ArrayList<CardAssist>()

    fun addLista(itens : ArrayList<CardAssist>){
        cardAssistencia.addAll(itens);
        notifyDataSetChanged();
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.assistencia_item, parent, false)
        return ViewHolder(view);
    }


    override fun getItemCount() = cardAssistencia.size;


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val card = cardAssistencia[position];

        val http = HttpRequest.requerir();
        http.getImagem(card.id).enqueue(object : Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                val bmp = BitmapFactory.decodeStream(response.body()!!.byteStream())

                holder.vincula(card, bmp)
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("api", t.message!!)
            }
        })
//        holder.nomeFantasia.text = card.nomeFantasia;
//        holder.cidade.text = card.cidade;
//        holder.estado.text = card.estado;
//        holder.rate.text = card.avaliacao.toString();

    }

    class ViewHolder (cardView : View) : RecyclerView.ViewHolder(cardView) {
        val nomeFantasia    : TextView  = cardView.findViewById(R.id.tv_nome_assistencia);
        val cidade          : TextView  = cardView.findViewById(R.id.tv_cidade_assistencia);
        val estado          : TextView  = cardView.findViewById(R.id.tv_estado_assistencia);
        val rate            : TextView  = cardView.findViewById(R.id.tv_rate_star);
        val image           : ImageView = cardView.findViewById(R.id.iv_img_assistencia);

        fun vincula(card : CardAssist, bmp : Bitmap){
            nomeFantasia.text = card.nomeFantasia;
            cidade.text = card.cidade;
            estado.text = card.estado;
            rate.text = card.avaliacao.toString();
            image.setImageBitmap(bmp);
        }
    }

}
