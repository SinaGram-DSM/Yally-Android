package com.sinagram.yallyandroid.Detail.Data

import com.sinagram.yallyandroid.Home.Data.User

data class CommentResponse(
    val comments: List<Comment>
)

data class Comment(
    val id: String,
    val content: String,
    val createdAt: String,
    val isMine: Boolean,
    val sound: String?,
    val user: User
)