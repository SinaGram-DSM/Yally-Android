package com.sinagram.yallyandroid.Profile.Ui.Adapter

import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sinagram.yallyandroid.Profile.Data.Post
import com.sinagram.yallyandroid.Home.Data.PostMediaPlayer
import com.sinagram.yallyandroid.R
import kotlinx.android.synthetic.main.item_post_cardview.view.*
import java.lang.Exception

class ProfileTimeLineViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun init(post:Post){
        itemView.apply {
            post_user_name_textView.text = post.user.nickname
            post_uploaded_time_textView.text = post.createdAt
            post_content_textView.text = post.content
            post_yally_count_textView.text = context.getString(R.string.yally_count, post.yally)
            post_comments_count_textView.text = context.getString(R.string.comment_count, post.comment)
            Glide.with(this).load(post.user.img).into(post_user_image_imageView)
            Glide.with(this).load(post.img).into(post_content_imageView)
        }
    }

    fun checkClickedYally(isYally: Boolean){
        if(isYally == true){
            itemView.post_do_yally_textView.setTextColor(Color.parseColor("#6E3CEF"))
            itemView.post_do_yally_imageView.setImageResource(R.drawable.ic_yally_note_colorful)
        }else{
            itemView.post_do_yally_textView.setTextColor(Color.parseColor("#707070"))
            itemView.post_do_yally_imageView.setImageResource(R.drawable.ic_yally_note)
        }
    }

    fun setClickPost(post: Post){
        val postSeekBar = itemView.post_player_seekBar

        PostMediaPlayer(postSeekBar).apply {
            isClickedPost = !isClickedPost

            try{
                postSeekBar.visibility = if(isClickedPost) {
                    startMediaPlayer(post)
                    itemView.post_soundLength_textView.text = getSoundSourceLength()
                    itemView.post_content_imageView.setColorFilter(Color.parseColor("#98000000"))
                    View.VISIBLE
                } else{
                    stopMediaPlayer()
                    itemView.post_content_imageView.setColorFilter(Color.parseColor("#4C000000"))
                    View.GONE
                }

            } catch (e: Exception) {
                Log.e("profileTimeLine", e.message.toString())
            }
        }
    }

}