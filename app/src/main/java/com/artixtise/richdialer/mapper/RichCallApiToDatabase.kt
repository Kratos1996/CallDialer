package com.artixtise.richdialer.mapper

import androidx.room.ColumnInfo
import com.artixtise.richdialer.data.call.model.RichCallDataGetResponse
import com.artixtise.richdialer.database.roomdatabase.tables.RichCallData

object RichCallApiToDatabase {
    fun convert(richCallData: RichCallData, response: RichCallDataGetResponse): RichCallData {
        richCallData.id = System.currentTimeMillis()
        richCallData.receiverUserId = response.data!!.receiverUserId!!
        richCallData.senderUserId = response.data.senderUserId!!
        richCallData.receiverName = response.data.receiverName!!
            richCallData.senderName= response.data.senderName!!
        richCallData.receiverDeviceToken= response.data.receiverDeveiceId!!
        richCallData.emoji= response.data.emoji!!
        richCallData.gif= response.data.gif!!
        richCallData.textMsg= response.data.textMsg!!
        richCallData.lat= response.data.lat!!
        richCallData.lng= response.data.lng!!
        richCallData.isRichCall= response.data.isRichcall!!.equals("true",true)
        richCallData.simType= response.data.simNumber!!
        richCallData.receiverNumber= ""
        richCallData.senderNumber= ""
        richCallData.instaID= response.data.instagramId!!
        richCallData.fbID= response.data.facebookId!!
        richCallData.twitterID= response.data.twitterId!!
        richCallData.linkedinID= response.data.linkedID!!
        richCallData.webUrl= ""
        richCallData.callStartTime = System.currentTimeMillis()
        richCallData.callEndTime=0
        richCallData.callType = "INCOMING"
        return richCallData
    }
}

