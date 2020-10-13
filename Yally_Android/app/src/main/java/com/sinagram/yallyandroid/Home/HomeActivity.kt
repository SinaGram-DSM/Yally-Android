package com.sinagram.yallyandroid.Home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sinagram.yallyandroid.R
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        home_bottom_navigationView.background = null
        home_bottom_navigationView.menu.getItem(1).isEnabled = false
    }
}