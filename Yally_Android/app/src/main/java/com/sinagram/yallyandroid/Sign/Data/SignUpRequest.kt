package com.sinagram.yallyandroid.Sign.Data

data class SignUpRequest(
    val email: String,
    val password: String,
    val nickname: String,
    val age: Int
)