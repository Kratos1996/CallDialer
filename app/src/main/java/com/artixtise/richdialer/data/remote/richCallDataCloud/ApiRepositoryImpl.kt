package com.artixtise.richdialer.data.remote.richCallDataCloud

import com.artixtise.richdialer.api.ApiInterface
import com.artixtise.richdialer.data.call.model.RichCallDataGetResponse
import com.artixtise.richdialer.data.call.model.RichCallDataResponse
import javax.inject.Inject

class ApiRepositoryImpl @Inject constructor(
    private val api: ApiInterface
) : ApiRepository {

    override suspend fun getRichCallData(id: String): RichCallDataGetResponse {
        return api.getRichCallData(senderId = id)
    }

    override suspend fun SetDataOnServer( emoji: String,
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
}