package com.sinagram.yallyandroid.Profile.Ui.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sinagram.yallyandroid.Network.YallyConnector
import com.sinagram.yallyandroid.Profile.Data.ListenUser
import com.sinagram.yallyandroid.R

class ListenAdapter(private val listenList:List<ListenUser>): RecyclerView.Adapter<ListenAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val profileImg = itemView.findViewById<ImageView>(R.id.listenItem_profile_imageView)
        val nicknameTxt = itemView.findViewById<TextView>(R.id.listenItem_nickname_textView)
        val listenBtn = itemView.findViewById<Button>(R.id.listenItem_listening_button)

        fun bind(listenUser: ListenUser){
            Glide.with(itemView).load(YallyConnector.s3+listenUser.image).into(profileImg)
            nicknameTxt.text=listenUser.nickname

            when(listenUser.isListening){
                true -> listenBtn.text="언리스닝"
                false -> listenBtn.text="리스닝"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.listen_item, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listenList[position])
    }

    override fun getItemCount(): Int = listenList.size
}