package com.sinagram.yallyandroid.Util

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Handler
import android.os.Message
import android.util.Log
import android.widget.SeekBar
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class ProgressBarThread(
    private val post_player_seekBar: SeekBar,
    private val mediaPlayer: MediaPlayer?,
) : Thread() {
    override fun run() {
        try {
            if (mediaPlayer != null) {
                while (!isInterrupted) {
                    post_player_seekBar.progress = mediaPlayer.currentPosition
//                    handler.sendEmptyMessage(0)
                }
            }
        } catch (e: Exception) {
            Log.e("ProgressBarThread", e.message.toString())
        }
    }
}

