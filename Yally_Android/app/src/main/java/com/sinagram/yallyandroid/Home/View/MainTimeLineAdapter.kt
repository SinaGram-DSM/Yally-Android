package com.sinagram.yallyandroid.Home.View

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.sinagram.yallyandroid.Home.Data.CustomDialog
import com.sinagram.yallyandroid.Home.Data.Listening
import com.sinagram.yallyandroid.Home.Data.Post
import com.sinagram.yallyandroid.Home.Data.StateOnPostMenu
import com.sinagram.yallyandroid.R
import kotlinx.android.synthetic.main.item_post_cardview.view.*

class MainTimeLineAdapter(
    var postsList: MutableList<Post>,
    private val clickYally: (Post, Observer<Boolean>) -> Unit,
    private val getListeningOnPost: (Observer<List<Listening>>) -> Unit,
    private val listeningOnPost: (StateOnPostMenu, String, Observer<StateOnPostMenu>) -> Unit,
    private val deletePost: (String, Int) -> Unit
) :
    RecyclerView.Adapter<MainTimeLineViewHolder>() {
    lateinit var stateOfPostMenu: StateOnPostMenu

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
            clickCommentOnPost()
            setPostMenuAnimation()
            setBubbleTitle(postData, itemView)

            itemView.post_yally_layout.setOnClickListener {
                clickYally(postData) {
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
            }

            itemView.post_menu_textView.setOnClickListener {
                if (stateOfPostMenu == StateOnPostMenu.DELETE) {
                    CustomDialog(itemView.context).showDialog { deletePost(postData.id, position) }
                } else {
                    listeningOnPost(stateOfPostMenu, postData.user.email) {
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

                itemView.post_menu_imageView.callOnClick()
            }
        }
    }

    private fun setBubbleTitle(postData: Post, itemView: View) {
        if (postData.isMine) {
            itemView.post_menu_textView.text = "삭제"
            stateOfPostMenu = StateOnPostMenu.DELETE
        } else {
            getListeningOnPost {
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
}