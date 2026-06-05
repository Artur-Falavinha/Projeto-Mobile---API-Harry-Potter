package com.example.trabalho1_apiharrypotter.model

import com.google.gson.annotations.SerializedName

data class Feitico(
    @SerializedName("id")
    val id: String?,
    @SerializedName("name")
    val nome: String?,
    @SerializedName("description")
    val descricao: String?
)
