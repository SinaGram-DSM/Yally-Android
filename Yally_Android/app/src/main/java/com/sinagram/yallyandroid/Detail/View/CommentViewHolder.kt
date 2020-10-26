package com.sinagram.yallyandroid.Detail.View

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sinagram.yallyandroid.Detail.Data.Comment
import com.sinagram.yallyandroid.Util.PostDate
import com.sinagram.yallyandroid.Network.YallyConnector
import com.sinagram.yallyandroid.Util.CommentMediaPlayer
import kotlinx.android.synthetic.main.item_post_comment.view.*

class CommentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var commentMediaPlayer: CommentMediaPlayer? = null

    fun commentInit(commentData: Comment) {
        itemView.run {
            Glide.with(this).load(YallyConnector.s3 + commentData.user.img).circleCrop()
                .into(comment_user_imageView)

            comment_user_name_textView.text = commentData.user.nickname
            comment_content_textView.text = commentData.content
            comment_uploaded_time_textView.text =
                PostDate(commentData.createdAt).setTimeFromUploadedTime()

            commentData.sound?.let {
                comment_player_layout.visibility = View.VISIBLE
            }
        }
    }

    fun startPlayer(sound: String?) {
        if (sound != null) {
            itemView.run {
                commentMediaPlayer = CommentMediaPlayer(
                    comment_player_seekBar,
                    comment_sound_start_textView,
                    comment_sound_end_textView
                )
                commentMediaPlayer?.isClickedPlay = true
                commentMediaPlayer?.startMediaPlayer(sound)
                commentMediaPlayer?.setSeekBarListener()
            }
        }
    }

    fun finishMedia() {
        commentMediaPlayer?.stopMediaPlayer()
        commentMediaPlayer = null
    }
}