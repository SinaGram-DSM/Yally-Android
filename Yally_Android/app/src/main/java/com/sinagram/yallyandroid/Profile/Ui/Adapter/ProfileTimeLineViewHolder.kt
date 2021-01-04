package com.sinagram.yallyandroid.Profile.Ui.Adapter

import android.graphics.Color
import android.media.MediaPlayer
import android.provider.MediaStore
import android.text.Spannable
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sinagram.yallyandroid.Base.BaseRepository
import com.sinagram.yallyandroid.Profile.Data.Post
import com.sinagram.yallyandroid.Home.Data.PostMediaPlayer
import com.sinagram.yallyandroid.Network.YallyConnector
import com.sinagram.yallyandroid.Profile.ViewModel.ProfileViewModel
import com.sinagram.yallyandroid.R
import com.sinagram.yallyandroid.Util.PostDate
import com.sinagram.yallyandroid.Util.PostTags
import kotlinx.android.synthetic.main.item_post_cardview.view.*
import java.lang.Exception

class ProfileTimeLineViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private var isMenuClick = false

    fun init(post:Post){
        itemView.apply {
            post_user_name_textView.text = post.user.nickname
            post_uploaded_time_textView.text = post.createdAt
            post_content_textView.text = post.content
            post_yally_count_textView.text = context.getString(R.string.yally_count, post.yally)
            post_comments_count_textView.text = context.getString(R.string.comment_count, post.comment)
            Glide.with(this).load(YallyConnector.s3+post.user.img).into(post_user_image_imageView)
            Glide.with(this).load(YallyConnector.s3+post.img).into(post_content_imageView)
        }
    }

    fun checkClickedYally(isYally: Boolean){
        if(isYally == true){
            itemView.post_do_yally_textView.setTextColor(Color.parseColor("#6E3CEF"))
            itemView.post_do_yally_imageView.setImageResource(R.drawable.ic_yally_note_colorful)
        } else{
            itemView.post_do_yally_textView.setTextColor(Color.parseColor("#707070"))
            itemView.post_do_yally_imageView.setImageResource(R.drawable.ic_yally_note)
        }
    }

    fun setTimeFromUploadedTime(uploadedDate: String) {
        itemView.post_uploaded_time_textView.text =
            PostDate(uploadedDate).setTimeFromUploadedTime()
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