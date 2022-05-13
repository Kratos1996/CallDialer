package com.artixtise.richdialer.data.remote.richCallDataCloud

import com.artixtise.richdialer.api.ApiInterface
import com.artixtise.richdialer.data.call.model.RichCallDataGetResponse
import com.artixtise.richdialer.data.call.model.RichCallDataResponse
import com.artixtise.richdialer.data.call.model.uplodeImage.UplodeImageResponse
import com.squareup.okhttp.MediaType
import com.squareup.okhttp.RequestBody
import java.io.File
import javax.inject.Inject

class ApiRepositoryImpl @Inject constructor(private val api: ApiInterface
) : ApiRepository {

    override suspend fun getRichCallData(id: String): RichCallDataGetResponse {
        return api.getRichCallData(senderId = id)
    }

    override suspend fun setDataOnServer( emoji: String,
                                          image: String,
                                          lat: String,
                                          lng:String,
                                          text_msg:String,
                                          senderId: String,
                                          senderName: String,
                                          gif:String,
                                          instaId:String,
                                          fbId: String,
                                          linkedId: String,
                                          twitterId: String,
                                          simNumber:String,
                                          isRichCall:String,
                                          receiverName: String,
                                          receiverId:String,
                                          receiverDeveiceId:String): RichCallDataResponse {
       // return api.richCallDataSave(map)
        return api.richCallDataSave(senderId,
            senderName,
            text_msg,
            emoji,
            image,
            latitude = lat,
            longitude = lng,
            instaId,
            fbId,twitterId,linkedId,simNumber,isRichCall,receiverName,receiverId,receiverDeveiceId)
    }

    override suspend fun UplodeImage(senderId: String, image: File): UplodeImageResponse {
        val userId: RequestBody = RequestBody.create(
            MediaType.parse("text/plain"),
            senderId
        )
        val imageMain = RequestBody.create(
            MediaType.parse("multipart/form-data"),
            image
        )
       return api.UplodeImage(userId,imageMain)
    }
}