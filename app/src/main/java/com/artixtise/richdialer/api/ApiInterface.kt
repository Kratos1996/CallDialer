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

    @FormUrlEncoded
    @POST("CallRequest.php")
    suspend fun richCallDataSave(@Field("senderUserId") senderUserId:String,
                                 @Field("senderName") senderUsername:String,
                                 @Field("textMsg") textMsg:String,
                                 @Field("emoji") emoji:String,
                                 @Field("image") image:String,
                                 @Field("lat") latitude:String,
                                 @Field("lng") longitude:String,
                                 @Field("instagramId") instagramId:String,
                                 @Field("facebookId") facebookId:String,
                                 @Field("twitterId") twitterId:String,
                                 @Field("linkedID") linkedID:String,
                                 @Field("simNumber") simNumber:String,
                                 @Field("isRichcall") isRichcall:String,
                                 @Field("receiverName") receiverName:String,
                                 @Field("receiverUserId") receiverUserId:String,
                                 @Field("receiverDeveiceId") receiverDeveiceId:String,
    ):RichCallDataResponse

    @FormUrlEncoded
    @POST("GetRichcallData.php")
    suspend fun getRichCallData( @Field(SENDER_ID) senderId: String): RichCallDataGetResponse


}