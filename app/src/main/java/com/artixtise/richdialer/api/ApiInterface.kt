package com.artixtise.richdialer.api

import com.artixtise.richdialer.base.BASE_URL
import com.artixtise.richdialer.base.SENDER_ID
import com.artixtise.richdialer.data.call.model.RichCallDataGetResponse
import com.artixtise.richdialer.data.call.model.RichCallDataResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.*
import java.util.concurrent.TimeUnit

interface ApiInterface {

    @POST("CallRequest.php")
    suspend fun richCallDataSave(@Body params:HashMap<String,String>):RichCallDataResponse

    @FormUrlEncoded
    @GET("GetRichcallData.php")
    suspend fun getRichCallData( @Field(SENDER_ID) senderId: String): RichCallDataGetResponse


}