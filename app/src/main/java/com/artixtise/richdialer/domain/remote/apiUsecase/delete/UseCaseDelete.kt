package com.artixtise.richdialer.domain.remote.apiUsecase.delete

import com.artixtise.richdialer.api.Resource
import com.artixtise.richdialer.data.call.model.RichCallDataGetResponse
import com.artixtise.richdialer.data.call.model.RichCallDataResponse
import com.artixtise.richdialer.database.roomdatabase.tables.RichCallData
import com.artixtise.richdialer.domain.model.delete.DeleteResponse
import kotlinx.coroutines.flow.Flow

interface UseCaseDelete {
    operator fun invoke(senderId: String): Flow<Resource<DeleteResponse>>
}