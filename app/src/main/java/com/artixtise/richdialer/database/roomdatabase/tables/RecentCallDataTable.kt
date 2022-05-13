package com.artixtise.richdialer.database.roomdatabase.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.lang.Long
import java.util.*
@Entity
data class RecentCallDataTable(@PrimaryKey (autoGenerate = true)val idRecentCall:Long,
                               @ColumnInfo var phNumber :String?="", // mobile number
                               @ColumnInfo val callType  :String?="", // call type
                               @ColumnInfo val callDate :String?="",// call date
                               @ColumnInfo  val callDuration :String?="",
                               @ColumnInfo val callDataType :String?="",//incoming,outgoing,missedCall
                               @ColumnInfo val name :String?="",
)
