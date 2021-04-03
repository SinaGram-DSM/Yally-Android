package com.sinagram.yallyandroid.Util

import android.media.MediaPlayer
import android.util.Log
import android.widget.SeekBar

class ProgressBarThread(
    private val post_player_seekBar: SeekBar?,
    private val mediaPlayer: MediaPlayer?,
) : Thread() {
    override fun run() {
        try {
            if (mediaPlayer != null) {
                while (!isInterrupted) {
                    post_player_seekBar?.progress = mediaPlayer.currentPosition
                }
            }
        } catch (e: Exception) {
            Log.e("ProgressBarThread", e.message.toString())
        }
    }
}

