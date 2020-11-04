package com.sinagram.yallyandroid.Home.ViewModel

import android.util.Log
import androidx.lifecycle.*
import com.sinagram.yallyandroid.Home.Data.*
import com.sinagram.yallyandroid.Network.Result
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val repository = SearchRepository()
    val recomendListLivedata: MutableLiveData<List<Friend>> = MutableLiveData()
    val findUserLiveData: MutableLiveData<List<User>> = MutableLiveData()
    val findPostLiveData: MutableLiveData<List<Post>> = MutableLiveData()
    val notPageLiveData: MutableLiveData<Boolean> = MutableLiveData()

    fun getRecomendedList() {
        viewModelScope.launch {
            var friendList: Result<FriendResponse>? = null
            var listenList: Result<ListeningResponse>? = null
            val friendJob = launch { friendList = repository.getFriendList() }
            val listenJob = launch { listenList = repository.getListeningList() }

            joinAll(friendJob, listenJob)

            if (friendList is Result.Success<FriendResponse>
                && (friendList as Result.Success<FriendResponse>).code == 200
                && listenList is Result.Success<ListeningResponse>
                && (listenList as Result.Success<ListeningResponse>).code == 200) {

                val data = distinguishIsListeningFriend(
                    (friendList as Result.Success<FriendResponse>).data?.friends,
                    (listenList as Result.Success<ListeningResponse>).data?.listenings
                )

                recomendListLivedata.postValue(data)
            }
        }
    }

    private fun distinguishIsListeningFriend(
        data: List<Friend>?,
        list: List<Listening>?
    ): List<Friend>? {

        if (data != null && list != null) {
            for (i in data.indices) {
                data[i].isListening = false
                for (j in list.indices) {
                    if (data[i].nickname == list[j].nickname) {
                        data[i].isListening = true
                    }
                }
            }
        }

        return data
    }

    fun getUserListBySearchName(name: String?) {
        if (name == null) {
            findUserLiveData.value = listOf()
        } else {
            viewModelScope.launch {
                val result = repository.getUserListBySearchName(name)

                if (result is Result.Success && result.code == 200) {
                    findUserLiveData.postValue(result.data?.users ?: listOf())
                } else {
                    Log.e("SearchViewModel", (result as Result.Error).exception)
                }
            }
        }
    }

    fun getPostListBySearchTag(tag: String?, page: Int) {
        if (tag == null) {
            findPostLiveData.value = listOf()
        } else {
            viewModelScope.launch {
                val result = repository.getPostsBySearchHashtag(tag, page)

                if (result is Result.Success && result.code == 200) {
                    if (!result.data?.posts.isNullOrEmpty()) {
                        findPostLiveData.postValue(result.data!!.posts)
                    } else {
                        notPageLiveData.postValue(true)
                    }
                } else {
                    Log.e("SearchViewModel", (result as Result.Error).exception)
                }
            }
        }
    }

    fun doListening(email: String): LiveData<Boolean> {
        return liveData {
            val result = repository.doListening(email)
            emit(result is Result.Success && result.code == 200)
        }
    }

    fun cancelListening(email: String): LiveData<Boolean> {
        return liveData {
            val result = repository.cancelListening(email)
            emit(result is Result.Success && result.code == 200)
        }
    }
}