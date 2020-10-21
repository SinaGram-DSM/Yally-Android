package com.sinagram.yallyandroid.Home.Data

import android.media.MediaPlayer
import android.widget.SeekBar
import com.sinagram.yallyandroid.Home.View.ProgressBarThread
import com.sinagram.yallyandroid.Util.SeekBarChangeListenerImpl
import java.text.SimpleDateFormat
import java.util.*

class PostMediaPlayer(private val postSeekBar: SeekBar) {
    var isClickedPost = false
    var mediaPlayer = MediaPlayer()
    var isPlaying = false

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
                ProgressBarThread(postSeekBar, mediaPlayer, isClickedPost).start()
            }
        })
    }

    fun startMediaPlayer(postData: Post) {
        mediaPlayer.setDataSource(postData.sound)
        mediaPlayer.isLooping = true
        mediaPlayer.start()

        val duration = mediaPlayer.duration
        postSeekBar.max = duration
        ProgressBarThread(postSeekBar, mediaPlayer, isClickedPost).start()
        isPlaying = true
    }

    fun stopMediaPlayer() {
        isPlaying = false
        mediaPlayer.stop()
        mediaPlayer.release()
        postSeekBar.progress = 0
    }

    fun getSoundSourceLength(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.duration)
    }
}