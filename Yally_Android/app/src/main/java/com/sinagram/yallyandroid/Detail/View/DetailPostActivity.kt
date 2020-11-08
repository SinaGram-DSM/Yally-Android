package com.sinagram.yallyandroid.Detail.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.sinagram.yallyandroid.R
import com.sinagram.yallyandroid.Util.YallyMediaPlayer
import kotlinx.android.synthetic.main.activity_detail_post.*

class DetailPostActivity : AppCompatActivity() {
    private val detailPostFragment = createDetailPostFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_post)

        supportFragmentManager.beginTransaction()
            .add(R.id.detail_post_fragment, detailPostFragment)
            .commit()

        setEditText()
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

    private fun setEditText() {
        detail_edit_textView.setOnClickListener {
            val data = detailPostFragment.postData
            if (data != null) {
                val intent = Intent(this, EditPostActivity::class.java)
                intent.putExtra("editPost", data)
                startActivity(intent)
            }
        }
    }

    fun backPress(view: View) {
        onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        YallyMediaPlayer.stopMediaPlayer()
    }
}