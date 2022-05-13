package com.artixtise.richdialer.domain.remote.apiUsecase.uplodeImage

import com.artixtise.richdialer.api.Resource
import com.artixtise.richdialer.data.call.model.RichCallDataResponse
import com.artixtise.richdialer.data.call.model.uplodeImage.UplodeImageResponse
import com.artixtise.richdialer.data.remote.richCallDataCloud.ApiRepository
import com.artixtise.richdialer.database.roomdatabase.tables.RichCallData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.File
import java.io.IOException
import javax.inject.Inject

class UplodeImageUseCase @Inject constructor(
    private val repository: ApiRepository
):UseCaseImage{

    override fun invoke(senderId: String, image: File): Flow<Resource<UplodeImageResponse>> = flow {
        try {
            emit(Resource.Loading<UplodeImageResponse>())
            val response = repository.UplodeImage(senderId,image)
            emit(Resource.Success(response))
        } catch (e: HttpException) {
            emit(
                Resource.Error<UplodeImageResponse>(
                    e.localizedMessage ?: "An unexpected error occured"
                )
            )
        } catch (e: IOException) {
            emit(Resource.Error<UplodeImageResponse>("Couldn't reach server. Check your internet connection."))
        }
    }
}