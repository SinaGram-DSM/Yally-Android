package com.sinagram.yallyandroid.Writing.ViewModel

import android.media.MediaRecorder
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sinagram.yallyandroid.Writing.Data.WritingRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import okhttp3.RequestBody
import com.sinagram.yallyandroid.Network.Result
import okhttp3.MultipartBody
import java.lang.Exception

class WritingViewModel : ViewModel() {
    private val repository = WritingRepository()
    var mediaRecorder: MediaRecorder? = null
    private var havefile = false

    fun writing(hashMap:HashMap<String, RequestBody>, contentPart: MultipartBody.Part, imgPartBody: MultipartBody.Part,soundPartBody: MultipartBody.Part) {
        viewModelScope.launch {
            Log.e("WritingViewModel",hashMap.toString())


            val result = repository.writing(hashMap, contentPart, imgPartBody, soundPartBody)

            if (result is Result.Success) {
                Log.e("WritingViewModel", "writing Success")
            } else {
                Log.e("WritingViewModel", (result as Result.Error).exception)
            }
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