package com.sinagram.yallyandroid.Detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sinagram.yallyandroid.R

class DetailPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_post)

        supportFragmentManager.beginTransaction().add(R.id.detail_post_fragment, DetailPostFragment()).commit()
    }
}