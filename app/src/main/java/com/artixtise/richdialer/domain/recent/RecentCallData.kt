package com.artixtise.richdialer.domain.recent

import java.lang.Long
import java.util.*

data class RecentCallData( var phNumber :String?="", // mobile number
                           val callType  :String?="", // call type
                           val callDate :String?="" ,// call date
                           val callDayTime:String?="",
                           val callDuration :String?="",
                           val callDataType :String?="",//incoming,outgoing,missedCall
)
