package com.sinagram.yallyandroid.Profile.Ui.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sinagram.yallyandroid.Profile.Data.Post
import com.sinagram.yallyandroid.R
import kotlinx.android.synthetic.main.item_post_cardview.view.*

class ProfileTimeLineAdapter(private val postList: List<Post>) : RecyclerView.Adapter<ProfileTimeLineViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileTimeLineViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post_cardview, parent, false)
        return ProfileTimeLineViewHolder(view)
    }

    override fun getItemCount(): Int = postList.size

    override fun onBindViewHolder(holder: ProfileTimeLineViewHolder, position: Int) {
        val postData = postList[position]

        holder.apply {
            init(postData)
            checkClickedYally(postData.isYally)
            setClickPost(postData)

        }
    }

}