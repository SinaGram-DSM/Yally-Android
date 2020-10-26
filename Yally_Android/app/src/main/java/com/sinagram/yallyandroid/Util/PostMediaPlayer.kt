package com.sinagram.yallyandroid.Util

import android.media.MediaPlayer
import android.widget.SeekBar
import android.widget.TextView
import com.sinagram.yallyandroid.Home.View.ProgressBarThread
import com.sinagram.yallyandroid.Network.YallyConnector
import java.text.SimpleDateFormat
import java.util.*

class PostMediaPlayer(private val postSeekBar: SeekBar, private val textView: TextView) {
    var isClickedPost = false
    var mediaPlayer: MediaPlayer? = MediaPlayer()
    var mThread: ProgressBarThread? = null

    fun setSeekBarListener() {
        postSeekBar.setOnSeekBarChangeListener(object : SeekBarChangeListenerImpl() {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                mediaPlayer?.pause()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val progress = seekBar?.progress
                mediaPlayer?.seekTo(progress!!)
                mediaPlayer?.start()
                mThread = ProgressBarThread(postSeekBar, mediaPlayer!!, isClickedPost)
                mThread!!.start()
            }
        })
    }

    fun startMediaPlayer(soundURL: String) {
        mediaPlayer = MediaPlayer()
        mediaPlayer!!.setDataSource(YallyConnector.s3 + soundURL)
        mediaPlayer!!.isLooping = true

        mediaPlayer!!.setOnPreparedListener { mp ->
            mp?.start()
            val duration = mediaPlayer!!.duration
            postSeekBar.max = duration
            textView.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(duration)
            mThread = ProgressBarThread(postSeekBar, mediaPlayer!!, isClickedPost)
            mThread!!.start()
        }
        mediaPlayer!!.prepareAsync()
    }

    fun stopMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer!!.stop()
            mediaPlayer!!.release()
        }
        mediaPlayer = null
        postSeekBar.progress = 0
        if (mThread != null) {
            mThread!!.interrupt()
        }
    }
}