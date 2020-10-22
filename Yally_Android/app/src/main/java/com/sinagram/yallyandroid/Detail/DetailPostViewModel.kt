package com.sinagram.yallyandroid.Detail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sinagram.yallyandroid.Network.Result
import kotlinx.coroutines.launch

class DetailPostViewModel : ViewModel() {
    private val repository = DetailRepository()
    val successLiveData: MutableLiveData<List<Comment>> = MutableLiveData()

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
}