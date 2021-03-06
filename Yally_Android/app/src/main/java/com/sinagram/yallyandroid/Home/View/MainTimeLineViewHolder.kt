package com.sinagram.yallyandroid.Home.View

import android.graphics.Color
import android.text.Spannable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sinagram.yallyandroid.Home.Data.Post
import com.sinagram.yallyandroid.Util.PostDate
import com.sinagram.yallyandroid.Util.PostTags
import com.sinagram.yallyandroid.Network.YallyConnector
import com.sinagram.yallyandroid.R
import kotlinx.android.synthetic.main.item_post_cardview.view.*

class MainTimeLineViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private var isMenuClick = false

    fun postInit(postData: Post) {
        itemView.apply {
            post_user_name_textView.text = postData.user.nickname
            post_yally_count_textView.text = context.getString(R.string.yally_count, postData.yally)
            post_comments_count_textView.text =
                context.getString(R.string.comment_count, postData.comment)
            post_content_imageView.setColorFilter(Color.parseColor("#4C000000"))
            Glide.with(this).load(YallyConnector.s3 + postData.user.img).circleCrop()
                .into(post_user_image_imageView)
            Glide.with(this).load(YallyConnector.s3 + postData.img).centerCrop()
                .into(post_content_imageView)
        }
    }

    fun setTimeFromUploadedTime(uploadedDate: String) {
        itemView.post_uploaded_time_textView.text =
            PostDate(uploadedDate).setTimeFromUploadedTime()
    }

    fun checkClickedYally(isYally: Boolean) {
        if (isYally) {
            itemView.post_do_yally_imageView.setImageResource(R.drawable.ic_yally_note_colorful)
            itemView.post_do_yally_textView.setTextColor(Color.parseColor("#6E3CEF"))
        } else {
            itemView.post_do_yally_imageView.setImageResource(R.drawable.ic_yally_note)
            itemView.post_do_yally_textView.setTextColor(Color.parseColor("#707070"))
        }
    }

    fun applyBoldToTags(content: String) {
        itemView.post_content_textView.text = content
        PostTags(content).applyBoldToTags(itemView.post_content_textView.text as Spannable)
    }

    fun setPostMenuAnimation() {
        itemView.post_menu_imageView.setOnClickListener {
            isMenuClick = !isMenuClick

            if (isMenuClick) {
                it.animate().rotationX(180f).setDuration(300).withStartAction {
                    it.rotationX = 0f
                }.start()
                itemView.post_menu_textView.visibility = View.VISIBLE
            } else {
                it.animate().rotationX(0f).setDuration(300).withStartAction {
                    it.rotationX = 180f
                }.start()
                itemView.post_menu_textView.visibility = View.GONE
            }
        }
    }
}