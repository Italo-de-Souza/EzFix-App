package com.ezfix.ezfixaplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ezfix.ezfixaplication.data.CardOrcamento
import com.ezfix.ezfixaplication.data.Orcamento
import java.util.ArrayList

class OrcamentoAdapter() : RecyclerView.Adapter<OrcamentoAdapter.ViewHolder>() {

    private var cardOrcamento = ArrayList<CardOrcamento>()
    private var listaOrcamento = ArrayList<Orcamento>()

    fun addLista(item : CardOrcamento, itemOrc : Orcamento){
        cardOrcamento.add(item);
        listaOrcamento.add(itemOrc)
        notifyDataSetChanged();
    }

    fun getListaOrcamento() : ArrayList<Orcamento> { return listaOrcamento }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_orcamento, parent, false);
        return ViewHolder(view);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val card = cardOrcamento[position]
        holder.problema.text = card.problema;
        holder.aparelho.text = card.produto;
        holder.observacao.text = card.observacao;

        holder.icDelete.setOnClickListener {
            cardOrcamento.removeAt(position)
            listaOrcamento.removeAt(position)
            notifyDataSetChanged();
        }
    }

    override fun getItemCount() = cardOrcamento.size;

    class ViewHolder(cardView : View) : RecyclerView.ViewHolder(cardView){
        val problema   : TextView = cardView.findViewById(R.id.tv_problema);
        val aparelho   : TextView = cardView.findViewById(R.id.tv_aparelho);
        val observacao : TextView = cardView.findViewById(R.id.tv_observacao);
        val icDelete   : ImageView = cardView.findViewById(R.id.ic_delete);
    }


}
