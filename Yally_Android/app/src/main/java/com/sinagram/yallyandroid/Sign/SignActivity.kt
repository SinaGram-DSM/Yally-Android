package com.sinagram.yallyandroid.Sign

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.sinagram.yallyandroid.R

class SignActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign)
    }

    fun backPress(view: View) {
        onBackPressed()
    }
}