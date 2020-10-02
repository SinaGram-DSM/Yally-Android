package com.sinagram.yallyandroid.Splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sinagram.yallyandroid.R
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        splash_login_button.setOnClickListener {
            val intent = Intent(this,)
        }

        splash_signUp_button.setOnClickListener {
            val intent = Intent(this,)
        }
    }
}
