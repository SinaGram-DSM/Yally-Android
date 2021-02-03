package com.sinagram.yallyandroid.Profile.Data

import com.sinagram.yallyandroid.Base.BasePostRepository
import com.sinagram.yallyandroid.Network.Result
import com.sinagram.yallyandroid.Network.YallyConnector
import okhttp3.MultipartBody

class ProfileRepository: BasePostRepository() {

    suspend fun getListening(): Result<ListenList> {
        val token = getAccessToken()
        val email = sharedPreferences.email

        return mappingToResult {
            YallyConnector.createAPI().getListening(token!!, email)
        }
    }

    suspend fun getListener(): Result<ListenList> {
        val token = getAccessToken()
        val email = sharedPreferences.email

        return mappingToResult {
            YallyConnector.createAPI().getListener(token!!, email)
        }
    }

    suspend fun setProfile(): Result<User> {
        val token = getAccessToken()
        val email = sharedPreferences.email

        return mappingToResult {
            YallyConnector.createAPI().setProfile(token!!, email)
        }
    }

    suspend fun setMyTimeLine(): Result<Posts> {
        val token = getAccessToken()
        val email = sharedPreferences.email

        return mappingToResult {
            YallyConnector.createAPI().setMyTimeLine(token!!, email,5)
        }
    }

    suspend fun modifyProfile(nickname: MultipartBody.Part, imagePart: MultipartBody.Part): Result<Void> {
        val token = getAccessToken()

        return mappingToResult {
            YallyConnector.createAPI().modifyProfile(token!!, nickname,imagePart)
        }
    }

}