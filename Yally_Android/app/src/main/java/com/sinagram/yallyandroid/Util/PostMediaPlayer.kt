package com.sinagram.yallyandroid.Home.Data

import android.media.MediaPlayer
import android.widget.SeekBar
import com.sinagram.yallyandroid.Home.View.ProgressBarThread
import com.sinagram.yallyandroid.Network.YallyConnector
import com.sinagram.yallyandroid.Util.SeekBarChangeListenerImpl
import java.text.SimpleDateFormat
import java.util.*

class PostMediaPlayer(private val postSeekBar: SeekBar) {
    var isClickedPost = false
    var mediaPlayer = MediaPlayer()
    var isPlaying = false
    var mThread: ProgressBarThread? = null

    fun setSeekBarListener() {
        postSeekBar.setOnSeekBarChangeListener(object : SeekBarChangeListenerImpl() {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                isPlaying = false
                mediaPlayer.pause()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                isPlaying = true
                val progress = seekBar?.progress
                mediaPlayer.seekTo(progress!!)
                mediaPlayer.start()
                mThread = ProgressBarThread(postSeekBar, mediaPlayer, isClickedPost)
                mThread!!.start()
            }
        })
    }

    fun startMediaPlayer(postData: Post) {
        mediaPlayer.setDataSource(YallyConnector.s3 + postData.sound)
        mediaPlayer.isLooping = true

        mediaPlayer.setOnPreparedListener { mp ->
            mp?.start()
            val duration = mediaPlayer.duration
            postSeekBar.max = duration
            mThread = ProgressBarThread(postSeekBar, mediaPlayer, isClickedPost)
            mThread!!.start()
            isPlaying = true
        }
        mediaPlayer.prepareAsync()
    }

    fun stopMediaPlayer() {
        isPlaying = false
        mediaPlayer.stop()
        mediaPlayer.release()
        postSeekBar.progress = 0
        if (mThread != null) {
            mThread!!.interrupt()
        }
    }

    fun getSoundSourceLength(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.duration)
    }
}