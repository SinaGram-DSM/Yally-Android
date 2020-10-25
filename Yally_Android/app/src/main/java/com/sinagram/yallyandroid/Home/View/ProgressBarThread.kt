package com.sinagram.yallyandroid.Home.View

import android.media.MediaPlayer
import android.util.Log
import android.widget.SeekBar

class ProgressBarThread(
    private val post_player_seekBar: SeekBar,
    private val mediaPlayer: MediaPlayer,
    private val isPlaying: Boolean
) : Thread() {

    override fun run() {
        while (isPlaying && !isInterrupted) {
            try {
                post_player_seekBar.progress = mediaPlayer.currentPosition
            } catch (e: Exception) {
                Log.e("ProgressBarThread", e.message.toString())
            }
        }
    }
}