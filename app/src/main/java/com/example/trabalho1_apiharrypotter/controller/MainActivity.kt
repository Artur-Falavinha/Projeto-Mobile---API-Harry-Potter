package com.example.trabalho1_apiharrypotter.controller

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.trabalho1_apiharrypotter.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        configurarBotoes()
    }

    private fun configurarBotoes() {
        findViewById<Button>(R.id.botaoPersonagem).setOnClickListener {
            abrirTela(PersonagemActivity::class.java)
        }
        findViewById<Button>(R.id.botaoProfessor).setOnClickListener {
            abrirTela(ProfessorActivity::class.java)
        }
        findViewById<Button>(R.id.botaoEstudantes).setOnClickListener {
            abrirTela(EstudantesCasaActivity::class.java)
        }
        findViewById<Button>(R.id.botaoFeiticos).setOnClickListener {
            abrirTela(FeiticosActivity::class.java)
        }
        findViewById<Button>(R.id.botaoSair).setOnClickListener {
            finish()
        }
    }

    private fun abrirTela(classeDestino: Class<*>) {
        startActivity(Intent(this, classeDestino))
    }
}
