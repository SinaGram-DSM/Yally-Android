package com.sinagram.yallyandroid.Home.Data

data class ListeningResponse(
    val listenings: List<Listening>
)

data class Listening(
    val nickname: String,
    val image: String,
    val listener: Int,
    val listening: Int,
    val isListening: Boolean
)