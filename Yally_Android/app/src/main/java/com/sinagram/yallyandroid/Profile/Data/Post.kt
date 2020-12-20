package com.sinagram.yallyandroid.Profile.Data

data class Posts(
	val posts: List<Post>
)

data class Post(
	val id: String?,
	val user: profileUser,
	val content: String,
	val sound: String?,
	val img: String?,
	val comment: Int,
	val yally: Int,
	val isYally: Boolean,
	val createdAt: String
)

data class profileUser(
	val email: String?,
	val nickname: String,
	val img: String?
)