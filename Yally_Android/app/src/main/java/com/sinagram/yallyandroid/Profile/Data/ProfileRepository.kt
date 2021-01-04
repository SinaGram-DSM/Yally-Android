package com.sinagram.yallyandroid.Profile.Data

import android.util.Log
import com.sinagram.yallyandroid.Base.BasePostRepository
import com.sinagram.yallyandroid.Base.BaseRepository
import com.sinagram.yallyandroid.Home.Data.Post
import com.sinagram.yallyandroid.Network.Result
import com.sinagram.yallyandroid.Network.YallyConnector
import okhttp3.MultipartBody

class ProfileRepository: BasePostRepository() {
    val testToken:String = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2MDEzNTAyNzUsIm5iZiI6MTYwMTM1MDI3NSwianRpIjoiNjM1ZTk3OWItNjczZC00ZmI5LTg3MmEtZDE2MjdjNGQyYTBlIiwiZXhwIjoxNjA5OTkwMjc1LCJpZGVudGl0eSI6ImFkbWluQGdtYWlsLmNvbSIsImZyZXNoIjpmYWxzZSwidHlwZSI6ImFjY2VzcyJ9.3fLkBFWZ9N0Cq0xGEXZzVeKjNvkqkVdREsMOJwbtzy8"
    val testEmail:String = "admin@gmail.com"
    var test: String = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2MDEzNTAyNzUsIm5iZiI6MTYwMTM1MDI3NSwianRpIjoiNjM1ZTk3OWItNjczZC00ZmI5LTg3MmEtZDE2MjdjNGQyYTBlIiwiZXhwIjoxNjA5OTkwMjc1LCJpZGVudGl0eSI6ImFkbWluQGdtYWlsLmNvbSIsImZyZXNoIjpmYWxzZSwidHlwZSI6ImFjY2VzcyJ9.3fLkBFWZ9N0Cq0xGEXZzVeKjNvkqkVdREsMOJwbtzy8"

    suspend fun getListening(): Result<ListenList> {
        val token = getAccessToken()
        val email = sharedPreferences.email
        return mappingToResult {
            YallyConnector.createAPI().getListening(testToken, testEmail)
        }
    }

    suspend fun getListener(): Result<ListenList> {
        val token = getAccessToken()
        val email = sharedPreferences.email
        return mappingToResult {
            YallyConnector.createAPI().getListener(testToken, testEmail)
        }
    }

    suspend fun setProfile(): Result<User> {
        val token = getAccessToken()
        val email = sharedPreferences.email

        return mappingToResult {
            YallyConnector.createAPI().setProfile(testToken, testEmail)
        }
    }

    suspend fun setMyTimeLine(): Result<Posts> {
        val token = getAccessToken()
        val email = sharedPreferences.email
        return mappingToResult {
            YallyConnector.createAPI().setMyTimeLine(test, testEmail,5)
        }
    }

    suspend fun modifyProfile(nickname: MultipartBody.Part, imagePart: MultipartBody.Part): Result<Void> {
        return mappingToResult {
            Log.e("profileRepository", nickname.toString())
            Log.e("profileRepository",imagePart.toString())

            YallyConnector.createAPI().modifyProfile(testToken, nickname,imagePart)
        }
    }

}