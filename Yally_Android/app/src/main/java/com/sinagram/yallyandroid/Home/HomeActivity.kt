package com.sinagram.yallyandroid.Home

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.sinagram.yallyandroid.R
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    private var fab_open: Animation? = null
    private var fab_close: Animation? = null
    private var isFabOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        home_bottom_navigationView.background = null
        home_bottom_navigationView.menu.getItem(1).isEnabled = false

        fab_open = AnimationUtils.loadAnimation(this, R.anim.fab_open)
        fab_close = AnimationUtils.loadAnimation(this, R.anim.fab_close)
        home_circleMenuButton.setOnClickListener {
            toggleFab()
        }
    }

    private fun toggleFab() {
        isFabOpen = if (isFabOpen) {
            home_writing_button.startAnimation(fab_close)
            home_user_profile_button.startAnimation(fab_close)
            home_writing_button.isClickable = false
            home_user_profile_button.isClickable = false
            home_circleMenuButton.animate().scaleX(1f).scaleY(1f).setDuration(300).withStartAction {
                home_circleMenuButton.scaleX = 1.5f
                home_circleMenuButton.scaleY = 1.5f
            }.start()
            false
        } else {
            home_writing_button.startAnimation(fab_open)
            home_user_profile_button.startAnimation(fab_open)
            home_writing_button.isClickable = true
            home_user_profile_button.isClickable = true
            home_circleMenuButton.animate().scaleX(1.5f).scaleY(1.5f).setDuration(300)
                .withStartAction {
                    home_circleMenuButton.scaleX = 1f
                    home_circleMenuButton.scaleY = 1f
                }.start()
            true
        }
    }
}