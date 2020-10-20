package com.sinagram.yallyandroid.Home.View

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.sinagram.yallyandroid.Home.Data.Listening
import com.sinagram.yallyandroid.Home.Data.Post
import com.sinagram.yallyandroid.R
import kotlinx.android.synthetic.main.item_post_cardview.view.*

class MainTimeLineAdapter(
    var postsList: List<Post>,
    private val clickYally: (Post, Observer<Boolean>) -> Unit,
    private val listeningOnPost: (Observer<List<Listening>>) -> Unit
) :
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
            setPostMenuAnimation()

            itemView.post_yally_layout.setOnClickListener {
                val observer = Observer<Boolean> {
                    if (it) {
                        postData.isYally = !postData.isYally
                        checkClickedYally(postData.isYally)
                        if (postData.isYally) {
                            itemView.post_yally_count_textView.text = (++postData.yally).toString()
                        } else {
                            itemView.post_yally_count_textView.text = (--postData.yally).toString()
                        }
                    }
                }

                clickYally(postData, observer)
            }

            if (postData.isMine) {
                itemView.post_menu_textView.text = "삭제"
            } else {
                val observer = Observer<List<Listening>> {
                    itemView.post_menu_textView.text = "리스닝"

                    for (i in it) {
                        if (postData.user.nickname == i.nickname) {
                            itemView.post_menu_textView.text = "언리스닝"
                        }
                    }
                }

                listeningOnPost(observer)
            }
        }
    }
}