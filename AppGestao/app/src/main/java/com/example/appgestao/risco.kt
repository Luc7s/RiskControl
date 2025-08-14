package com.example.appgestao

data class Risco(
    val descricao: String = "",
    val dataHora: String = "",
    val usuarioId: String = "",
    val classificacao: String = "",
    val mapX: Int = -1,
    val mapY: Int = -1
)
