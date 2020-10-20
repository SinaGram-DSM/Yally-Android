package com.sinagram.yallyandroid.Home.ViewModel

import android.util.Log
import androidx.lifecycle.*
import com.sinagram.yallyandroid.Home.Data.HomeRepository
import com.sinagram.yallyandroid.Home.Data.Listening
import com.sinagram.yallyandroid.Home.Data.Post
import com.sinagram.yallyandroid.Home.Data.PostsResponse
import com.sinagram.yallyandroid.Network.Result
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TimeLineViewModel : ViewModel() {
    private val repository = HomeRepository()
    val notPageLiveData: MutableLiveData<String> = MutableLiveData()
    val successLiveData: MutableLiveData<List<Post>> = MutableLiveData()

    fun getTimeLineItem(page: Int) {
        viewModelScope.launch {
            val result = repository.getMainTimeLine(page)

            if (result is Result.Success) {
                timeLineSuccess(result)
            } else {
                Log.e("LoginViewModel", (result as Result.Error).exception)
            }
        }
    }

    private fun timeLineSuccess(result: Result.Success<PostsResponse>) {
        if (result.code == 200) {
            successLiveData.postValue(result.data?.posts ?: listOf())
        } else {
            notPageLiveData.postValue("페이지가 더이상 없습니다.")
        }
    }

    fun clickYally(post: Post): LiveData<Boolean> {
        return liveData {
            val isSuccess = withContext(viewModelScope.coroutineContext) {
                if (post.isYally) {
                    when (repository.cancelYally(post.id)) {
                        is Result.Success -> true
                        is Result.Error -> false
                    }
                } else {
                    when (repository.doYally(post.id)) {
                        is Result.Success -> true
                        is Result.Error -> false
                    }
                }
            }
            emit(isSuccess)
        }
    }

    fun getListeningList(): LiveData<List<Listening>> {
        return liveData {
            val isSuccess = withContext(viewModelScope.coroutineContext) {
                when (val result = repository.getListeningList()) {
                    is Result.Success -> result.data!!.listeners
                    is Result.Error -> listOf()
                }
            }

            emit(isSuccess)
        }
    }
}