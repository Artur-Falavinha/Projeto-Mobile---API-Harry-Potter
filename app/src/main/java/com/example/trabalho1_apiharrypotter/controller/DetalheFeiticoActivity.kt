package com.example.trabalho1_apiharrypotter.controller

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.trabalho1_apiharrypotter.R

class DetalheFeiticoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhe_feitico)
        configurarVoltar()
        exibirDetalhe()
    }

    private fun configurarVoltar() {
        findViewById<Button>(R.id.botaoVoltar).setOnClickListener {
            finish()
        }
    }

    private fun exibirDetalhe() {
        val nomeFeitico = intent.getStringExtra(FeiticosActivity.EXTRA_NOME_FEITICO)
            ?.takeIf { it.isNotBlank() }
            ?: getString(R.string.texto_sem_informacao)
        val descricaoFeitico = intent.getStringExtra(FeiticosActivity.EXTRA_DESCRICAO_FEITICO)
            ?.takeIf { it.isNotBlank() }
            ?: getString(R.string.texto_sem_informacao)

        findViewById<TextView>(R.id.textoNomeFeiticoDetalhe).text = nomeFeitico
        findViewById<TextView>(R.id.textoDescricaoFeiticoDetalhe).text =
            "${getString(R.string.rotulo_descricao)}: $descricaoFeitico"
    }
}
