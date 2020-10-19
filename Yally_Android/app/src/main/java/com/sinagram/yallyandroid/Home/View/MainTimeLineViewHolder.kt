package com.sinagram.yallyandroid.Home.View

import android.graphics.Color
import android.text.Spannable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.sinagram.yallyandroid.Home.Data.Post
import com.sinagram.yallyandroid.Home.Data.PostDate
import com.sinagram.yallyandroid.Home.Data.PostMediaPlayer
import com.sinagram.yallyandroid.Home.Data.PostTags
import com.sinagram.yallyandroid.R
import kotlinx.android.synthetic.main.item_post_cardview.view.*

class MainTimeLineViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun postInit(postData: Post) {
        itemView.apply {
            post_user_name_textView.text = postData.user.nickname
            post_yally_count_textView.text =
                context.getString(R.string.yally_count, postData.yally)
            post_comments_count_textView.text =
                context.getString(R.string.comment_count, postData.comment)
        }
    }

    fun setPostClicked(postData: Post) {
        val postSeekBar = itemView.post_player_seekBar

        PostMediaPlayer(postSeekBar).apply {
            setSeekBarListener()

            itemView.post_content_textView.setOnClickListener {
                isClickedPost = !isClickedPost

                postSeekBar.visibility = if (isClickedPost) {
                    startMediaPlayer(postData)
                    View.VISIBLE
                } else {
                    stopMediaPlayer()
                    View.GONE
                }
            }
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
        }
    }

    fun applyBoldToTags(content: String) {
        itemView.post_content_textView.text = content
        PostTags(content).applyBoldToTags(itemView.post_content_textView.text as Spannable)
    }
}