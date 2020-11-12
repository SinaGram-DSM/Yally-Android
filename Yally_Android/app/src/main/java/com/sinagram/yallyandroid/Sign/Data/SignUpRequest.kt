package com.sinagram.yallyandroid.Sign.Data

import java.util.regex.Pattern

data class SignUpRequest(
    var email: String = "",
    var password: String = "",
    var nickname: String = "",
    var age: Int = 0
) {
    fun scanEnteredSignUpInformation(confirm: String) = password == confirm
            && password.length in 8..20
            && password.isNotBlank()
            && nickname.isNotBlank()
            && age in 1..120

    fun scanEnteredEmail() = email.length <= 30 && Pattern.compile("^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\\.([a-zA-Z])+([a-zA-Z])+")
        .matcher(email).matches()

    fun scanEnteredPinCode(pinCode: String) = pinCode.length == 6
}