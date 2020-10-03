package com.sinagram.yallyandroid.Sign

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.sinagram.yallyandroid.Home.HomeActivity
import com.sinagram.yallyandroid.R
import com.sinagram.yallyandroid.Sign.Fragment.LoginFragment

class SignActivity : AppCompatActivity() {
    private val fragmentTransaction = supportFragmentManager.beginTransaction()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign)

        fragmentTransaction.add(R.id.sign_fragment, LoginFragment())
        fragmentTransaction.commit()
    }

    fun replaceFragment(fragment: Fragment) {
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