package com.sinagram.yallyandroid.Base

import android.util.Log
import androidx.lifecycle.*
import com.sinagram.yallyandroid.Home.Data.Listening
import com.sinagram.yallyandroid.Home.Data.Post
import com.sinagram.yallyandroid.Home.Data.StateOnPostMenu
import com.sinagram.yallyandroid.Network.Result
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BasePostViewModel: ViewModel() {
    val successDeleteLiveData: MutableLiveData<Int> = MutableLiveData()
    abstract var repository: BasePostRepository

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

    fun deletePost(id: String, index: Int) {
        viewModelScope.launch {
            val result = repository.deletePost(id)

            if (result is Result.Success) {
                successDeleteLiveData.postValue(index)
            } else {
                Log.d("BasePostViewModel", (result as Result.Error).exception)
            }
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
                    Log.e("BasePostViewModel", (result as Result.Error).exception)
                    StateOnPostMenu.DELETE
                }
            }

            emit(isSuccess)
        }
    }
}