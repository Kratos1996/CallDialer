package com.artixtise.richdialer.data.remote.richCallDataCloud

import com.artixtise.richdialer.data.call.model.RichCallDataGetResponse
import com.artixtise.richdialer.data.call.model.RichCallDataResponse

interface ApiRepository {
    suspend fun getRichCallData(senderUserId:String): RichCallDataGetResponse
    suspend fun SetDataOnServer( emoji: String,
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
                                 receiverDeveiceId:String): RichCallDataResponse
}