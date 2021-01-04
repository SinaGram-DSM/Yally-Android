package com.sinagram.yallyandroid.Profile.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.sinagram.yallyandroid.Network.Result
import com.sinagram.yallyandroid.Profile.Data.*
import okhttp3.MultipartBody

class ProfileViewModel : ViewModel() {
    private val repository = ProfileRepository()
    val listenLiveData: MutableLiveData<ListenList> = MutableLiveData<ListenList>()
    val setLiveData: MutableLiveData<User> = MutableLiveData()
    val timeLineData: MutableLiveData<List<Post>> = MutableLiveData()
    val notPageLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val successLiveData: MutableLiveData<List<Post>> = MutableLiveData()

    fun setProfile() {
        viewModelScope.launch {
            val result = repository.setProfile()

            if (result is Result.Success) {
                setProfileSuccess(result)
            } else {
                Log.e("LoginViewModel", (result as Result.Error).exception)
            }
        }
    }

    private fun setProfileSuccess(result: Result.Success<User>){
        if(result.code == 200){
            val data = result.data
            if(data != null){
                setLiveData.postValue(data)
            }
        }
    }

    fun getListening() {
        viewModelScope.launch {
            val result = repository.getListening()
            if (result is Result.Success) {
                setListenSuccess(result)
            } else {
                Log.e("profileViewModel", (result as Result.Error).exception)
            }
        }
    }

    fun getListener() {
        viewModelScope.launch {
            val result = repository.getListener()
            if (result is Result.Success) {
                setListenSuccess(result)
                Log.e("profileViewModel","getListener")
            } else {
                Log.e("profileViewModel", (result as Result.Error).exception)
            }
        }
    }

    private fun setListenSuccess(result: Result.Success<ListenList>){
        if(result.code == 200) {
            val data = result.data
            if(data != null){
                listenLiveData.postValue(data)

                Log.e("profileViewModel","listen liveData insert")
            }
        }
    }

    fun setMyTimeLine() {
        viewModelScope.launch {
            val result = repository.setMyTimeLine()
            if(result is Result.Success){
                setTimeLineSuccess(result)
            }
        }
    }

    private fun setTimeLineSuccess(result: Result.Success<Posts>){
        if(result.code == 200) {
            val data = result.data
            if(data != null) {
                timeLineData.postValue(data.posts)

                Log.e("profileViewModel","timeLine liveData insert")
            }
        }
    }

    fun modifyProfile(nickname: MultipartBody.Part, imagePart: MultipartBody.Part) {
        viewModelScope.launch {
            Log.e("profileViewModel", nickname.toString())

            val result = repository.modifyProfile(nickname, imagePart)

            if (result is Result.Success) {
                Log.e("profileViewModel", "modifySuccess")
            } else {
                Log.e("profileViewModel", (result as Result.Error).exception)
            }
        }
    }

    fun getTimeLineItem(page: Int) {
        viewModelScope.launch {
            val result = repository.setMyTimeLine()

            if (result is Result.Success) {
                timeLineSuccess(result)
            } else {
                Log.e("TimeLineViewModel", (result as Result.Error).exception)
            }
        }
    }

    private fun timeLineSuccess(result: Result.Success<Posts>) {
        if (result.code == 200) {
            val data = result.data?.posts
            if (data?.isNotEmpty() == true) {
                successLiveData.postValue(result.data.posts)
            } else {
                notPageLiveData.postValue(true)
            }
        }
    }
}