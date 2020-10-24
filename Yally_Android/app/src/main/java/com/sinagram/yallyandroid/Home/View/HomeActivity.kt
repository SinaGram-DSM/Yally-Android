package com.sinagram.yallyandroid.Home.View

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.sinagram.yallyandroid.Home.View.Fragment.SearchFragment
import com.sinagram.yallyandroid.Home.View.Fragment.TimeLineFragment
import com.sinagram.yallyandroid.R
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    private var fabOpen: Animation? = null
    private var fabClose: Animation? = null
    private var isFabOpen = false
    private val MY_PERMISSION_STORAGE = 1111

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        checkPermission()
        initialization()
        addSetOnNavigationItemSelectedListener()
    }

    private fun initialization() {
        home_bottom_navigationView.background = null
        home_bottom_navigationView.menu.getItem(1).isEnabled = false

        fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open)
        fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close)

        supportFragmentManager.beginTransaction().add(R.id.home_fragment, TimeLineFragment())
            .commit()
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

    private fun replaceFragment(fragment: Fragment) {
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

    private fun checkPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) || ActivityCompat.shouldShowRequestPermissionRationale(
                this, Manifest.permission.RECORD_AUDIO
            )
        ) {
            AlertDialog.Builder(this)
                .setTitle("알림")
                .setMessage("권한이 거부되었습니다.\n사용을 원하시면 설정에서 해당 권한을 직접 허용하셔야 합니다.")
                .setNeutralButton("설정") { _, _ ->
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.data = Uri.parse("package:$packageName")
                    startActivity(intent)
                }.setPositiveButton("확인") { _, _ -> finish() }
                .setCancelable(false)
                .create()
                .show()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                MY_PERMISSION_STORAGE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSION_STORAGE -> for (i in grantResults.indices) {
                if (grantResults[i] < 0) {
                    Toast.makeText(this, "해당 권한을 활성화 하셔야 합니다.", Toast.LENGTH_SHORT).show()
                    return
                }
            }
        }
    }
}