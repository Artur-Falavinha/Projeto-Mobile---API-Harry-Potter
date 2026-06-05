package com.example.trabalho1_apiharrypotter.controller

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trabalho1_apiharrypotter.R
import com.example.trabalho1_apiharrypotter.adapter.FeiticosAdapter
import com.example.trabalho1_apiharrypotter.api.HarryPotterApi
import com.example.trabalho1_apiharrypotter.model.Feitico
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FeiticosActivity : AppCompatActivity() {
    private lateinit var progressoFeiticos: ProgressBar
    private lateinit var listaFeiticos: RecyclerView
    private lateinit var feiticosAdapter: FeiticosAdapter
    private val harryPotterApi = criarApi()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feiticos)
        vincularComponentes()
        configurarLista()
        buscarFeiticos()
    }

    private fun vincularComponentes() {
        progressoFeiticos = findViewById(R.id.progressoFeiticos)
        listaFeiticos = findViewById(R.id.listaFeiticos)
    }

    private fun configurarLista() {
        feiticosAdapter = FeiticosAdapter(emptyList()) { feitico ->
            abrirDetalhe(feitico)
        }
        listaFeiticos.layoutManager = LinearLayoutManager(this)
        listaFeiticos.adapter = feiticosAdapter
    }

    private fun buscarFeiticos() {
        lifecycleScope.launch(Dispatchers.Main) {
            controlarCarregamento(true)
            try {
                val feiticos = withContext(Dispatchers.IO) {
                    harryPotterApi.listarFeiticos()
                }
                if (feiticos.isEmpty()) {
                    Toast.makeText(
                        this@FeiticosActivity,
                        R.string.mensagem_feiticos_nao_encontrados,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    feiticosAdapter.atualizarFeiticos(feiticos)
                }
            } catch (erro: Exception) {
                Log.e(TAG, "Erro ao buscar feitiços", erro)
                Toast.makeText(
                    this@FeiticosActivity,
                    R.string.mensagem_erro_consulta,
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                controlarCarregamento(false)
            }
        }
    }

    private fun abrirDetalhe(feitico: Feitico) {
        val intent = Intent(this, DetalheFeiticoActivity::class.java)
        intent.putExtra(EXTRA_NOME_FEITICO, feitico.nome)
        intent.putExtra(EXTRA_DESCRICAO_FEITICO, feitico.descricao)
        startActivity(intent)
    }

    private fun controlarCarregamento(carregando: Boolean) {
        progressoFeiticos.visibility = if (carregando) View.VISIBLE else View.GONE
    }

    private fun criarApi(): HarryPotterApi {
        return Retrofit.Builder()
            .baseUrl(URL_BASE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HarryPotterApi::class.java)
    }

    companion object {
        const val EXTRA_NOME_FEITICO = "nome_feitico"
        const val EXTRA_DESCRICAO_FEITICO = "descricao_feitico"
        private const val URL_BASE = "https://hp-api.onrender.com/api/"
        private const val TAG = "FeiticosActivity"
    }
}
