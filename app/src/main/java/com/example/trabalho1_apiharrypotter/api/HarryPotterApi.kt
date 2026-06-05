package com.example.trabalho1_apiharrypotter.api

import com.example.trabalho1_apiharrypotter.model.Feitico
import com.example.trabalho1_apiharrypotter.model.Personagem
import retrofit2.http.GET
import retrofit2.http.Path

interface HarryPotterApi {
    @GET("character/{id}")
    suspend fun buscarPersonagemPorId(@Path("id") id: String): List<Personagem>

    @GET("characters/staff")
    suspend fun listarProfessores(): List<Personagem>

    @GET("characters/house/{casa}")
    suspend fun listarPersonagensDaCasa(@Path("casa") casa: String): List<Personagem>

    @GET("spells")
    suspend fun listarFeiticos(): List<Feitico>
}
