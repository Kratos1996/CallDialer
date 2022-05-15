package com.artixtise.richdialer.data.remote.richCallDataCloud

import com.artixtise.richdialer.data.call.model.RichCallDataGetResponse
import com.artixtise.richdialer.data.call.model.RichCallDataResponse
import com.artixtise.richdialer.data.call.model.uplodeImage.UplodeImageResponse
import com.artixtise.richdialer.domain.model.delete.DeleteResponse
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
    suspend fun deleteRichCall( senderId: String): DeleteResponse

}
