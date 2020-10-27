package com.sinagram.yallyandroid.Util

import android.media.MediaPlayer
import android.widget.SeekBar
import android.widget.TextView
import com.sinagram.yallyandroid.Network.YallyConnector
import java.text.SimpleDateFormat
import java.util.*

class YallyMediaPlayer(
    private val seekBar: SeekBar,
    private val endTextView: TextView,
) {
    var mediaPlayer: MediaPlayer? = null
    var progressBarThread: ProgressBarThread? = null

    fun setSeekBarListener() {
        seekBar.setOnSeekBarChangeListener(object : SeekBarChangeListenerImpl() {
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

    fun setTimeToTextView(textView: TextView?, time: Int) {
        textView?.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)
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
            seekBar.max = duration
            setTimeToTextView(endTextView, duration)

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
        seekBar.progress = 0
        releaseThread()
    }

    fun releaseThread() {
        progressBarThread?.interrupt()
        progressBarThread = null
    }
}