package com.artixtise.richdialer.database.roomdatabase.tables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RichCallData(
    @PrimaryKey(autoGenerate = true)
    var receiverUserId: String = "",
    var senderUserId: String = "",
    var receiverName: String = "",
    var senderName: String = "",
    var receiverDeviceToken :String="",
    var emoji :String="",
    var image :String="",
    var textMsg :String="",
    var lat :String= "",
    var lng :String= "",
    var isRichCall :String= "",
    var simType :String= "",
    var receiverNumber :String= "",
    var senderNumber :String= "",
    var instaID :String="",
    var fbID :String="",
    var twitterID :String="",
    var linkedinID :String= ""

)
