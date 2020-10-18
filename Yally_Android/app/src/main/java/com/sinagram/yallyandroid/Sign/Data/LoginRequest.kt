package com.sinagram.yallyandroid.Sign.Data

import java.util.regex.Pattern

data class LoginRequest(
    var email: String = "",
    var pinCode: String = "",
    var password: String = "",
    var confirm: String = "",
    val hashMap: HashMap<String, String> = HashMap()
) {
    private val emailPattern =
        Pattern.compile("^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\\.([a-zA-Z])+([a-zA-Z])+")

    fun scanEnteredLoginInformation() =
        scanEnteredEmail() && password.length >= 8 && password.isNotBlank()

    fun scanEnteredEmail() = email.length <= 30 && emailPattern.matcher(email).matches()

    fun scanEnteredPinCode() = pinCode.length == 6

    fun scanEnteredPassword() = password == confirm && password.length >= 8 && password.isNotBlank()

    fun addDataInHashMap(name: String, data: String) {
        hashMap[name] = data
    }
}