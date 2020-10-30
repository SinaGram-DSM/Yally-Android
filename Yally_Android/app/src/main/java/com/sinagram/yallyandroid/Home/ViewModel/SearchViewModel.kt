package com.sinagram.yallyandroid.Home.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sinagram.yallyandroid.Home.Data.*
import com.sinagram.yallyandroid.Network.Result
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val repository = SearchRepository()
    val recomendListLivedata: MutableLiveData<List<Friend>> = MutableLiveData()

    fun getRecomendedList() {
        viewModelScope.launch {
            var friendList: Result<FriendResponse>? = null
            var listenList: Result<ListeningResponse>? = null
            val friendJob = launch { friendList = repository.getFriendList() }
            val listenJob = launch { listenList = repository.getListeningList() }

            joinAll(friendJob, listenJob)

            if (friendList is Result.Success<FriendResponse> && listenList is Result.Success<ListeningResponse>) {
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
                for (j in list.indices) {
                    if (data[i].nickname == list[j].nickname) {
                        data[i].isListening = true
                    }
                }
            }
        }

        return data
    }
}