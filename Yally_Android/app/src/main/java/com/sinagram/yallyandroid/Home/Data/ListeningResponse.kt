package com.sinagram.yallyandroid.Home.Data

data class ListeningResponse(
    val listeners: List<Listening>
)

data class Listening(
    val isListening: Boolean,
    val listener: Int,
    val listening: Int,
    val nickname: String
)