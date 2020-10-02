package com.sinagram.yallyandroid.Sign.Data

import com.sinagram.yallyandroid.Base.BaseRepository
import com.sinagram.yallyandroid.Network.Result
import com.sinagram.yallyandroid.Network.YallyConnector

class SignRepository : BaseRepository() {
    suspend fun doLogin(body: HashMap<String, String>): Result<Any> {
        return MappingToResult { YallyConnector.createAPI().doLogin(body) }
    }

    suspend fun sendAuthCode(body: HashMap<String, String>): Result<Any> {
        return MappingToResult { YallyConnector.createAPI().sendAuthCode(body) }
    }

    suspend fun confirmAuthCode(body: HashMap<String, String>): Result<Any> {
        return MappingToResult { YallyConnector.createAPI().confirmAuthCode(body) }
    }

    suspend fun doSignUp(body: HashMap<String, String>): Result<Any> {
        return MappingToResult { YallyConnector.createAPI().doSignUp(body) }
    }
}