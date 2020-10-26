package com.sinagram.yallyandroid.Util

import android.media.MediaPlayer
import android.widget.SeekBar
import android.widget.TextView
import com.sinagram.yallyandroid.Home.View.ProgressBarThread
import com.sinagram.yallyandroid.Network.YallyConnector
import java.text.SimpleDateFormat
import java.util.*

class CommentMediaPlayer(
    private val postSeekBar: SeekBar,
    private val currentTextView: TextView,
    private val endTextView: TextView
) {
    var isClickedPlay = false
    var mediaPlayer = MediaPlayer()
    var mThread: ProgressBarThread? = null

    fun setSeekBarListener() {
        postSeekBar.setOnSeekBarChangeListener(object : SeekBarChangeListenerImpl() {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                isClickedPlay = false
                mediaPlayer.pause()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val progress = seekBar?.progress
                currentTextView.text =
                    SimpleDateFormat("mm:ss", Locale.getDefault()).format(progress)
                isClickedPlay = true
                mediaPlayer.seekTo(progress!!)
                mediaPlayer.start()
                mThread =
                    ProgressBarThread(postSeekBar, mediaPlayer, isClickedPlay, currentTextView)
                mThread!!.start()
            }
        })
    }

    fun startMediaPlayer(soundURL: String) {
        mediaPlayer.setDataSource(YallyConnector.s3 + soundURL)
        mediaPlayer.isLooping = true

        mediaPlayer.setOnPreparedListener { mp ->
            mp?.start()
            val duration = mediaPlayer.duration
            postSeekBar.max = duration
            endTextView.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(duration)
            mThread = ProgressBarThread(postSeekBar, mediaPlayer, isClickedPlay, currentTextView)
            mThread!!.start()
        }
        mediaPlayer.prepareAsync()
    }

    fun pauseMediaPlayer() {
        isClickedPlay = false
        mediaPlayer.pause()
        if (mThread != null) mThread!!.interrupt()
    }

    fun restartMediaPlayer() {
        isClickedPlay = true
        mediaPlayer.start()
        mThread = ProgressBarThread(postSeekBar, mediaPlayer, isClickedPlay, currentTextView)
    }

    fun stopMediaPlayer() {
        isClickedPlay = false
        mediaPlayer.stop()
        mediaPlayer.release()
        postSeekBar.progress = 0
        if (mThread != null) {
            mThread!!.interrupt()
        }
    }
}