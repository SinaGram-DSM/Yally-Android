package com.sinagram.yallyandroid.Writing.ViewModel

import android.media.MediaRecorder
import android.os.Environment
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sinagram.yallyandroid.Writing.Data.WritingRepository
import com.sinagram.yallyandroid.Writing.Data.WritingRequest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import java.io.File
import java.lang.Exception

class WritingViewModel : ViewModel() {
    private val repository = WritingRepository()
    var mediaRecorder: MediaRecorder? = null
    private var havefile = false

    fun writing() {

    }

    fun startRecording(filepath: String) {
        try {
            viewModelScope.launch {
                val recordingTask = launch {
                    withTimeout(10000) {
                        mediaRecorder = MediaRecorder()
                        mediaRecorder?.let {
                            it.setAudioSource(android.media.MediaRecorder.AudioSource.MIC)
                            it.setOutputFormat(android.media.MediaRecorder.OutputFormat.MPEG_4)
                            it.setAudioEncoder(android.media.MediaRecorder.AudioEncoder.DEFAULT)
                            it.setOutputFile(filepath)
                            it.prepare()
                            it.start()
                        }
                    }
                }
                if (!havefile && recordingTask.isCancelled) {
                    stopRecording()
                }
            }

        } catch (e: Exception) {
            println(e.message)
        }

    }

    fun stopRecording() {
        mediaRecorder?.let {
            it.stop()
            it.release()
            havefile = true
        }
        mediaRecorder = null
    }

    fun hashtags(text: String): MutableList<String> {
        val str = text
        var isCharacter = false
        var index = 0
        val res = mutableListOf<Char>()

        while (str.length >= index + 1) {
            if (str[index] == '#') isCharacter = true
            if (str[index] == ' ') isCharacter = false
            if (isCharacter) res.add(str[index])

            index++
        }

        val result = mutableListOf<String>()
        var count = -1
        if ('#' in res) {
            for (i in res) {
                if (i == '#') {
                    count++
                    result.add("")
                } else {
                    result[count] += i.toString()
                }
            }
        }
        return result
    }

}