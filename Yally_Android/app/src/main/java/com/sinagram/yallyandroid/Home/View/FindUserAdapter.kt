package com.sinagram.yallyandroid.Home.View

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sinagram.yallyandroid.Home.Data.Friend
import com.sinagram.yallyandroid.Home.Data.User
import com.sinagram.yallyandroid.R
import kotlinx.android.synthetic.main.item_users_list.view.*

class FindUserAdapter<T>(private val userList: List<T>) :
    RecyclerView.Adapter<FindUserAdapter<T>.FriendViewHolder>() {
    inner class FriendViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        return FriendViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_users_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        val data = userList[position]

        holder.itemView.run {
            if (data is Friend) {
                Glide.with(context).load(data.img).circleCrop().into(user_list_imageView)
                user_name_textView.text = data.nickname
                Log.e("FindUserAdapter", "call?")
                changeListenTextColor(user_listen_button, data.isListening)
            } else if (data is User) {
                Glide.with(context).load(data.img).circleCrop().into(user_list_imageView)
                user_name_textView.text = data.nickname
                user_listener_layout.visibility = View.VISIBLE
                user_listener_textView.text = data.listener.toString()
                user_listening_layout.visibility = View.VISIBLE
                checkUnit(user_listening_textView, data.listening)
                changeListenTextColor(user_listen_button, data.isListening)
            }
        }
    }

    private fun changeListenTextColor(textView: TextView, isListening: Boolean) {
        if (isListening) {
            textView.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#362F99"))
            textView.text = "언리스닝"
        } else {
            textView.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#6960EF"))
            textView.text = "리스닝"
        }
    }

    @SuppressLint("SetTextI18n")
    private fun checkUnit(textView: TextView, listening: Int) {
        textView.text = if (listening >= 1000) {
            "${(listening / 1000)}k"
        } else {
            listening.toString()
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}