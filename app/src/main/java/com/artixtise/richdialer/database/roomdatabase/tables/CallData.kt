package com.artixtise.richdialer.database.roomdatabase.tables

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigInteger

@Entity
class CallData {
    @PrimaryKey(autoGenerate = true)
    var callingId: Long = 0L
    var callStartTime: Long = 0L
    var callPickupTime: Long = 0L
    var callEndTime: Long = 0L
    var callerName :String=""
    var receiverProfile :String=""
    var callerPhoneNumber :String=""
    var callType :String=""
    var isRichCall :Boolean=false
    var isConfrenceCall :Boolean=false
    var isRichVideoCall :Boolean=false
    var fromSim1 :Boolean=false
    var fromSim2 :Boolean=false
    var missedCall :Boolean=false
    var callFrom :String=""
    var callTo :String=""
    var simData :String=""
    var missedCallCount :Int=0

}