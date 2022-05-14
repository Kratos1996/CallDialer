package com.artixtise.richdialer.api

import com.artixtise.richdialer.base.SENDER_ID
import com.artixtise.richdialer.data.call.model.RichCallDataGetResponse
import com.artixtise.richdialer.data.call.model.RichCallDataResponse
import com.artixtise.richdialer.data.call.model.uplodeImage.UplodeImageResponse
import com.squareup.okhttp.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.io.File
import java.util.*


interface ApiInterface {

    @FormUrlEncoded
    @POST("CallRequest.php")
    suspend fun richCallDataSave(
        @Field(SENDER_ID) senderUserId: String,
        @Field("senderName") senderUsername: String,
        @Field("textMsg") textMsg: String,
        @Field("emoji") emoji: String,
        @Field("gif") gif: String,
        @Field("image") image: String,
        @Field("lat") latitude: String,
        @Field("lng") longitude: String,
        @Field("instagramId") instagramId: String,
        @Field("facebookId") facebookId: String,
        @Field("twitterId") twitterId: String,
        @Field("linkedID") linkedID: String,
        @Field("simNumber") simNumber: String,
        @Field("isRichcall") isRichcall: String,
        @Field("receiverName") receiverName: String,
        @Field("receiverUserId") receiverUserId: String,
        @Field("receiverDeveiceId") receiverDeveiceId: String,
    ):RichCallDataResponse

    @FormUrlEncoded
    @POST("GetRichcallData.php")
    suspend fun getRichCallData( @Field(SENDER_ID) senderId: String): RichCallDataGetResponse


    @Multipart
    @POST("CallRequestImage.php")
    suspend fun UplodeImage(
        @Part(SENDER_ID) userId: RequestBody,
        @Part("image\"; filename=\"image.png\" ") file: RequestBody
    ): UplodeImageResponse

}