package com.ezfix.ezfixaplication.mainscreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ezfix.ezfixaplication.R
import com.ezfix.ezfixaplication.data.CardAssist
import com.ezfix.ezfixaplication.databinding.ItemCertificadoBinding
import com.ezfix.ezfixaplication.model.Certificado
import java.util.ArrayList

class CertificadoAdapter(private val onItemClickListener: CertificadoAdapter.OnItemClickListener)
    : RecyclerView.Adapter<CertificadoAdapter.ViewHolder>(){

    private var cardCertificado = ArrayList<Certificado>();

    fun addLista(itens : ArrayList<Certificado>){
        cardCertificado.addAll(itens);
        notifyDataSetChanged();
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
        R.layout.item_certificado, parent, false);
        val viewHolder = CertificadoAdapter.ViewHolder(view);

        viewHolder.itemView.setOnClickListener {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(cardCertificado[viewHolder.adapterPosition]);
            }
        }
        return viewHolder;
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val card = cardCertificado[position];

        holder.nomeCurso.text     = card.nomeCurso;
        holder.dataInicio.text    = card.dataInicio;
        holder.dataConclusao.text = card.dataConclusao;
        holder.cargaHoraria.text  = card.quantidadeHoras.toString();

        var isVisible = card.isVisible();
        if (isVisible){
            holder.infoCurso.visibility = View.VISIBLE
            holder.icArrow.rotation = 0.0.toFloat();
        }
        else {
            holder.infoCurso.visibility = View.GONE
            holder.icArrow.rotation = (-90.0).toFloat();
        }

    }

    override fun getItemCount() = cardCertificado.size

    class ViewHolder (cardView : View) : RecyclerView.ViewHolder(cardView) {
        val nomeCurso      : TextView = cardView.findViewById(R.id.tv_nome_curso);
        val dataInicio     : TextView = cardView.findViewById(R.id.tv_data_inicio);
        val dataConclusao  : TextView = cardView.findViewById(R.id.tv_data_fim);
        val cargaHoraria   : TextView = cardView.findViewById(R.id.tv_carga_horaria);
        val infoCurso      : RelativeLayout = cardView.findViewById(R.id.rl_info_curso);
        val icArrow        : ImageView      = cardView.findViewById(R.id.ic_arrow)

    }

    interface OnItemClickListener {
        fun onItemClick(card: Certificado);
    }

}
