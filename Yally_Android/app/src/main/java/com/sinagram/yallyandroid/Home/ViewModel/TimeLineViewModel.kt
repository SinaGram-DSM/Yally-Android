package com.sinagram.yallyandroid.Home.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sinagram.yallyandroid.Home.Data.HomeRepository
import com.sinagram.yallyandroid.Home.Data.Post
import com.sinagram.yallyandroid.Home.Data.PostsResponse
import com.sinagram.yallyandroid.Network.Result
import kotlinx.coroutines.launch

class TimeLineViewModel : ViewModel() {
    private val repository = HomeRepository()
    val notPageLiveData: MutableLiveData<String> = MutableLiveData()
    val successLiveData: MutableLiveData<List<Post>> = MutableLiveData()

    fun getTimeLineItem(page: Int) {
        viewModelScope.launch {
            when (val result = repository.getMainTimeLine(page)) {
                is Result.Success -> {
                    loginSuccess(result)
                }
                is Result.Error -> {
                    Log.e("LoginViewModel", result.exception)
                }
            }
        }
    }

    private fun loginSuccess(result: Result.Success<PostsResponse>) {
        if (result.code == 200) {
            successLiveData.postValue(result.data?.posts ?: listOf())
        } else {
            notPageLiveData.postValue("페이지가 더이상 없습니다.")
        }
    }
}