package com.example.trabalho1_apiharrypotter.controller

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.trabalho1_apiharrypotter.R
import com.example.trabalho1_apiharrypotter.api.HarryPotterApi
import com.example.trabalho1_apiharrypotter.model.Personagem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProfessorActivity : AppCompatActivity() {
    private lateinit var campoNomeProfessor: EditText
    private lateinit var botaoBuscarProfessor: Button
    private lateinit var progressoProfessor: ProgressBar
    private lateinit var textoNomeProfessor: TextView
    private lateinit var textoNomesAlternativosProfessor: TextView
    private lateinit var textoEspecieProfessor: TextView
    private lateinit var textoCasaProfessor: TextView
    private val harryPotterApi = criarApi()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_professor)
        vincularComponentes()
        configurarBusca()
    }

    private fun vincularComponentes() {
        campoNomeProfessor = findViewById(R.id.campoNomeProfessor)
        botaoBuscarProfessor = findViewById(R.id.botaoBuscarProfessor)
        progressoProfessor = findViewById(R.id.progressoProfessor)
        textoNomeProfessor = findViewById(R.id.textoNomeProfessor)
        textoNomesAlternativosProfessor = findViewById(R.id.textoNomesAlternativosProfessor)
        textoEspecieProfessor = findViewById(R.id.textoEspecieProfessor)
        textoCasaProfessor = findViewById(R.id.textoCasaProfessor)
    }

    private fun configurarBusca() {
        botaoBuscarProfessor.setOnClickListener {
            val nomeProfessor = campoNomeProfessor.text.toString().trim()
            if (nomeProfessor.isEmpty()) {
                Toast.makeText(this, R.string.mensagem_campo_obrigatorio, Toast.LENGTH_SHORT).show()
                campoNomeProfessor.requestFocus()
                return@setOnClickListener
            }
            buscarProfessor(nomeProfessor)
        }
    }

    private fun buscarProfessor(nomeProfessor: String) {
        lifecycleScope.launch(Dispatchers.Main) {
            controlarCarregamento(true)
            try {
                val professores = withContext(Dispatchers.IO) {
                    harryPotterApi.listarProfessores()
                }
                val professor = professores.firstOrNull { personagem ->
                    personagem.nome?.contains(nomeProfessor, ignoreCase = true) == true ||
                        personagem.nomesAlternativos.orEmpty().any {
                            it.contains(nomeProfessor, ignoreCase = true)
                        }
                }
                if (professor == null) {
                    limparResultado()
                    Toast.makeText(
                        this@ProfessorActivity,
                        R.string.mensagem_professor_nao_encontrado,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    exibirProfessor(professor)
                }
            } catch (erro: Exception) {
                Log.e(TAG, "Erro ao buscar professor", erro)
                Toast.makeText(
                    this@ProfessorActivity,
                    R.string.mensagem_erro_consulta,
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                controlarCarregamento(false)
            }
        }
    }

    private fun exibirProfessor(professor: Personagem) {
        val nomesAlternativos = professor.nomesAlternativos
            .orEmpty()
            .filter { it.isNotBlank() }
            .joinToString(", ")

        textoNomeProfessor.text = montarTexto(R.string.rotulo_nome, professor.nome)
        textoNomesAlternativosProfessor.text = montarTexto(
            R.string.rotulo_nomes_alternativos,
            nomesAlternativos
        )
        textoEspecieProfessor.text = montarTexto(R.string.rotulo_especie, professor.especie)
        textoCasaProfessor.text = montarTexto(R.string.rotulo_casa, professor.casa)
    }

    private fun montarTexto(rotulo: Int, valor: String?): String {
        val texto = valor?.takeIf { it.isNotBlank() } ?: getString(R.string.texto_sem_informacao)
        return "${getString(rotulo)}: $texto"
    }

    private fun limparResultado() {
        textoNomeProfessor.text = ""
        textoNomesAlternativosProfessor.text = ""
        textoEspecieProfessor.text = ""
        textoCasaProfessor.text = ""
    }

    private fun controlarCarregamento(carregando: Boolean) {
        progressoProfessor.visibility = if (carregando) View.VISIBLE else View.GONE
        botaoBuscarProfessor.isEnabled = !carregando
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
        private const val TAG = "ProfessorActivity"
    }
}
