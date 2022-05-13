package com.artixtise.richdialer.data.remote.richCallDataCloud

import com.artixtise.richdialer.base.SENDER_ID
import com.artixtise.richdialer.data.call.model.RichCallDataGetResponse
import com.artixtise.richdialer.data.call.model.RichCallDataResponse
import com.artixtise.richdialer.data.call.model.uplodeImage.UplodeImageResponse
import retrofit2.http.Field
import java.io.File

interface ApiRepository {
    suspend fun getRichCallData(senderUserId:String): RichCallDataGetResponse
    suspend fun setDataOnServer( emoji: String,
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

    suspend fun UplodeImage( senderId: String, image: File): UplodeImageResponse

}
