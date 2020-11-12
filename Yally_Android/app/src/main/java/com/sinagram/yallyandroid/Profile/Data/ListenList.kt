package com.sinagram.yallyandroid.Profile.Data

data class ListenList (
    val target: target,
    val listeners: ArrayList<ListenUser>
)

data class target(
    val nickname: String,
    val image: String,
    val listening: Int,
    val listener: Int,
    val isMine: Boolean
)
