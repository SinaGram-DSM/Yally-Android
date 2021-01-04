package com.sinagram.yallyandroid.Home.Data

import android.graphics.Color
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.sinagram.yallyandroid.Detail.ViewModel.DetailPostViewModel
import com.sinagram.yallyandroid.Home.ViewModel.TimeLineViewModel
import com.sinagram.yallyandroid.Util.YallyMediaPlayer
import kotlinx.android.synthetic.main.item_post_cardview.view.*

data class PostAdaptConnector(
    var clickYally: (Post, Observer<Boolean>) -> Unit = { _, _ -> },
    var getListeningOnPost: (Observer<List<Listening>>) -> Unit = { _ -> },
    var listeningOnPost: (StateOnPostMenu, String, Observer<StateOnPostMenu>) -> Unit = { _, _, _ -> },
    var deletePost: (String, Int) -> Unit = { _, _ -> },
    var moveToComment: (Post) -> Unit = { _ -> },
    var clickPost: (String?, View) -> Unit = { _, _ -> }
) {
    fun setAttributeFromTimeLine(
        timeLineViewModel: TimeLineViewModel,
        lifecycleOwner: LifecycleOwner
    ) {
        clickYally = { data: Post, observer: Observer<Boolean> ->
            timeLineViewModel.clickYally(data).observe(lifecycleOwner, observer)
        }

        getListeningOnPost = { observer: Observer<List<Listening>> ->
            timeLineViewModel.getListeningList().observe(lifecycleOwner, observer)
        }

        listeningOnPost =
            { state: StateOnPostMenu, email: String, observer: Observer<StateOnPostMenu> ->
                timeLineViewModel.sendListeningToUser(state, email)
                    .observe(lifecycleOwner, observer)
            }

        deletePost = { id: String, index: Int -> timeLineViewModel.deletePost(id, index) }

        setClickPost()
    }

    fun setAttributeFromComment(
        detailPostViewModel: DetailPostViewModel,
        lifecycleOwner: LifecycleOwner
    ) {
        clickYally = { data: Post, observer: Observer<Boolean> ->
            detailPostViewModel.clickYally(data).observe(lifecycleOwner, observer)
        }

        getListeningOnPost = { observer: Observer<List<Listening>> ->
            detailPostViewModel.getListeningList().observe(lifecycleOwner, observer)
        }

        listeningOnPost =
            { state: StateOnPostMenu, email: String, observer: Observer<StateOnPostMenu> ->
                detailPostViewModel.sendListeningToUser(state, email)
                    .observe(lifecycleOwner, observer)
            }

        deletePost = { id: String, index: Int -> detailPostViewModel.deletePost(id, index) }

        setClickPost()
    }

    private fun setClickPost() {
        clickPost = { sound: String?, itemView: View ->
            var isClick = false
            val seekBar = itemView.post_player_seekBar
            val textView = itemView.post_soundLength_textView
            val imageView = itemView.post_content_imageView

            YallyMediaPlayer.apply {
                setViews(seekBar, textView)
                setSeekBarListener()
                setInvoke {
                    textView.text = ""
                    imageView.setColorFilter(Color.parseColor("#4C000000"))
                    seekBar.visibility = View.GONE
                }

                itemView.post_content_layout.setOnClickListener {
                    isClick = !isClick

                    if (isClick) {
                        sound?.let {
                            if (sound.contains('.')) {
                                initMediaPlayer(sound)
                            } else {
                                initMediaPlayer("$sound.mp3")
                            }

                        }
                        imageView.setColorFilter(Color.parseColor("#98000000"))
                        seekBar.visibility = View.VISIBLE
                    } else {
                        stopMediaPlayer()
                        textView.text = ""
                        imageView.setColorFilter(Color.parseColor("#4C000000"))
                        seekBar.visibility = View.GONE
                    }
                }
            }
        }
    }
}