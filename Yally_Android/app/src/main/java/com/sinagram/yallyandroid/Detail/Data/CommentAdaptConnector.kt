package com.sinagram.yallyandroid.Detail.Data

import android.view.View
import android.widget.ImageView
import com.sinagram.yallyandroid.Detail.ViewModel.DetailPostViewModel
import com.sinagram.yallyandroid.R
import com.sinagram.yallyandroid.Util.YallyMediaPlayer
import kotlinx.android.synthetic.main.item_post_comment.view.*

data class CommentAdaptConnector(
    var clickDeleteComment: (String, Int) -> Unit = { _, _ -> },
    var clickComment: (String?, View) -> Unit = { _, _ -> }
) {
    fun setAttributeFromComment(viewModel: DetailPostViewModel) {
        clickDeleteComment = { id: String, index: Int -> viewModel.deleteComment(id, index) }
        clickComment = { sound: String?, itemView: View ->
            val seekBar = itemView.comment_player_seekBar
            val endTextView = itemView.comment_sound_end_textView
            var isClick: Boolean? = null

            if (sound != null) {
                YallyMediaPlayer.apply {
                    setViews(seekBar, endTextView)
                    setSeekBarListener()

                    itemView.comment_play_imageView.setOnClickListener { view ->
                        if (isClick == null) {
                            initMediaPlayer(sound)
                            (view as ImageView).setImageResource(R.drawable.ic_baseline_pause_24)
                            isClick = true
                        } else {
                            isClick = !isClick!!

                            val resId = if (isClick!!) {
                                restartMediaPlayer()
                                R.drawable.ic_baseline_pause_24
                            } else {
                                pauseMediaPlayer()
                                R.drawable.ic_baseline_play_arrow_24
                            }

                            (view as ImageView).setImageResource(resId)
                        }
                    }
                }
            }
        }
    }
}