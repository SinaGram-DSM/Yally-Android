package com.sinagram.yallyandroid.Home.Data

data class FriendResponse(
    val friends: List<Friend>
)

data class Friend(
    val email: String,
    val nickname: String,
    val img: String,
    var isListening: Boolean = false
)