package com.sinagram.yallyandroid.Home.View

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
    private val mediaPlayer: MediaPlayer,
    private val isPlaying: Boolean,
    private val currentTime: TextView? = null
) : Thread() {
    var currentPosition = 0

    override fun run() {
        try {
            while (isPlaying && !isInterrupted) {
                currentPosition = mediaPlayer.currentPosition
                post_player_seekBar.progress = currentPosition
                handler.sendEmptyMessage(0)
            }
        } catch (e: Exception) {
            Log.e("ProgressBarThread", e.message.toString())
        }
    }

    private var handler: Handler =
        @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message) {
                if (msg.what == 0) {
                    currentTime?.text =
                        SimpleDateFormat("mm:ss", Locale.getDefault()).format(currentPosition)

                    if (isPlaying) sendEmptyMessageDelayed(0, 1000)
                }


            }
        }
}

