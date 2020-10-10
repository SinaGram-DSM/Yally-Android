package com.sinagram.yallyandroid.Sign

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.sinagram.yallyandroid.Home.HomeActivity
import com.sinagram.yallyandroid.R
import com.sinagram.yallyandroid.Sign.Fragment.AuthenticationFragment
import com.sinagram.yallyandroid.Sign.Fragment.LoginFragment
import com.sinagram.yallyandroid.Sign.Fragment.SignUpFragment

class SignActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign)

        val fragmentTransaction = supportFragmentManager.beginTransaction()

        when (intent.getStringExtra("splash")) {
            "splash_login" -> fragmentTransaction.add(R.id.sign_fragment, LoginFragment()).commit()
            "splash_signUp" -> fragmentTransaction.add(R.id.sign_fragment, AuthenticationFragment()).commit()
            else -> {
                Toast.makeText(
                    this,
                    "일시적인 오류가 발생하였습니다.\n앱을 다시 실행하시길 바랍니다.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.sign_fragment, fragment)
        fragmentTransaction.commit()
    }

    fun moveToMain() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    fun backPress(view: View) {
        onBackPressed()
    }
}