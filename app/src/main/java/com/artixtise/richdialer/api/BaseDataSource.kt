package com.artixtise.richdialer.api

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException

@Suppress("IMPLICIT_CAST_TO_ANY")
abstract class BaseDataSource {

    fun convertToRequestBody(params: Map<String, Any>): RequestBody? {
        var requestBody: RequestBody? = null
        try {
            requestBody = Gson().toJson(params)
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return requestBody
    }

    protected suspend fun <T> getResult(tag: String = "", call: suspend () -> Response<T>): Resource<T> {
        Log.d("BASE", "getResult: ")
        try {
            val response = call.invoke()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return Resource.success(tag, body)
            }
            return if (response.errorBody() != null) {
                val e = if (response.errorBody() != null) {
                    JSONObject(response.errorBody()!!.string()).getString("message")
                } else
                    "${response.code()} ${response.message()}"
                error(tag, e)
            } else {
                error(tag, " ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            return error(tag, e.message ?: e.toString())
        }
    }

    private fun <T> error(tag: String, msg: String?): Resource<T> {
        return Resource.error(tag, msg)
    }

    fun <T> performOperation(
        tag: String = "",
        networkCall: suspend () -> Resource<T>
    ): LiveData<Resource<T?>> =
        liveData(
            Dispatchers.IO
        ) {
            try {
                Log.d("BASE", "performOperation: ")
                emit(Resource.loading(tag, data = null))
                val responseStatus = networkCall.invoke()
                if (responseStatus.status == Resource.Status.SUCCESS) {
                    emit(
                        Resource.success(
                            tag = tag,
                            data = responseStatus.data,
                            message = responseStatus.message
                        )
                    )
                } else if (responseStatus.status == Resource.Status.ERROR) {
                    emit(Resource.error(tag, responseStatus.message!!))
                }
            } catch (e: IOException) {
                emit(Resource.error<T>(tag, "Network Error !!"))
            } catch (e: Exception) {
                Log.d("BASE", "performOperation: ${e.toString()}")
                emit(Resource.error<T>(tag, e.message ?: e.toString()))
            }
        }

    data class Resource<out T>(
        val status: Status,
        val data: T?,
        val message: String?,
        val tag: String
    ) {
        companion object {
            fun <T> success(tag: String, data: T, message: String? = null): Resource<T> =
                Resource(Status.SUCCESS, data, null, tag)

            fun <T> error(tag: String, message: String?, data: T? = null): Resource<T> =
                Resource(Status.ERROR, data, message, tag)

            fun <T> loading(tag: String, data: T? = null): Resource<T> =
                Resource(Status.LOADING, data, null, tag)
        }

        enum class Status {
            SUCCESS,
            ERROR,
            LOADING
        }
    }

}

