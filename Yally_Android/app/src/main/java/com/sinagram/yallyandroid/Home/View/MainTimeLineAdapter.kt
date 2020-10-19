package com.sinagram.yallyandroid.Home.View

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sinagram.yallyandroid.Home.Data.Post
import com.sinagram.yallyandroid.R
import kotlinx.android.synthetic.main.item_post_cardview.view.*

class MainTimeLineAdapter(var postsList: List<Post>) :
    RecyclerView.Adapter<MainTimeLineViewHolder>() {

    override fun getItemCount(): Int = postsList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainTimeLineViewHolder {
        return MainTimeLineViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_post_cardview, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MainTimeLineViewHolder, position: Int) {
        val postData = postsList[position]

        holder.apply {
            postInit(postData)
            setPostClicked(postData)
            setTimeFromUploadedTime(postData.createdAt)
            applyBoldToTags(postData.content)
            checkClickedYally(postData.isYally)

            itemView.post_yally_layout.setOnClickListener {
                postData.isYally = !postData.isYally
                checkClickedYally(postData.isYally)
            }
        }
    }
}