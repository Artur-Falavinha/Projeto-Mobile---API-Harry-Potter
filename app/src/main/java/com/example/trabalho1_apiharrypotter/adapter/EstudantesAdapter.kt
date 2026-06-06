package com.example.trabalho1_apiharrypotter.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trabalho1_apiharrypotter.R
import com.example.trabalho1_apiharrypotter.model.Personagem
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class EstudantesAdapter(
    private var estudantes: List<Personagem>
) : RecyclerView.Adapter<EstudantesAdapter.EstudanteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EstudanteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_estudante, parent, false)
        return EstudanteViewHolder(view)
    }

    override fun onBindViewHolder(holder: EstudanteViewHolder, position: Int) {
        holder.bind(estudantes[position])
    }

    override fun getItemCount(): Int = estudantes.size

    fun atualizarEstudantes(novosEstudantes: List<Personagem>) {
        estudantes = novosEstudantes
        notifyDataSetChanged()
    }

    class EstudanteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imagemEstudante: ImageView = itemView.findViewById(R.id.imagemEstudante)
        private val textoNomeEstudante: TextView = itemView.findViewById(R.id.textoNomeEstudante)
        private val textoCasaEstudante: TextView = itemView.findViewById(R.id.textoCasaEstudante)

        fun bind(estudante: Personagem) {
            val contexto = itemView.context
            textoNomeEstudante.text = estudante.nome
                ?.takeIf { it.isNotBlank() }
                ?: contexto.getString(R.string.texto_sem_informacao)
            textoCasaEstudante.text = estudante.casa
                ?.takeIf { it.isNotBlank() }
                ?: contexto.getString(R.string.texto_sem_informacao)

            mostrarImagemIndisponivel()
            if (estudante.imagem.isNullOrBlank()) {
                return
            }
            imagemEstudante.setPadding(0, 0, 0, 0)
            imagemEstudante.scaleType = ImageView.ScaleType.CENTER_CROP
            Picasso.get()
                .load(estudante.imagem)
                .fit()
                .centerCrop()
                .into(
                    imagemEstudante,
                    object : Callback {
                        override fun onSuccess() {
                            imagemEstudante.scaleType = ImageView.ScaleType.CENTER_CROP
                        }

                        override fun onError(erro: Exception?) {
                            mostrarImagemIndisponivel()
                        }
                    }
                )
        }

        private fun mostrarImagemIndisponivel() {
            imagemEstudante.setPadding(8, 8, 8, 8)
            imagemEstudante.scaleType = ImageView.ScaleType.CENTER_INSIDE
            imagemEstudante.setImageResource(R.drawable.ic_personagem_indisponivel)
            imagemEstudante.contentDescription =
                itemView.context.getString(R.string.texto_imagem_indisponivel)
        }
    }
}
