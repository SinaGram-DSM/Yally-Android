package com.sinagram.yallyandroid.Home.Data

data class PostsResponse(
    val posts: List<Post>
)

data class Post(
    val comment: Int,
    val content: String,
    val createdAt: String,
    val id: String,
    val img: String,
    val isMine: Boolean,
    val isYally: Boolean,
    val sound: String,
    val user: User,
    val yally: Int
)

data class User(
    val email: String,
    val img: String,
    val nickname: String
)