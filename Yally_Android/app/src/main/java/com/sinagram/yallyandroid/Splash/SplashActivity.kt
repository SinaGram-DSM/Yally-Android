package com.sinagram.yallyandroid.Splash

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.sinagram.yallyandroid.R
import com.sinagram.yallyandroid.Sign.SignActivity
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setTransparentStatusBar()

        splash_login_button.setOnClickListener {
            val intent = Intent(this, SignActivity::class.java)
            intent.putExtra("splash", "splash_login")
            startActivity(intent)
        }

        splash_signUp_button.setOnClickListener {
            val intent = Intent(this, SignActivity::class.java)
            intent.putExtra("splash", "splash_signUp")
            startActivity(intent)
        }
    }

    private fun setTransparentStatusBar() {
        window?.decorView?.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = Color.TRANSPARENT
    }
}
