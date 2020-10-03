package com.sinagram.yallyandroid.Sign.Data

import com.sinagram.yallyandroid.Base.BaseRepository
import com.sinagram.yallyandroid.Network.Result
import com.sinagram.yallyandroid.Network.YallyConnector

class SignRepository : BaseRepository() {
    suspend fun doLogin(body: HashMap<String, String>): Result<TokenResponse> {
        return MappingToResult { YallyConnector.createAPI().doLogin(body) }
    }

    suspend fun sendAuthCode(body: HashMap<String, String>): Result<Void> {
        return MappingToResult { YallyConnector.createAPI().sendAuthCode(body) }
    }

    suspend fun confirmAuthCode(body: HashMap<String, String>): Result<Void> {
        return MappingToResult { YallyConnector.createAPI().confirmAuthCode(body) }
    }

    suspend fun doSignUp(body: SignUpRequest): Result<Void> {
        return MappingToResult { YallyConnector.createAPI().doSignUp(body) }
    }

    fun putToken(tokenResponse: TokenResponse) {
        sharedPreferences.accessToken = tokenResponse.acessToken
        sharedPreferences.refreshToken = tokenResponse.refreshToken
    }

    fun putLoginInfo(isLogin: Boolean) {
        sharedPreferences.isLogin = isLogin
    }
}