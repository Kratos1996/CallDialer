package com.artixtise.richdialer.api

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


//    @GET("getpostofstate")
//    suspend fun getUserPost(@Body hashMap: HashMap<String,String>):Response<PostResponse>

    @POST("CallRequest.php")
    suspend fun richCallDataSave(
        @Body params:HashMap<String,String>
    ):Response<RichCallDataResponse>


    @GET("GetRichcallData.php")
    suspend fun getRichCallData(
        @Body params:HashMap<String,String>):
            Response<RichCallDataGetResponse>


//    @GET("GetRichcallData.php")
//    suspend fun sendOtp(@Query("senderUserId") senderUserId: String):
//            Response<OtpResponse>



    companion object {

        private const val BASE_URL = "https://codetesting.xyz/CallingAppApi/"

        fun createRichCalDialer(): ApiInterface {
            val httpClient = OkHttpClient().newBuilder()
                .connectTimeout(300, TimeUnit.SECONDS)
                .readTimeout(600, TimeUnit.SECONDS)
                .writeTimeout(600, TimeUnit.SECONDS)
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(AuthorizationInterceptor())
                .build()


            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ApiInterface::class.java)
        }


    }
}