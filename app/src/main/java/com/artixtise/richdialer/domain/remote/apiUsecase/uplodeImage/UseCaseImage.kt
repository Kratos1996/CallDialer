package com.artixtise.richdialer.domain.remote.apiUsecase.uplodeImage

import com.artixtise.richdialer.api.Resource
import com.artixtise.richdialer.data.call.model.RichCallDataResponse
import com.artixtise.richdialer.data.call.model.uplodeImage.UplodeImageResponse
import com.artixtise.richdialer.database.roomdatabase.tables.RichCallData
import kotlinx.coroutines.flow.Flow
import java.io.File

interface UseCaseImage {
    operator fun invoke(senderId:String,image:File
    ): Flow<Resource<UplodeImageResponse>>
}