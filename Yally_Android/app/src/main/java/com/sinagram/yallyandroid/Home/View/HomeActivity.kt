package com.sinagram.yallyandroid.Home.View

import android.Manifest
import android.R.attr.actionBarSize
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.sinagram.yallyandroid.Home.View.Fragment.FindPostFragment
import com.sinagram.yallyandroid.Home.View.Fragment.FindUserFragment
import com.sinagram.yallyandroid.Home.View.Fragment.SearchFragment
import com.sinagram.yallyandroid.Home.View.Fragment.TimeLineFragment
import com.sinagram.yallyandroid.R
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    private var fabOpen: Animation? = null
    private var fabClose: Animation? = null
    private var isFabOpen = false
    private val PERMISSION_CODE = 1111

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        checkPermission()
        initialization()
        addSetOnNavigationItemSelectedListener()
        setSearchView()
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
            home_toolbar.visibility = View.GONE
            setMarginToRecyclerView(0)

            val fragment = when (item.itemId) {
                R.id.menu_home_stack -> TimeLineFragment()
                R.id.menu_home_search -> {
                    home_toolbar.visibility = View.VISIBLE
                    val tv = TypedValue()
                    if (theme.resolveAttribute(actionBarSize, tv, true)) {
                        setMarginToRecyclerView(
                            TypedValue.complexToDimensionPixelSize(
                                tv.data,
                                resources.displayMetrics
                            )
                        )
                    }
                    SearchFragment()
                }
                else -> TimeLineFragment()
            }

            replaceFragment(fragment)
            true
        }
    }

    private fun setMarginToRecyclerView(height: Int) {
        val marginParams = MarginLayoutParams(home_fragment.layoutParams)
        marginParams.setMargins(0, height, 0, 0)
        home_fragment.layoutParams = CoordinatorLayout.LayoutParams(marginParams)
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                PERMISSION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.isEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
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
            }
            return
        }
    }

    private fun setSearchView() {
        home_search_searchView.setOnSearchClickListener {
            home_title_textView.visibility = View.GONE
            home_search_cancel_textView.visibility = View.VISIBLE
            home_search_searchView.maxWidth = Int.MAX_VALUE
        }

        home_search_searchView.setOnCloseListener {
            home_title_textView.visibility = View.VISIBLE
            home_search_cancel_textView.visibility = View.GONE
            false
        }

        home_search_searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null && query.isNotEmpty()) {
                    val fragment = when {
                        query[0] == '@' -> {
                            var searchWord = "$query "
                            searchWord = searchWord.substring(1, searchWord.indexOf(" "))

                            FindUserFragment().apply {
                                arguments = Bundle().apply {
                                    putString("findQuery", searchWord)
                                }
                            }
                        }
                        else -> {
                            FindPostFragment().apply {
                                arguments = Bundle().apply {
                                    putString("findQuery", query)
                                }
                            }
                        }
                    }

                    replaceFragment(fragment)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        home_search_cancel_textView.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        if (!home_search_searchView.isIconified) {
            home_search_searchView.isIconified = true
        } else {
            super.onBackPressed()
        }
    }
}