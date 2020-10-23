package com.sinagram.yallyandroid.Detail.ViewModel

import android.media.MediaRecorder
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sinagram.yallyandroid.Detail.Data.Comment
import com.sinagram.yallyandroid.Detail.Data.CommentRequest
import com.sinagram.yallyandroid.Detail.Data.CommentResponse
import com.sinagram.yallyandroid.Detail.Data.DetailRepository
import com.sinagram.yallyandroid.Network.Result
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class DetailPostViewModel : ViewModel() {
    private val repository = DetailRepository()
    val successLiveData: MutableLiveData<List<Comment>> = MutableLiveData()
    val deleteCommentLiveData: MutableLiveData<Int> = MutableLiveData()
    val recorderLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private var mediaRecorder: MediaRecorder = MediaRecorder()

    fun getComments(id: String) {
        viewModelScope.launch {
            val result = repository.getCommentList(id)

            if (result is Result.Success) {
                timeLineSuccess(result)
            } else {
                Log.e("DetailPostViewModel", (result as Result.Error).exception)
            }
        }
    }

    private fun timeLineSuccess(result: Result.Success<CommentResponse>) {
        if (result.code == 200) {
            successLiveData.postValue(result.data?.comments ?: listOf())
        }
    }

    fun deleteComment(id: String, index: Int) {
        viewModelScope.launch {
            val result = repository.deleteComment(id)

            if (result is Result.Success) {
                deleteCommentLiveData.postValue(index)
            } else {
                Log.e("DetailPostViewModel", (result as Result.Error).exception)
            }
        }
    }

    fun startRecord(filePath: String) {
        viewModelScope.launch {
            val recordTask = launch {
                withTimeout(60000) {
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT)
                    mediaRecorder.setOutputFile(filePath)
                    mediaRecorder.prepare()
                    mediaRecorder.start()
                }
            }

            if (recordTask.isCancelled) {
                stopRecord()
            }
        }
    }

    fun stopRecord() {
        mediaRecorder.stop()
        mediaRecorder.release()
        recorderLiveData.value = true
    }

    fun sendComment(id: String, request: CommentRequest) {
        viewModelScope.launch {
            request.addComment()
            request.addFile()
            val result = repository.sendComment(id, request.requestHashMap)

            if (result is Result.Success) {

            } else {
                Log.e("DetailPostViewModel", (result as Result.Error).exception)
            }
        }
    }
}