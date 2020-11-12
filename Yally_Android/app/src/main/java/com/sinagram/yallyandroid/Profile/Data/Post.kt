package com.sinagram.yallyandroid.Profile.Data

data class Post(
	val postId: String,
	val user: profileUser,
	val content: String,
	val sound: String,
	val img: String,
	val comment: String,
	val yally: String,
	val isYally: Boolean,
	val createdAt: String
)

data class profileUser(
		val email: String,
		val nickname: String,
		val img: String
)