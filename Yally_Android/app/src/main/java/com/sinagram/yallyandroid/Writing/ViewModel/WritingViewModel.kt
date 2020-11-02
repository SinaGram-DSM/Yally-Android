package com.sinagram.yallyandroid.Writing.ViewModel

import android.media.MediaRecorder
import android.util.Log
import androidx.lifecycle.ViewModel
import com.sinagram.yallyandroid.Writing.Data.WritingRepository
import com.sinagram.yallyandroid.Writing.Data.WritingRequest
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

    suspend fun startRecording(filePath: String) {
        try {
            mediaRecorder = MediaRecorder()
            mediaRecorder?.let {
                it.setAudioSource(android.media.MediaRecorder.AudioSource.MIC)
                it.setOutputFormat(android.media.MediaRecorder.OutputFormat.MPEG_4)
                it.setAudioEncoder(android.media.MediaRecorder.AudioEncoder.DEFAULT)
                it.setOutputFile(filePath)
                it.prepare()
                it.start()
            }
        }catch(e:Exception){
            println(e.message)
        }

    }

    suspend fun stopRecording(){
        mediaRecorder?.let{
            it.stop()
            it.release()
            havefile = true
        }
        mediaRecorder = null
    }

    suspend fun hashtags(text: String) {
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
                result.add(copy.substring(startIndex, (copy.substring(startIndex).indexOf(" ") + startIndex)))
                copy = copy.substring((copy.substring(startIndex).indexOf(" ") + startIndex) + 1)
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }

    private fun sendData(writing: String){

    }

}