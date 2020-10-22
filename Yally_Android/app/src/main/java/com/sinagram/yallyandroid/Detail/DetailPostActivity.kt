package com.sinagram.yallyandroid.Detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import com.sinagram.yallyandroid.R

class DetailPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_post)

        val detailPostFragment = createDetailPostFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.detail_post_fragment, detailPostFragment)
            .commit()
    }

    private fun getExtraFromIntent(): Parcelable {
        val parcelable: Parcelable? = intent.getParcelableExtra("postData")
        if (parcelable == null) finish()
        return parcelable!!
    }

    private fun createDetailPostFragment(): DetailPostFragment {
        val extra = getExtraFromIntent()

        return DetailPostFragment().apply {
            arguments = Bundle().apply {
                putParcelable("postData", extra)
            }
        }
    }
}