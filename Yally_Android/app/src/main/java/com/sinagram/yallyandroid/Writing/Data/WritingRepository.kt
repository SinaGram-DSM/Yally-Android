package com.sinagram.yallyandroid.Writing.Data

import com.sinagram.yallyandroid.Base.BaseRepository
import com.sinagram.yallyandroid.Network.Result
import com.sinagram.yallyandroid.Network.YallyConnector
import okhttp3.MultipartBody
import okhttp3.RequestBody

class WritingRepository : BaseRepository() {


    suspend fun writing(hashMap: HashMap<String, RequestBody>, contentPartBody: MultipartBody.Part, imgPartBody: MultipartBody.Part,soundPartBody: MultipartBody.Part): Result<Void> {
        return mappingToResult {
            YallyConnector.createAPI().writing(hashMap, contentPartBody,imgPartBody,soundPartBody)
        }
    }

}