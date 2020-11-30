package com.sinagram.yallyandroid.Splash

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.sinagram.yallyandroid.Home.View.HomeActivity
import com.sinagram.yallyandroid.R
import com.sinagram.yallyandroid.Sign.SignActivity
import com.sinagram.yallyandroid.Util.SharedPreferencesManager
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        isLogin()
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

    private fun isLogin() {
        if (SharedPreferencesManager.getInstance().isLogin) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }

    private fun setTransparentStatusBar() {
        with(window) {
            statusBarColor = Color.TRANSPARENT

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                this.setDecorFitsSystemWindows(false)
            } else {
                this.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }
}
