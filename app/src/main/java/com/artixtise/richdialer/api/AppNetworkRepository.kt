package com.artixtise.richdialer.api

import android.content.Context
import com.artixtise.richdialer.utility.Utility
import retrofit2.Response

class AppNetworkRepository(private val context: Context) : BaseDataSource() {


    var apiService = ApiInterface.createRichCalDialer()


    fun saveRichCallData(
        emoji: String,
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
        receiverDeveiceId:String
    ) = performOperation {
        getResult {
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
            apiService.richCallDataSave(map)
        }
    }

    fun getRichCallData(senderUserId:String) =performOperation {
        getResult {
            val map = hashMapOf(
                Utility.SENDER_ID to senderUserId
            )
            apiService.getRichCallData(map)
        }
    }

//    fun getOccupation() = performOperation {
//        getResult {
//            apiService.getOccupation()
//        }
//    }

//    fun getLatLngFromAddress(address: String,zip: String) = performOperation {
//        getResult {
//            val map = hashMapOf(
//                AppConstance.ADDRESS to address,
//                AppConstance.PINCODE to zip
//            )
//            apiService.getLatLngFromAddress(map)
//        }
//    }

}