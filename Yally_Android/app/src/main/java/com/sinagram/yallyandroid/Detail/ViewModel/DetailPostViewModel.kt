package com.sinagram.yallyandroid.Detail.ViewModel

import android.media.MediaRecorder
import android.util.Log
import androidx.lifecycle.*
import com.sinagram.yallyandroid.Detail.Data.Comment
import com.sinagram.yallyandroid.Detail.Data.CommentRequest
import com.sinagram.yallyandroid.Detail.Data.CommentResponse
import com.sinagram.yallyandroid.Detail.Data.DetailRepository
import com.sinagram.yallyandroid.Home.Data.Post
import com.sinagram.yallyandroid.Network.Result
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

class DetailPostViewModel : ViewModel() {
    private val repository = DetailRepository()
    val postLiveData: MutableLiveData<Post?> = MutableLiveData()
    val successLiveData: MutableLiveData<List<Comment>> = MutableLiveData()
    val deleteCommentLiveData: MutableLiveData<Int> = MutableLiveData()
    val recorderLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private var mediaRecorder: MediaRecorder = MediaRecorder()
    private var haveFile = false

    fun getDetailPost(id: String) {
        viewModelScope.launch {
            val result = repository.getDetailPost(id)

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
            val result = repository.getCommentList(id)

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
            val result = repository.deleteComment(id)

            if (result is Result.Success) {
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
                        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
                        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT)
                        mediaRecorder.setOutputFile(filePath)
                        mediaRecorder.prepare()
                        mediaRecorder.start()
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
        mediaRecorder.stop()
        mediaRecorder.release()
        recorderLiveData.value = false
        haveFile = true
    }

    fun sendComment(id: String, request: CommentRequest) {
        viewModelScope.launch {
            if (!haveFile) {
                request.file = null
            }

            haveFile = false
            request.addComment()
            val result = repository.sendComment(id, request.requestHashMap)

            if (result is Result.Success) {
                getComments(id)
                getDetailPost(id)
            } else {
                Log.e("DetailPostViewModel", (result as Result.Error).exception)
            }
        }
    }

    fun clickYally(post: Post): LiveData<Boolean> {
        return liveData {
            val isSuccess = withContext(viewModelScope.coroutineContext) {
                val result = if (post.isYally) {
                    post.id?.let { repository.cancelYally(it) }
                } else {
                    post.id?.let { repository.doYally(it) }
                }

                result is Result.Success
            }
            emit(isSuccess)
        }
    }
}