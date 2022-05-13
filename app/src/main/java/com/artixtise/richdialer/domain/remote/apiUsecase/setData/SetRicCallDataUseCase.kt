package com.artixtise.richdialer.domain.remote.apiUsecase.setData

import com.artixtise.richdialer.api.Resource
import com.artixtise.richdialer.data.call.model.RichCallDataResponse
import com.artixtise.richdialer.data.remote.richCallDataCloud.ApiRepository
import com.artixtise.richdialer.database.roomdatabase.tables.RichCallData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class SetRicCallDataUseCase @Inject constructor(
    private val repository: ApiRepository
):UseCaseSetData{
    override operator fun invoke(data: RichCallData
    ): Flow<Resource<RichCallDataResponse>> = flow {
        try {
            emit(Resource.Loading<RichCallDataResponse>())
            val response = repository.setDataOnServer(
                data.emoji,
                if(!data.image.isNullOrBlank()) data.image else "",
                data.lat,
                data.lng,
                data.textMsg,
                data.senderUserId,
                data.senderName,
                if(!data.gif.isNullOrBlank())data.gif else "",
                data.instaID,
                data.fbID,
                data.linkedinID,
                data.twitterID,
                data.simType,
                if(data.isRichCall) "true" else "false",
                data.receiverName,
                data.receiverNumber,
                data.receiverDeviceToken
            )
            emit(Resource.Success(response))
        } catch (e: HttpException) {
            emit(
                Resource.Error<RichCallDataResponse>(
                    e.localizedMessage ?: "An unexpected error occured"
                )
            )
        } catch (e: IOException) {
            emit(Resource.Error<RichCallDataResponse>("Couldn't reach server. Check your internet connection."))
        }
    }
}