package com.sinagram.yallyandroid.Home

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sinagram.yallyandroid.R
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity() {
    private var fabOpen: Animation? = null
    private var fabClose: Animation? = null
    private var isFabOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        initialization()
        addSetOnNavigationItemSelectedListener()
    }

    private fun initialization() {
        home_bottom_navigationView.background = null
        home_bottom_navigationView.menu.getItem(1).isEnabled = false

        fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open)
        fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close)
    }

    private fun addSetOnNavigationItemSelectedListener() {
        home_bottom_navigationView.setOnNavigationItemSelectedListener { item ->
            val fragment = when (item.itemId) {
                R.id.menu_home_stack -> TimeLineFragment()
                R.id.menu_home_search -> SearchFragment()
                else -> TimeLineFragment()
            }

            supportFragmentManager.beginTransaction().replace(R.id.home_fragment, fragment)

            true
        }
    }

    fun toggleFab(view: View) {
        val animator = home_circleMenuButton.animate()

        if (isFabOpen) {
            home_writing_button.startAnimation(fabClose)
            home_user_profile_button.startAnimation(fabClose)
            animator.scaleX(1f).scaleY(1f).setDuration(300).withStartAction {
                home_circleMenuButton.scaleX = 1.5f
                home_circleMenuButton.scaleY = 1.5f
            }
        } else {
            home_writing_button.startAnimation(fabOpen)
            home_user_profile_button.startAnimation(fabOpen)
            animator.scaleX(1.5f).scaleY(1.5f).setDuration(300).withStartAction {
                home_circleMenuButton.scaleX = 1f
                home_circleMenuButton.scaleY = 1f
            }
        }

        animator.start()
        isFabOpen = !isFabOpen
        home_writing_button.isClickable = isFabOpen
        home_user_profile_button.isClickable = isFabOpen
    }
}