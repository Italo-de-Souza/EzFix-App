package com.ezfix.ezfixaplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ezfix.ezfixaplication.data.CardOrcamento
import java.util.ArrayList

class OrcamentoAdapter() : RecyclerView.Adapter<OrcamentoAdapter.ViewHolder>() {

    private var cardOrcamento = ArrayList<CardOrcamento>()

    fun addLista(item : CardOrcamento){
        cardOrcamento.add(item);
        notifyDataSetChanged();
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_orcamento, parent, false);
        return ViewHolder(view);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val card = cardOrcamento[position]

        holder.problema.text = card.problema;
        holder.aparelho.text = card.produto;
        holder.observacao.text = card.observacao;
    }

    override fun getItemCount() = cardOrcamento.size;

    class ViewHolder(cardView : View) : RecyclerView.ViewHolder(cardView){
        val problema   : TextView = cardView.findViewById(R.id.tv_problema);
        val aparelho   : TextView = cardView.findViewById(R.id.tv_aparelho);
        val observacao : TextView = cardView.findViewById(R.id.tv_observacao);
    }


}
