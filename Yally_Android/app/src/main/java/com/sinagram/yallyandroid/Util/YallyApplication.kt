package com.sinagram.yallyandroid.Util

import android.app.Application
import android.content.Context

class YallyApplication : Application() {
    override fun onCreate() {
        context = applicationContext
        super.onCreate()
    }

    companion object {
        var context: Context? = null
            private set
    }
}