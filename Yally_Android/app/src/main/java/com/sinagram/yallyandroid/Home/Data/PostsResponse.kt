package com.sinagram.yallyandroid.Home.Data

data class PostsResponse(
    val posts: List<Post>
)

data class Post(
    var comment: Int,
    val content: String,
    val createdAt: String,
    var id: String?,
    val img: String?,
    val isMine: Boolean,
    var isYally: Boolean,
    val sound: String?,
    val user: User,
    var yally: Int
)

data class User(
    val email: String?,
    val img: String,
    val nickname: String
)