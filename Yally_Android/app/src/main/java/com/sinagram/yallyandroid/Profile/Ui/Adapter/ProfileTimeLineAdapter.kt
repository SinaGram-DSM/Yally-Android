package com.sinagram.yallyandroid.Profile.Ui.Adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sinagram.yallyandroid.Profile.Data.Post
import kotlinx.android.synthetic.main.item_post_cardview.view.*

class ProfileTimeLineAdapter(private val postList: ArrayList<Post>) : RecyclerView.Adapter<ProfileTimeLineAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setAudio() {

        }

        fun setDetail() {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int = postList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val view = holder.itemView
        val item: Post = postList[position]

        view.apply {
            Glide.with(context).load(item.user.img).into(post_user_image_imageView)
            Glide.with(context).load(item.content).into(post_content_imageView)
            post_uploaded_time_textView.text = item.createdAt
            post_user_name_textView.text = item.user.nickname
            post_content_textView.text = item.content
        }

    }


}

