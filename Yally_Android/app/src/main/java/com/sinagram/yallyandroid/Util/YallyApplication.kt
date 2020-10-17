package com.sinagram.yallyandroid.Util

import android.app.Application
import android.content.Context

class YallyApplication: Application() {
    override fun onCreate() {
        instance = this
        super.onCreate()
    }

    companion object {
        var instance: YallyApplication? = null
            private set

        val context: Context?
            get() = instance
    }
}