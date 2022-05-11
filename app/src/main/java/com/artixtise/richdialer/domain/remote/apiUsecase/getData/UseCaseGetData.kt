package com.artixtise.richdialer.domain.remote.apiUsecase.getData

import com.artixtise.richdialer.api.Resource
import com.artixtise.richdialer.data.call.model.RichCallDataGetResponse
import com.artixtise.richdialer.data.call.model.RichCallDataResponse
import com.artixtise.richdialer.database.roomdatabase.tables.RichCallData
import kotlinx.coroutines.flow.Flow

interface UseCaseGetData {
    operator fun invoke(senderId: String): Flow<Resource<RichCallDataGetResponse>>
}