package com.artixtise.richdialer.database.roomdatabase.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RichCallData(
    @PrimaryKey(autoGenerate = false)
    var id:Long=0L,
    @ColumnInfo var receiverUserId: String = "",
    @ColumnInfo var senderUserId: String = "",
    @ColumnInfo var receiverName: String = "",
    @ColumnInfo var senderName: String = "",
    @ColumnInfo var receiverDeviceToken :String="",
    @ColumnInfo var emoji :String="",
    @ColumnInfo var image :String="",
    @ColumnInfo var gif :String="",
    @ColumnInfo var textMsg :String="",
    @ColumnInfo var lat :String= "",
    @ColumnInfo var lng :String= "",
    @ColumnInfo var isRichCall :Boolean= false,
    @ColumnInfo var simType :String= "",
    @ColumnInfo var receiverNumber :String= "",
    @ColumnInfo var senderNumber :String= "",
    @ColumnInfo var instaID :String="",
    @ColumnInfo var fbID :String="",
    @ColumnInfo var twitterID :String="",
    @ColumnInfo var linkedinID :String= "",
    @ColumnInfo var webUrl :String= "",
    @ColumnInfo var callStartTime :Long= 0L,
    @ColumnInfo var callEndTime :Long= 0L,
    @ColumnInfo var callType :String="",

)
