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

    suspend fun writing(part: WritingRequest) {
        if (part != null) {
            repository.writing(part)
        } else {
            Log.d("writingViewModel", "part null")
        }
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

    fun hashtags(text: String): ArrayList<String> {
        var copy = text
        var result = ArrayList<String>()
        var count = 0
        var startIndex = 0

        for (element in copy) {
            if (element == '#')
                count++
        }

        for (i in 0 until count) {
            for (j in text) startIndex = text.indexOf('#') + 1

            try {
                result.add(
                    copy.substring(
                        startIndex,
                        (copy.substring(startIndex).indexOf(" ") + startIndex)
                    )
                )
                copy = copy.substring((copy.substring(startIndex).indexOf(" ") + startIndex) + 1)
            } catch (e: Exception) {
                println(e.message)
            }
        }
        return result
    }

}