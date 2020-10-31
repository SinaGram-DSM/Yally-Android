package com.sinagram.yallyandroid.Home.Data

data class SearchPostsResponse(val posts: List<SearchPost>)

data class SearchPost(
    val comment: Int,
    val content: String,
    val createdAt: String,
    val isYally: Boolean,
    val nickname: String,
    val sound: String?,
    val yally: Int
)