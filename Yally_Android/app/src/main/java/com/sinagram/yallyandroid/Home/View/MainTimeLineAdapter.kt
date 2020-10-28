package com.sinagram.yallyandroid.Home.View

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sinagram.yallyandroid.Home.Data.Post
import com.sinagram.yallyandroid.Home.Data.PostAdaptConnector
import com.sinagram.yallyandroid.Home.Data.StateOnPostMenu
import com.sinagram.yallyandroid.R
import com.sinagram.yallyandroid.Util.CustomDialog
import kotlinx.android.synthetic.main.item_post_cardview.view.*

class MainTimeLineAdapter(
    var postsList: MutableList<Post>,
    private val postAdaptConnector: PostAdaptConnector
) : RecyclerView.Adapter<MainTimeLineViewHolder>() {
    private lateinit var stateOfPostMenu: StateOnPostMenu

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
            setTimeFromUploadedTime(postData.createdAt)
            applyBoldToTags(postData.content)
            checkClickedYally(postData.isYally)
            setPostMenuAnimation()
            setBubbleTitle(postData, itemView)

            itemView.post_yally_layout.setOnClickListener {
                postAdaptConnector.clickYally(postData) {
                    if (it) {
                        postData.isYally = !postData.isYally
                        checkClickedYally(postData.isYally)
                        itemView.post_yally_count_textView.text = if (postData.isYally) {
                            itemView.context.getString(R.string.yally_count, ++postData.yally)
                        } else {
                            itemView.context.getString(R.string.yally_count, --postData.yally)
                        }
                    }
                }
            }

            itemView.post_content_layout.setOnClickListener {
                postAdaptConnector.clickPost(postData.sound, itemView)
                it.callOnClick()
            }

            itemView.post_menu_textView.setOnClickListener {
                if (stateOfPostMenu == StateOnPostMenu.DELETE) {
                    CustomDialog(itemView.context).showDialog(true) {
                        postData.id?.let { postAdaptConnector.deletePost(it, position) }
                    }
                } else {
                    postData.user.email?.let { email ->
                        postAdaptConnector.listeningOnPost(stateOfPostMenu, email) {
                            when (it) {
                                StateOnPostMenu.LISTENING -> {
                                    itemView.post_menu_textView.text = "리스닝"
                                    stateOfPostMenu = StateOnPostMenu.LISTENING
                                }
                                StateOnPostMenu.UNLISTENING -> {
                                    itemView.post_menu_textView.text = "언리스닝"
                                    stateOfPostMenu = StateOnPostMenu.UNLISTENING
                                }
                                else -> {
                                }
                            }
                        }
                    }
                }

                itemView.post_menu_imageView.callOnClick()
            }

            itemView.post_comments_layout.setOnClickListener {
                postData.id?.let { postAdaptConnector.moveToComment(it) }
            }

            itemView.post_comments_count_textView.setOnClickListener {
                postData.id?.let { postAdaptConnector.moveToComment(it) }
            }
        }
    }

    private fun setBubbleTitle(postData: Post, itemView: View) {
        if (postData.isMine) {
            itemView.post_menu_textView.text = "삭제"
            stateOfPostMenu = StateOnPostMenu.DELETE
        } else {
            postAdaptConnector.getListeningOnPost {
                itemView.post_menu_textView.text = "리스닝"
                stateOfPostMenu = StateOnPostMenu.LISTENING

                for (i in it) {
                    if (postData.user.nickname == i.nickname) {
                        itemView.post_menu_textView.text = "언리스닝"
                        stateOfPostMenu = StateOnPostMenu.UNLISTENING
                    }
                }
            }
        }
    }

    fun removeAt(position: Int) {
        postsList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, postsList.size)
    }

    fun minusComment() {
        postsList[0].comment--
        notifyDataSetChanged()
    }
}