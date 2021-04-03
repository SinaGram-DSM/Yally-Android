package com.sinagram.yallyandroid.Util

import android.media.MediaPlayer
import android.widget.SeekBar
import android.widget.TextView
import com.sinagram.yallyandroid.Network.YallyConnector
import java.text.SimpleDateFormat
import java.util.*

object YallyMediaPlayer {
    var mediaPlayer: MediaPlayer? = null
    private var seekBar: SeekBar? = null
    private var endTextView: TextView? = null
    private var progressBarThread: ProgressBarThread? = null
    var func: () -> Unit = {}

    fun setViews(seekBar: SeekBar, textView: TextView) {
        stopMediaPlayer()

        this.seekBar = seekBar
        endTextView = textView
    }

    fun setInvoke(func: () -> Unit) {
        this.func = func
    }

    fun setSeekBarListener() {
        seekBar?.setOnSeekBarChangeListener(object : SeekBarChangeListenerImpl() {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                mediaPlayer?.pause()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val progress = seekBar?.progress
                mediaPlayer?.seekTo(progress!!)
                mediaPlayer?.start()
                startThread()
            }
        })
    }

    fun startThread() {
        progressBarThread = ProgressBarThread(seekBar, mediaPlayer)
        progressBarThread!!.start()
    }

    fun initMediaPlayer(soundURL: String) {
        mediaPlayer = MediaPlayer()
        mediaPlayer!!.setDataSource(YallyConnector.s3 + soundURL)
        mediaPlayer!!.isLooping = true

        mediaPlayer!!.setOnPreparedListener { player ->
            val duration = mediaPlayer!!.duration
            seekBar?.max = duration
            endTextView?.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(duration)

            progressBarThread = ProgressBarThread(seekBar, mediaPlayer!!)
            progressBarThread?.start()
            player?.start()
        }
        mediaPlayer!!.prepareAsync()
    }

    fun pauseMediaPlayer() {
        mediaPlayer?.pause()
        releaseThread()
    }

    fun restartMediaPlayer() {
        mediaPlayer?.start()
        progressBarThread = ProgressBarThread(seekBar, mediaPlayer)
        progressBarThread!!.start()
    }

    fun stopMediaPlayer() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        seekBar?.progress = 0
        releaseThread()
        func()
    }

    private fun releaseThread() {
        progressBarThread?.interrupt()
        progressBarThread = null
    }
}