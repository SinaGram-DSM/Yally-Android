package com.sinagram.yallyandroid.Profile.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sinagram.yallyandroid.Profile.Data.ProfileRepository
import kotlinx.coroutines.launch
import com.sinagram.yallyandroid.Network.Result
import com.sinagram.yallyandroid.Profile.Data.User
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ProfileViewModel: ViewModel() {
    private val repository = ProfileRepository()

    fun setProfile(){
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

    }

    fun modifyProfile(nickname: MultipartBody.Part, imagePart:MultipartBody.Part) {
        viewModelScope.launch {
            Log.e("profileViewModel", nickname.toString())

            val result = repository.modifyProfile(nickname,imagePart)

            if(result is Result.Success){
                Log.e("profileViewModel","modifySuccess")
            } else {
                Log.e("profileViewModel",(result as Result.Error).exception)
            }
        }
    }

    fun getListening(){
        viewModelScope.launch {
            val result = repository.getListening()
        }
    }

    fun getListener(){
        viewModelScope.launch{
            val result = repository.getListener()
        }
    }

    fun getMyPost(){
        viewModelScope.launch {
            val result = repository.getMyPost()
        }
    }
}