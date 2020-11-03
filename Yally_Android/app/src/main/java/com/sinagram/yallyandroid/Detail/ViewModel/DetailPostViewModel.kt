package com.sinagram.yallyandroid.Detail.ViewModel

import android.media.MediaRecorder
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sinagram.yallyandroid.Base.BasePostRepository
import com.sinagram.yallyandroid.Base.BasePostViewModel
import com.sinagram.yallyandroid.Detail.Data.Comment
import com.sinagram.yallyandroid.Detail.Data.CommentRequest
import com.sinagram.yallyandroid.Detail.Data.CommentResponse
import com.sinagram.yallyandroid.Detail.Data.DetailRepository
import com.sinagram.yallyandroid.Home.Data.Post
import com.sinagram.yallyandroid.Network.Result
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class DetailPostViewModel : BasePostViewModel() {
    override var repository: BasePostRepository = DetailRepository()
    val postLiveData: MutableLiveData<Post?> = MutableLiveData()
    val successLiveData: MutableLiveData<List<Comment>> = MutableLiveData()
    val deleteCommentLiveData: MutableLiveData<Int> = MutableLiveData()
    val recorderLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private var mediaRecorder: MediaRecorder? = null
    private var haveFile = false

    fun getDetailPost(id: String) {
        viewModelScope.launch {
            val result = (repository as DetailRepository).getDetailPost(id)

            if (result is Result.Success) {
                postSuccess(result)
            } else {
                Log.e("DetailPostViewModel", (result as Result.Error).exception)
            }
        }
    }

    private fun postSuccess(result: Result.Success<Post>) {
        if (result.code == 200) {
            postLiveData.postValue(result.data)
        }
    }

    fun getComments(id: String) {
        viewModelScope.launch {
            val result = (repository as DetailRepository).getCommentList(id)

            if (result is Result.Success) {
                commentSuccess(result)
            } else {
                Log.e("DetailPostViewModel", (result as Result.Error).exception)
            }
        }
    }

    private fun commentSuccess(result: Result.Success<CommentResponse>) {
        if (result.code == 200) {
            successLiveData.postValue(result.data?.comments ?: listOf())
        }
    }

    fun deleteComment(id: String, index: Int) {
        viewModelScope.launch {
            val result = (repository as DetailRepository).deleteComment(id)

            if (result is Result.Success && result.code == 204) {
                deleteCommentLiveData.postValue(index)
            } else {
                Log.e("DetailPostViewModel", (result as Result.Error).exception)
            }
        }
    }

    fun startRecord(filePath: String) {
        try {
            viewModelScope.launch {
                val recordTask = launch {
                    withTimeout(10000) {
                        mediaRecorder = MediaRecorder()
                        mediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
                        mediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                        mediaRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT)
                        mediaRecorder!!.setOutputFile(filePath)
                        mediaRecorder!!.prepare()
                        mediaRecorder!!.start()
                        recorderLiveData.postValue(true)
                    }
                }

                if (!haveFile && recordTask.isCancelled) {
                    stopRecord()
                }
            }
        } catch (e: Exception) {
            Log.e("DetailPostViewModel", e.message.toString())
        }
    }

    fun stopRecord() {
        if (mediaRecorder != null) {
            mediaRecorder!!.stop()
            mediaRecorder!!.release()
            recorderLiveData.value = false
            haveFile = true
        }
        mediaRecorder = null
    }

    fun sendComment(id: String, request: CommentRequest) {
        viewModelScope.launch {
            if (!haveFile) request.file = null

            haveFile = false
            request.addComment()
            request.addFile()
            val result = (repository as DetailRepository).sendComment(id, request.requestHashMap)

            if (result is Result.Success && result.code == 201) {
                getComments(id)
                getDetailPost(id)
            } else {
                Log.e("DetailPostViewModel", (result as Result.Error).exception)
            }
        }
    }
}