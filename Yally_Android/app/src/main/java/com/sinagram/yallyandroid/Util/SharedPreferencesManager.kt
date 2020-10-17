package com.sinagram.yallyandroid.Util

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesManager {
    private val sharedPrefs: SharedPreferences = YallyApplication.context!!.getSharedPreferences(
        MY_APP_PREFERENCES,
        Context.MODE_PRIVATE
    )

    var accessToken: String?
        get() = sharedPrefs.getString(SAVE_TOKEN, null)
        set(value) {
            val editor = sharedPrefs.edit()
            editor.putString(SAVE_TOKEN, value)
            editor.apply()
        }

    var refreshToken: String?
        get() = sharedPrefs.getString(SAVE_REFRESH, null)
        set(value) {
            val editor = sharedPrefs.edit()
            editor.putString(SAVE_REFRESH, value)
            editor.apply()
        }

    var isLogin: Boolean
        get() = sharedPrefs.getBoolean(IS_LOGIN, false)
        set(value) {
            val editor = sharedPrefs.edit()
            editor.putBoolean(IS_LOGIN, value)
            editor.apply()
        }

    companion object {
        private const val MY_APP_PREFERENCES = "Yally-Android"
        private const val IS_LOGIN = "isLogin"
        private const val SAVE_TOKEN = "accessToken"
        private const val SAVE_REFRESH = "refreshToken"
        private var instance: SharedPreferencesManager? = null

        @Synchronized
        fun getInstance(): SharedPreferencesManager {
            if (instance == null) instance = SharedPreferencesManager()
            return instance!!
        }
    }
}