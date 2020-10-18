package com.sinagram.yallyandroid.Home

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.sinagram.yallyandroid.Home.View.Fragment.SearchFragment
import com.sinagram.yallyandroid.Home.View.Fragment.TimeLineFragment
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

        supportFragmentManager.beginTransaction().add(R.id.home_fragment, TimeLineFragment()).commit()
    }

    private fun addSetOnNavigationItemSelectedListener() {
        home_bottom_navigationView.setOnNavigationItemSelectedListener { item ->
            val fragment = when (item.itemId) {
                R.id.menu_home_stack -> TimeLineFragment()
                R.id.menu_home_search -> SearchFragment()
                else -> TimeLineFragment()
            }

            replaceFragment(fragment)
            true
        }
    }

    fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.home_fragment, fragment)
        fragmentTransaction.commit()
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