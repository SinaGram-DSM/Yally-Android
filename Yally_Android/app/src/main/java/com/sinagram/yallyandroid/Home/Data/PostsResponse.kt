package com.sinagram.yallyandroid.Home.Data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class PostsResponse(
    val posts: List<Post>
)

@Parcelize
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
): Parcelable

@Parcelize
data class User(
    val email: String,
    val img: String,
    val nickname: String,
    val listening: Int = 0,
    val listener: Int = 0,
    var isListening: Boolean = false
): Parcelable