package com.sinagram.yallyandroid.Detail.View

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.sinagram.yallyandroid.Detail.Data.Comment
import com.sinagram.yallyandroid.Detail.Data.CommentAdaptConnector
import com.sinagram.yallyandroid.Util.CustomDialog
import com.sinagram.yallyandroid.R
import kotlinx.android.synthetic.main.item_post_comment.view.*

class CommentAdapter(
    var commentList: MutableList<Comment>,
    private val commentAdaptConnector: CommentAdaptConnector
) : RecyclerView.Adapter<CommentViewHolder>() {
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
            commentAdaptConnector.clickComment(commentData.sound, itemView)
            if (commentData.isMine) setDeleteButton(
                commentData.id,
                position,
                itemView.comment_delete_imageView
            )
        }
    }

    private fun setDeleteButton(id: String, index: Int, view: ImageView) {
        view.run {
            visibility = View.VISIBLE

            setOnClickListener {
                CustomDialog(context).showDialog(false) {
                    commentAdaptConnector.clickDeleteComment(id, index)
                }
            }
        }
    }

    fun removeAt(index: Int) {
        commentList.removeAt(index)
        notifyDataSetChanged()
    }
}