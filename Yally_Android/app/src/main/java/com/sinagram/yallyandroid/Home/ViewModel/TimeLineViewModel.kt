package com.sinagram.yallyandroid.Home.ViewModel

import android.util.Log
import androidx.lifecycle.*
import com.sinagram.yallyandroid.Home.Data.*
import com.sinagram.yallyandroid.Network.Result
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TimeLineViewModel : ViewModel() {
    private val repository = HomeRepository()
    val notPageLiveData: MutableLiveData<String> = MutableLiveData()
    val successLiveData: MutableLiveData<List<Post>> = MutableLiveData()
    val successDeleteLiveData: MutableLiveData<Int> = MutableLiveData()

    fun getTimeLineItem(page: Int) {
        viewModelScope.launch {
            val result = repository.getMainTimeLine(page)

            if (result is Result.Success) {
                timeLineSuccess(result)
            } else {
                Log.e("TimeLineViewModel", (result as Result.Error).exception)
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

    fun sendListeningToUser(state: StateOnPostMenu, email: String): LiveData<StateOnPostMenu> {
        return liveData {
            val isSuccess = withContext(viewModelScope.coroutineContext) {
                val result = if (state == StateOnPostMenu.LISTENING) {
                    repository.cancelListening(email)
                } else {
                    repository.doListening(email)
                }

                if (result is Result.Success) {
                    if (state == StateOnPostMenu.LISTENING) {
                        StateOnPostMenu.UNLISTENING
                    } else {
                        StateOnPostMenu.LISTENING
                    }
                } else {
                    Log.e("TimeLineViewModel", (result as Result.Error).exception)
                    StateOnPostMenu.DELETE
                }
            }

            emit(isSuccess)
        }
    }

    fun deletePost(id: String, index: Int) {
        viewModelScope.launch {
            val result = repository.deletePost(id)

            if (result is Result.Success) {
                successDeleteLiveData.postValue(index)
            } else {
                Log.d("TimeLineViewModel", (result as Result.Error).exception)
            }
        }
    }
}