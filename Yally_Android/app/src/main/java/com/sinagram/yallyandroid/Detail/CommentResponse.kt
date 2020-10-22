package com.sinagram.yallyandroid.Detail

import com.sinagram.yallyandroid.Home.Data.User

data class CommentResponse(
    val comments: List<Comment>
)

data class Comment(
    val content: String,
    val createdAt: String,
    val isMine: Boolean,
    val sound: String,
    val user: User
)