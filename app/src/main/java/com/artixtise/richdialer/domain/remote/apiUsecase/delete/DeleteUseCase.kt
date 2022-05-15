package com.artixtise.richdialer.domain.remote.apiUsecase.delete

import com.artixtise.richdialer.api.Resource
import com.artixtise.richdialer.data.call.model.RichCallDataGetResponse
import com.artixtise.richdialer.data.remote.richCallDataCloud.ApiRepository
import com.artixtise.richdialer.domain.model.delete.DeleteResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class DeleteUseCase @Inject constructor(
    private val repository: ApiRepository
) :UseCaseDelete{
    override operator fun invoke(senderId: String): Flow<Resource<DeleteResponse>> = flow {
        try {
            emit(Resource.Loading<DeleteResponse>())
            val response = repository.deleteRichCall(senderId)
            emit(Resource.Success<DeleteResponse>(response))
        } catch(e: HttpException) {
            emit(Resource.Error<DeleteResponse>(e.localizedMessage ?: "An unexpected error occured"))
        } catch(e: IOException) {
            emit(Resource.Error<DeleteResponse>("Couldn't reach server. Check your internet connection."))
        }
    }
}