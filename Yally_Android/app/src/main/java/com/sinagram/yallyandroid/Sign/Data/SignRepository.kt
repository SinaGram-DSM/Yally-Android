package com.sinagram.yallyandroid.Sign.Data

import com.sinagram.yallyandroid.Base.BaseRepository
import com.sinagram.yallyandroid.Network.Result
import com.sinagram.yallyandroid.Network.YallyConnector

class SignRepository : BaseRepository() {
    suspend fun doLogin(body: HashMap<String, String>): Result<TokenResponse> {
        return mappingToResult { YallyConnector.createAPI().doLogin(body) }
    }

    suspend fun sendAuthCode(body: HashMap<String, String>): Result<Void> {
        return mappingToResult { YallyConnector.createAPI().sendAuthCode(body) }
    }

    suspend fun confirmAuthCode(body: HashMap<String, String>): Result<Void> {
        return mappingToResult { YallyConnector.createAPI().confirmAuthCode(body) }
    }

    suspend fun doSignUp(body: SignUpRequest): Result<Void> {
        return mappingToResult { YallyConnector.createAPI().doSignUp(body) }
    }

    suspend fun sendResetCode(body: HashMap<String, String>): Result<Void> {
        return mappingToResult { YallyConnector.createAPI().sendResetCode(body) }
    }

    suspend fun changePassword(body: HashMap<String, String>): Result<Void> {
        return mappingToResult { YallyConnector.createAPI().sendResetPassword(body) }
    }

    fun putToken(tokenResponse: TokenResponse) {
        sharedPreferences.accessToken = tokenResponse.accessToken
        sharedPreferences.refreshToken = tokenResponse.refreshToken
    }

    fun putLoginInfo(isLogin: Boolean) {
        sharedPreferences.isLogin = isLogin
    }
}