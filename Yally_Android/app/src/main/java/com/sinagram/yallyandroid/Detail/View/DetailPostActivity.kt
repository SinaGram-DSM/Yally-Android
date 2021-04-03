package com.sinagram.yallyandroid.Detail.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.sinagram.yallyandroid.R
import com.sinagram.yallyandroid.Util.YallyMediaPlayer
import kotlinx.android.synthetic.main.activity_detail_post.*

class DetailPostActivity : AppCompatActivity() {
    private val detailPostFragment by lazy {
        createDetailPostFragment()
    }
    private val isMine by lazy {
        getIsMine()
    }

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

    private fun getIsMine(): Boolean {
        return intent.getBooleanExtra("isMine", false)
    }

    private fun setEditText() {
        if (isMine) {
            detail_edit_textView.visibility = View.VISIBLE
            detail_edit_textView.setOnClickListener {
                val intent = Intent(this, EditPostActivity::class.java)
                intent.putExtra("editPost", detailPostFragment.postData)
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