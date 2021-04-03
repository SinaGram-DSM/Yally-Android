package com.sinagram.yallyandroid.Writing.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sinagram.yallyandroid.Writing.Data.WritingRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import okhttp3.RequestBody
import com.sinagram.yallyandroid.Network.Result
import com.sinagram.yallyandroid.Writing.MediaRecorderManager
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.lang.Exception

class WritingViewModel : ViewModel() {
    private val repository = WritingRepository()
    private var havefile = false
    private val recorderManager = MediaRecorderManager()

    fun writing(soundFile: File, imageFile: File, getContent: String) {
        val sound = changeSound(soundFile)
        val image = changeImage(imageFile)
        val content = changeContent(getContent)
        val hashtag = changeHashtag(getContent)

        viewModelScope.launch {
            val result = repository.writing(hashtag, content, image, sound)

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
                        recorderManager.startRecorder(filepath)
                    }
                }
                if (!havefile && recordingTask.isCancelled) {
                    stopRecording()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun stopRecording() {
        recorderManager.stopRecorder()
    }

    private fun changeSound(soundFile: File): MultipartBody.Part {
        return MultipartBody.Part.createFormData("sound", soundFile.name,
                soundFile.asRequestBody("multipart/form-data".toMediaTypeOrNull()))

    }

    private fun changeImage(imageFile: File): MultipartBody.Part {
        return MultipartBody.Part.createFormData("img", imageFile.name,
                imageFile.asRequestBody("multipart/form-data".toMediaTypeOrNull()))
    }

    private fun changeContent(content: String): MultipartBody.Part {
        return MultipartBody.Part.createFormData("content", content)
    }

    private fun changeHashtag(text: String): HashMap<String, RequestBody> {
        val str = text
        var isCharacter = false
        var index = 0
        val res = mutableListOf<Char>()
        var requestHashMap: HashMap<String, RequestBody> = hashMapOf()

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

        for(i in result.indices) {
            requestHashMap["hashtag[$i]"] =
                    result[i].toRequestBody("multipart/form-data".toMediaTypeOrNull())
        }
        return requestHashMap
    }
}