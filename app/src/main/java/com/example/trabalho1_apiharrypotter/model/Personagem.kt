package com.example.trabalho1_apiharrypotter.model

import com.google.gson.annotations.SerializedName

data class Personagem(
    @SerializedName("id")
    val id: String?,
    @SerializedName("name")
    val nome: String?,
    @SerializedName("alternate_names")
    val nomesAlternativos: List<String>?,
    @SerializedName("species")
    val especie: String?,
    @SerializedName("house")
    val casa: String?,
    @SerializedName("image")
    val imagem: String?,
    @SerializedName("hogwartsStaff")
    val professor: Boolean?,
    @SerializedName("hogwartsStudent")
    val estudante: Boolean?
)
