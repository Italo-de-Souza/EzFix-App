package com.ezfix.ezfixaplication.mainscreen

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ezfix.ezfixaplication.ActivityDetalhePedido
import com.ezfix.ezfixaplication.ActivityLogin
import com.ezfix.ezfixaplication.R
import com.ezfix.ezfixaplication.configuration.HttpRequest
import com.ezfix.ezfixaplication.data.CardPedido
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PedidosAdapter(private val context: Context?) :
    RecyclerView.Adapter<PedidosAdapter.ViewHolder>() {

    private var cardPedido = ArrayList<CardPedido>();


    fun addLista(itens : ArrayList<CardPedido>){
        cardPedido.addAll(itens);
        notifyDataSetChanged();
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pedido, parent, false);
        val viewHolder = ViewHolder(view);

//        viewHolder.itemView.setOnClickListener {
//            if (onItemClickListener != null) {
//                onItemClickListener.onItemClick(cardPedido[viewHolder.adapterPosition]);
//            }
//        }
        return viewHolder;
    }


    override fun getItemCount() = cardPedido.size;


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val card = cardPedido[position];

        holder.btnDetalhes.setOnClickListener {
            var intent = Intent(context, ActivityDetalhePedido::class.java);
            intent.putExtra("idAssistencia", card.idAssistencia);
            intent.putExtra("idOrcamento", card.idOrcamento);
            context!!.startActivity(intent);
        }

        val http = HttpRequest.requerir();
        http.getImagem(card.idAssistencia).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                val bmp = BitmapFactory.decodeStream(response.body()!!.byteStream());

                holder.vincula(card, bmp);
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("api", t.message!!)
            }
        })

    }

    class ViewHolder (cardView : View) : RecyclerView.ViewHolder(cardView){
        val nomeAssistencia : TextView = cardView.findViewById(R.id.tv_nome_assistencia);
        val statusPedido    : TextView = cardView.findViewById(R.id.tv_status_pedido);
        val image           : ImageView = cardView.findViewById(R.id.iv_img_assistencia);
        val btnDetalhes     : Button    = cardView.findViewById(R.id.btn_detalhes);


        fun vincula(card : CardPedido, bmp : Bitmap){
            nomeAssistencia.text = card.nomeAssistencia;
            statusPedido.text = card.status;
            image.setImageBitmap(bmp);
        }
    }

//    interface OnItemClickListener {
//        fun onItemClick(card: CardPedido);
//    }

}
