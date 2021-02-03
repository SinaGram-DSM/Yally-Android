package com.sinagram.yallyandroid.Profile.Data

data class User(
    val nickname: String,
    val image: String,
    val listening: Int,
    val listener: Int,
    val isMine: Boolean
)