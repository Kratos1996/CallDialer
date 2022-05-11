package com.artixtise.richdialer.domain.remote.apiUsecase.setData

import com.artixtise.richdialer.api.Resource
import com.artixtise.richdialer.data.call.model.RichCallDataResponse
import com.artixtise.richdialer.database.roomdatabase.tables.RichCallData
import kotlinx.coroutines.flow.Flow

interface UseCaseSetData {
    operator fun invoke(data: RichCallData
    ): Flow<Resource<RichCallDataResponse>>
}