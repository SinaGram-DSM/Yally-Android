package com.sinagram.yallyandroid.Detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sinagram.yallyandroid.Home.Data.PostDate
import com.sinagram.yallyandroid.R
import kotlinx.android.synthetic.main.item_post_comment.view.*

class CommentAdapter(var commentList: MutableList<Comment>) :
    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_post_comment, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val commentData = commentList[position]

        holder.run {
            commentInit(commentData)
        }
    }

    inner class CommentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun commentInit(commentData: Comment) {
            itemView.run {
                Glide.with(this).load(commentData.user.img).circleCrop()
                    .into(comment_user_imageView)

                comment_user_name_textView.text = commentData.user.nickname
                comment_content_textView.text = commentData.content
                comment_uploaded_time_textView.text =
                    PostDate(commentData.createdAt).setTimeFromUploadedTime()

                if (commentData.isMine) setDeleteButton()

                if (commentData.sound.isNotBlank()) {
                    comment_player_layout.visibility = View.VISIBLE
                }
            }
        }

        private fun setDeleteButton() {
            itemView.comment_delete_imageView.visibility = View.VISIBLE
        }
    }
}