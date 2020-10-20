package com.sinagram.yallyandroid.Home.Data

data class ListenersResponse(
    val listeners: List<Listener>
)

data class Listener(
    val isListening: Boolean,
    val listener: Int,
    val listening: Int,
    val nickname: String
)