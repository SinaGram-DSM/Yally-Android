package com.sinagram.yallyandroid.Home.View

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sinagram.yallyandroid.Home.Data.Friend
import com.sinagram.yallyandroid.R
import kotlinx.android.synthetic.main.item_users_list.view.*

class FriendAdapter(private val friendList: List<Friend>) :
    RecyclerView.Adapter<FriendAdapter.FriendViewHolder>() {
    inner class FriendViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        return FriendViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_users_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        val friendData = friendList[position]

        holder.itemView.run {
            Glide.with(context).load(friendData.img).circleCrop().into(user_list_imageView)
            user_name_textView.text = friendData.nickname
            changeListenButtonColor(user_list_imageView, friendData.isListening)
        }
    }

    private fun changeListenButtonColor(imageView: ImageView, isListening: Boolean) {
        if (isListening) {
            imageView.setBackgroundColor(Color.parseColor("#362F99"))
        } else {
            imageView.setBackgroundColor(Color.parseColor("#6960EF"))
        }
    }

    override fun getItemCount(): Int {
        return friendList.size
    }
}