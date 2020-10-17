package com.sinagram.yallyandroid.Writing.Data

import com.sinagram.yallyandroid.Base.BaseRepository
import com.sinagram.yallyandroid.Network.Result
import com.sinagram.yallyandroid.Network.YallyConnector


class WritingRepository : BaseRepository() {

    suspend fun writing(part: WritingRequest): Result<Void> {
        return mappingToResult {
            YallyConnector.createAPI().writing(part)
        }
    }
}