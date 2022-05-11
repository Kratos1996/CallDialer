package com.artixtise.richdialer.domain.remote.apiUsecase.getData

import com.artixtise.richdialer.api.Resource
import com.artixtise.richdialer.data.call.model.RichCallDataGetResponse
import com.artixtise.richdialer.data.remote.richCallDataCloud.ApiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetRicCallDataUseCase @Inject constructor(
    private val repository: ApiRepository
) :UseCaseGetData{
    override operator fun invoke(senderId: String): Flow<Resource<RichCallDataGetResponse>> = flow {
        try {
            emit(Resource.Loading<RichCallDataGetResponse>())
            val response = repository.getRichCallData(senderId)
            emit(Resource.Success<RichCallDataGetResponse>(response))
        } catch(e: HttpException) {
            emit(Resource.Error<RichCallDataGetResponse>(e.localizedMessage ?: "An unexpected error occured"))
        } catch(e: IOException) {
            emit(Resource.Error<RichCallDataGetResponse>("Couldn't reach server. Check your internet connection."))
        }
    }
}