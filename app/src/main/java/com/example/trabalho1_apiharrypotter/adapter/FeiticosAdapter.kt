package com.example.trabalho1_apiharrypotter.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trabalho1_apiharrypotter.R
import com.example.trabalho1_apiharrypotter.model.Feitico

class FeiticosAdapter(
    private var feiticos: List<Feitico>,
    private val clique: (Feitico) -> Unit
) : RecyclerView.Adapter<FeiticosAdapter.FeiticoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeiticoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_feitico, parent, false)
        return FeiticoViewHolder(view, clique)
    }

    override fun onBindViewHolder(holder: FeiticoViewHolder, position: Int) {
        holder.bind(feiticos[position])
    }

    override fun getItemCount(): Int = feiticos.size

    fun atualizarFeiticos(novosFeiticos: List<Feitico>) {
        feiticos = novosFeiticos
        notifyDataSetChanged()
    }

    class FeiticoViewHolder(
        itemView: View,
        private val clique: (Feitico) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val textoNomeFeitico: TextView = itemView.findViewById(R.id.textoNomeFeitico)
        private val textoDescricaoFeitico: TextView = itemView.findViewById(R.id.textoDescricaoFeitico)

        fun bind(feitico: Feitico) {
            val contexto = itemView.context
            textoNomeFeitico.text = feitico.nome
                ?.takeIf { it.isNotBlank() }
                ?: contexto.getString(R.string.texto_sem_informacao)
            textoDescricaoFeitico.text = feitico.descricao
                ?.takeIf { it.isNotBlank() }
                ?: contexto.getString(R.string.texto_sem_informacao)
            itemView.setOnClickListener {
                clique(feitico)
            }
        }
    }
}
