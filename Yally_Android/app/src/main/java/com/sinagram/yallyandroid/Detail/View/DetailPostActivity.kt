package com.sinagram.yallyandroid.Detail.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.sinagram.yallyandroid.R
import com.sinagram.yallyandroid.Util.YallyMediaPlayer

class DetailPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_post)

        val detailPostFragment = createDetailPostFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.detail_post_fragment, detailPostFragment)
            .commit()
    }

    private fun getExtraFromIntent(): String {
        val data = intent.getStringExtra("postData")
        if (data == null) finish()
        return data!!
    }

    private fun createDetailPostFragment(): DetailPostFragment {
        val extra = getExtraFromIntent()

        return DetailPostFragment().apply {
            arguments = Bundle().apply {
                putString("postDataId", extra)
            }
        }
    }

    fun backPress(view: View) {
        onBackPressed()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        YallyMediaPlayer.stopMediaPlayer()
    }
}