package com.sinagram.yallyandroid.Detail.View

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.sinagram.yallyandroid.Home.Data.EditPostRequest
import com.sinagram.yallyandroid.Home.Data.Post
import com.sinagram.yallyandroid.Home.ViewModel.TimeLineViewModel
import com.sinagram.yallyandroid.R
import kotlinx.android.synthetic.main.activity_edit_post.*

class EditPostActivity : AppCompatActivity() {
    private val postData: Post by lazy {
        intent.getParcelableExtra("editPost")!!
    }
    private val editPostRequest: EditPostRequest = EditPostRequest()
    private val timeLineViewModel: TimeLineViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_post)

        setViews()
        edit_post_complete_textView.setOnClickListener {
            if (edit_post_content_editText.text.toString().isNotEmpty()) {
                timeLineViewModel.toEditPost(postData.id!!, editPostRequest)
                finish()
            }
        }
    }

    private fun setViews() {
        Glide.with(this).load(postData.user.img).circleCrop().into(edit_post_user_imageView)
        edit_post_content_editText.setText(postData.content)
    }

    fun backPress(view: View) {
        onBackPressed()
    }
}