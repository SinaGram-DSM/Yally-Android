package com.sinagram.yallyandroid.Sign.Data

data class SignUpRequest(
    var email: String,
    var password: String,
    var nickname: String,
    var age: Int
) {

}