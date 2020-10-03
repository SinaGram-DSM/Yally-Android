package com.sinagram.yallyandroid.Sign

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.sinagram.yallyandroid.Home.HomeActivity
import com.sinagram.yallyandroid.R
import com.sinagram.yallyandroid.Sign.Fragment.LoginFragment
import com.sinagram.yallyandroid.Sign.Fragment.SignUpFragment

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