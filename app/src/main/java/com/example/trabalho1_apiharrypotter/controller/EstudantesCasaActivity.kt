package com.example.trabalho1_apiharrypotter.controller

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trabalho1_apiharrypotter.R
import com.example.trabalho1_apiharrypotter.adapter.EstudantesAdapter
import com.example.trabalho1_apiharrypotter.api.HarryPotterApi
import com.example.trabalho1_apiharrypotter.model.Personagem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EstudantesCasaActivity : AppCompatActivity() {
    private lateinit var grupoCasas: RadioGroup
    private lateinit var botaoListarEstudantes: Button
    private lateinit var progressoEstudantes: ProgressBar
    private lateinit var listaEstudantes: RecyclerView
    private lateinit var estudantesAdapter: EstudantesAdapter
    private val harryPotterApi = criarApi()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estudantes_casa)
        vincularComponentes()
        configurarLista()
        configurarBusca()
    }

    private fun vincularComponentes() {
        grupoCasas = findViewById(R.id.grupoCasas)
        botaoListarEstudantes = findViewById(R.id.botaoListarEstudantes)
        progressoEstudantes = findViewById(R.id.progressoEstudantes)
        listaEstudantes = findViewById(R.id.listaEstudantes)
    }

    private fun configurarLista() {
        estudantesAdapter = EstudantesAdapter(emptyList())
        listaEstudantes.layoutManager = LinearLayoutManager(this)
        listaEstudantes.adapter = estudantesAdapter
    }

    private fun configurarBusca() {
        botaoListarEstudantes.setOnClickListener {
            val casaSelecionada = obterCasaSelecionada()
            if (casaSelecionada == null) {
                Toast.makeText(this, R.string.mensagem_casa_obrigatoria, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            buscarEstudantes(casaSelecionada)
        }
    }

    private fun obterCasaSelecionada(): String? {
        return when (grupoCasas.checkedRadioButtonId) {
            R.id.radioGryffindor -> "gryffindor"
            R.id.radioSlytherin -> "slytherin"
            R.id.radioRavenclaw -> "ravenclaw"
            R.id.radioHufflepuff -> "hufflepuff"
            else -> null
        }
    }

    private fun buscarEstudantes(casaSelecionada: String) {
        lifecycleScope.launch(Dispatchers.Main) {
            controlarCarregamento(true)
            try {
                val personagens = withContext(Dispatchers.IO) {
                    harryPotterApi.listarPersonagensDaCasa(casaSelecionada)
                }
                val estudantes = personagens.filter { it.estudante == true }
                if (estudantes.isEmpty()) {
                    estudantesAdapter.atualizarEstudantes(emptyList())
                    Toast.makeText(
                        this@EstudantesCasaActivity,
                        R.string.mensagem_estudantes_nao_encontrados,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    exibirEstudantes(estudantes)
                }
            } catch (erro: Exception) {
                Log.e(TAG, "Erro ao buscar estudantes", erro)
                Toast.makeText(
                    this@EstudantesCasaActivity,
                    R.string.mensagem_erro_consulta,
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                controlarCarregamento(false)
            }
        }
    }

    private fun exibirEstudantes(estudantes: List<Personagem>) {
        estudantesAdapter.atualizarEstudantes(estudantes)
    }

    private fun controlarCarregamento(carregando: Boolean) {
        progressoEstudantes.visibility = if (carregando) View.VISIBLE else View.GONE
        botaoListarEstudantes.isEnabled = !carregando
    }

    private fun criarApi(): HarryPotterApi {
        return Retrofit.Builder()
            .baseUrl(URL_BASE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HarryPotterApi::class.java)
    }

    companion object {
        private const val URL_BASE = "https://hp-api.onrender.com/api/"
        private const val TAG = "EstudantesCasaActivity"
    }
}
