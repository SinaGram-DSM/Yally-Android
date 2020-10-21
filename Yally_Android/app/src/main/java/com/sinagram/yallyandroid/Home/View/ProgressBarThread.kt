package com.sinagram.yallyandroid.Home.View

import android.media.MediaPlayer
import android.widget.SeekBar

class ProgressBarThread(
    private val post_player_seekBar: SeekBar,
    private val mediaPlayer: MediaPlayer,
    private val isPlaying: Boolean
) : Thread() {

    override fun run() {
        while (isPlaying) {
            post_player_seekBar.progress = mediaPlayer.currentPosition
        }
    }
}