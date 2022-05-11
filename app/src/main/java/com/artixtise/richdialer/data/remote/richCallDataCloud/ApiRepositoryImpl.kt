package com.artixtise.richdialer.data.remote.richCallDataCloud

import android.util.Log
import com.artixtise.richdialer.api.ApiInterface
import com.artixtise.richdialer.data.call.model.RichCallDataGetResponse
import com.artixtise.richdialer.data.call.model.RichCallDataResponse
import com.artixtise.richdialer.utility.Utility
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
        val map = hashMapOf(
            Utility.EMOJI to emoji,
            Utility.IMAGE to image,
            Utility.LAT to lat,
            Utility.LNG to lng,
            Utility.TEXT_MSG to text_msg,
            Utility.SENDER_ID to senderId,
            Utility.SENDER_NAME to senderName,
            Utility.GIF to gif,
            Utility.INSTAGRAM_ID to instaId,
            Utility.FACEBOOK_ID to fbId,
            Utility.TWITTER_ID to twitterId,
            Utility.LINKED_ID to linkedId,
            Utility.SIM_NUMBER to simNumber,
            Utility.IS_RICH_CALL to isRichCall,
            Utility.RECEIVER_NAME to receiverName,
            Utility.RECEIVER_ID to receiverId,
            Utility.RECEIVER_DEVEICE_ID to receiverDeveiceId
        )
        Log.e("HashmapData",map.toString())
        return api.richCallDataSave(map)
    }
}