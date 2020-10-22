package com.sinagram.yallyandroid.Home.Data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class PostsResponse(
    val posts: List<Post>
)

@Parcelize
data class Post(
    val comment: Int,
    val content: String,
    val createdAt: String,
    val id: String,
    val img: String,
    val isMine: Boolean,
    var isYally: Boolean,
    val sound: String,
    val user: User,
    var yally: Int
) : Parcelable

@Parcelize
data class User(
    val email: String,
    val img: String,
    val nickname: String
) : Parcelable