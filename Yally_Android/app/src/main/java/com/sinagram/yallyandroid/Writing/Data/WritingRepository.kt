package com.sinagram.yallyandroid.Writing.Data

import com.sinagram.yallyandroid.Base.BaseRepository
import com.sinagram.yallyandroid.Network.Result
import com.sinagram.yallyandroid.Network.YallyConnector
import okhttp3.MultipartBody
import okhttp3.RequestBody

class WritingRepository : BaseRepository() {

    suspend fun writing(hashtag: HashMap<String, RequestBody>, content: MultipartBody.Part, image: MultipartBody.Part, sound: MultipartBody.Part): Result<Unit> {
        val token = getAccessToken()

        return mappingToResult {
            YallyConnector.createAPI().writing(token!!, hashtag, content, image, sound)
        }
    }

}