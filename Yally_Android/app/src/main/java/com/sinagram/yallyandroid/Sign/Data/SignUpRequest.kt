package com.sinagram.yallyandroid.Sign.Data

import java.util.regex.Pattern

data class SignUpRequest(
    var email: String = "",
    var password: String = "",
    var nickname: String = "",
    var age: Int = 0
) {
    fun scanEnteredSignUpInformation(confirm: String): Boolean {
        var sum = 0
        nickname.forEach { sum += if (it in 'a'..'z' || it in 'A'..'Z') 1 else 2 }

        return sum <= 20
                && password == confirm
                && password.length in 8..20
                && password.isNotBlank()
                && nickname.isNotBlank()
                && age in 1..120
    }

    fun scanEnteredEmail() =
        email.length <= 30 && Pattern.compile("^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\\.([a-zA-Z])+([a-zA-Z])+")
            .matcher(email).matches()

    fun scanEnteredPinCode(pinCode: String) = pinCode.length == 6
}