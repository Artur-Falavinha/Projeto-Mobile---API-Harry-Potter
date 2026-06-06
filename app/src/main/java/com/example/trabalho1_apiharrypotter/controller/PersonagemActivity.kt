package com.example.trabalho1_apiharrypotter.controller

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.trabalho1_apiharrypotter.R
import com.example.trabalho1_apiharrypotter.api.HarryPotterApi
import com.example.trabalho1_apiharrypotter.model.Personagem
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PersonagemActivity : AppCompatActivity() {
    private lateinit var campoIdPersonagem: EditText
    private lateinit var botaoBuscarPersonagem: Button
    private lateinit var progressoPersonagem: ProgressBar
    private lateinit var imagemPersonagem: ImageView
    private lateinit var textoNomePersonagem: TextView
    private lateinit var textoEspeciePersonagem: TextView
    private lateinit var textoCasaPersonagem: TextView
    private val harryPotterApi = criarApi()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personagem)
        vincularComponentes()
        configurarVoltar()
        configurarBusca()
    }

    private fun vincularComponentes() {
        campoIdPersonagem = findViewById(R.id.campoIdPersonagem)
        botaoBuscarPersonagem = findViewById(R.id.botaoBuscarPersonagem)
        progressoPersonagem = findViewById(R.id.progressoPersonagem)
        imagemPersonagem = findViewById(R.id.imagemPersonagem)
        textoNomePersonagem = findViewById(R.id.textoNomePersonagem)
        textoEspeciePersonagem = findViewById(R.id.textoEspeciePersonagem)
        textoCasaPersonagem = findViewById(R.id.textoCasaPersonagem)
    }

    private fun configurarVoltar() {
        findViewById<Button>(R.id.botaoVoltar).setOnClickListener {
            finish()
        }
    }

    private fun configurarBusca() {
        botaoBuscarPersonagem.setOnClickListener {
            val idPersonagem = campoIdPersonagem.text.toString().trim()
            if (idPersonagem.isEmpty()) {
                mostrarErroCampoObrigatorio()
                return@setOnClickListener
            }
            buscarPersonagem(idPersonagem)
        }
    }

    private fun mostrarErroCampoObrigatorio() {
        Toast.makeText(this, R.string.mensagem_campo_obrigatorio, Toast.LENGTH_SHORT).show()
        campoIdPersonagem.requestFocus()
    }

    private fun buscarPersonagem(idPersonagem: String) {
        lifecycleScope.launch(Dispatchers.Main) {
            controlarCarregamento(true)
            esconderImagem()
            try {
                val personagens = withContext(Dispatchers.IO) {
                    harryPotterApi.buscarPersonagemPorId(idPersonagem)
                }
                val personagem = personagens.firstOrNull()
                if (personagem == null) {
                    limparResultado()
                    Toast.makeText(
                        this@PersonagemActivity,
                        R.string.mensagem_personagem_nao_encontrado,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    exibirPersonagem(personagem)
                }
            } catch (erro: Exception) {
                Log.e(TAG, "Erro ao buscar personagem", erro)
                Toast.makeText(
                    this@PersonagemActivity,
                    R.string.mensagem_erro_consulta,
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                controlarCarregamento(false)
            }
        }
    }

    private fun exibirPersonagem(personagem: Personagem) {
        textoNomePersonagem.text = montarTexto(R.string.rotulo_nome, personagem.nome)
        textoEspeciePersonagem.text = montarTexto(R.string.rotulo_especie, personagem.especie)
        textoCasaPersonagem.text = montarTexto(R.string.rotulo_casa, personagem.casa)
        carregarImagem(personagem.imagem)
    }

    private fun montarTexto(rotulo: Int, valor: String?): String {
        val texto = valor?.takeIf { it.isNotBlank() } ?: getString(R.string.texto_sem_informacao)
        return "${getString(rotulo)}: $texto"
    }

    private fun carregarImagem(urlImagem: String?) {
        esconderImagem()
        if (urlImagem.isNullOrBlank()) {
            imagemPersonagem.contentDescription = getString(R.string.texto_imagem_indisponivel)
            return
        }
        Picasso.get()
            .load(urlImagem)
            .resize(converterDpParaPixel(160), converterDpParaPixel(220))
            .centerCrop()
            .into(
                imagemPersonagem,
                object : Callback {
                    override fun onSuccess() {
                        imagemPersonagem.visibility = View.VISIBLE
                    }

                    override fun onError(erro: Exception?) {
                        Log.e(TAG, "Erro ao carregar imagem do personagem", erro)
                        esconderImagem()
                    }
                }
            )
    }

    private fun limparResultado() {
        textoNomePersonagem.text = ""
        textoEspeciePersonagem.text = ""
        textoCasaPersonagem.text = ""
        esconderImagem()
    }

    private fun esconderImagem() {
        imagemPersonagem.setImageDrawable(null)
        imagemPersonagem.visibility = View.GONE
    }

    private fun converterDpParaPixel(valorDp: Int): Int {
        return (valorDp * resources.displayMetrics.density).toInt()
    }

    private fun controlarCarregamento(carregando: Boolean) {
        progressoPersonagem.visibility = if (carregando) View.VISIBLE else View.GONE
        botaoBuscarPersonagem.isEnabled = !carregando
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
        private const val TAG = "PersonagemActivity"
    }
}
