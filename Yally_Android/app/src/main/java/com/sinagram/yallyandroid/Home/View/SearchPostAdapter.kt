package com.sinagram.yallyandroid.Home.View

import android.graphics.Color
import android.text.Spannable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sinagram.yallyandroid.Home.Data.SearchPost
import com.sinagram.yallyandroid.R
import com.sinagram.yallyandroid.Util.PostDate
import com.sinagram.yallyandroid.Util.PostTags
import com.sinagram.yallyandroid.Util.YallyMediaPlayer
import kotlinx.android.synthetic.main.item_post_cardview.view.*

class SearchPostAdapter(var postsList: MutableList<SearchPost>) :
    RecyclerView.Adapter<SearchPostAdapter.SearchPostViewHolder>() {

    inner class SearchPostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun checkClickedYally(isYally: Boolean) {
            if (isYally) {
                itemView.post_do_yally_imageView.setImageResource(R.drawable.ic_yally_note_colorful)
                itemView.post_do_yally_textView.setTextColor(Color.parseColor("#6E3CEF"))
            } else {
                itemView.post_do_yally_imageView.setImageResource(R.drawable.ic_yally_note)
                itemView.post_do_yally_textView.setTextColor(Color.parseColor("#707070"))
            }
        }

        fun setTimeFromUploadedTime(uploadedDate: String) {
            itemView.post_uploaded_time_textView.text =
                PostDate(uploadedDate).setTimeFromUploadedTime()
        }

        fun applyBoldToTags(content: String) {
            itemView.post_content_textView.text = content
            PostTags(content).applyBoldToTags(itemView.post_content_textView.text as Spannable)
        }

        fun setOnClickContent(sound: String?) {
            itemView.post_content_layout.setOnClickListener {
                var isClick = false
                val seekBar = itemView.post_player_seekBar
                val textView = itemView.post_soundLength_textView
                val imageView = itemView.post_content_imageView

                YallyMediaPlayer.apply {
                    setViews(seekBar, textView)
                    setSeekBarListener()
                    setInvoke {
                        textView.text = ""
                        imageView.setColorFilter(Color.parseColor("#4C000000"))
                        seekBar.visibility = View.GONE
                    }

                    itemView.post_content_layout.setOnClickListener {
                        isClick = !isClick

                        if (isClick) {
                            sound?.let { initMediaPlayer(sound) }
                            imageView.setColorFilter(Color.parseColor("#98000000"))
                            seekBar.visibility = View.VISIBLE
                        } else {
                            stopMediaPlayer()
                            textView.text = ""
                            imageView.setColorFilter(Color.parseColor("#4C000000"))
                            seekBar.visibility = View.GONE
                        }
                    }
                }

                it.callOnClick()
            }
        }
    }

    override fun getItemCount(): Int = postsList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchPostViewHolder {
        return SearchPostViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_post_cardview, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SearchPostViewHolder, position: Int) {
        val postData = postsList[position]

        holder.run {
            itemView.run {
                post_comments_count_textView.text = postData.comment.toString()
                post_yally_count_textView.text =
                    context.getString(R.string.yally_count, postData.yally)
                post_user_name_textView.text = postData.nickname
                post_content_imageView.setColorFilter(Color.parseColor("#4C000000"))
            }

            setTimeFromUploadedTime(postData.createdAt)
            checkClickedYally(postData.isYally)
            applyBoldToTags(postData.content)
        }
    }
}