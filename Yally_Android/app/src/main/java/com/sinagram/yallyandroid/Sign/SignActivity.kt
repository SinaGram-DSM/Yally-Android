package com.sinagram.yallyandroid.Sign

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.sinagram.yallyandroid.R
import com.sinagram.yallyandroid.Sign.Fragment.LoginFragment
import com.sinagram.yallyandroid.Sign.Fragment.SignUpFragment

class SignActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign)

        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.sign_fragment, LoginFragment())
        fragmentTransaction.commit()
    }

    fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.sign_fragment, fragment)
        fragmentTransaction.commit()
    }

    fun moveToMain() {

    }

    fun backPress(view: View) {
        onBackPressed()
    }
}