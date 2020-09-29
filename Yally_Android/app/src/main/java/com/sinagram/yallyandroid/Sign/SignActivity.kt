package com.sinagram.yallyandroid.Sign

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.sinagram.yallyandroid.R
import com.sinagram.yallyandroid.Sign.Fragment.LoginFragment
import com.sinagram.yallyandroid.Sign.Fragment.SignUpFragment
import kotlinx.android.synthetic.main.activity_sign.*

class SignActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign)

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val fragment = LoginFragment()
        fragmentTransaction.add(R.id.sign_fragment, fragment)
        fragmentTransaction.commit()
    }

    fun backPress(view: View) {
        onBackPressed()
    }
}