package com.artixtise.richdialer.brodcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.artixtise.richdialer.base.ACCEPT_CALL
import com.artixtise.richdialer.base.DECLINE_CALL
import com.artixtise.richdialer.presentation.managers.CallManager

class CallActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val bundle: Bundle? = intent.extras
        val telecomID = bundle!!.getString("telecomID")
        when (intent.action) {
            ACCEPT_CALL -> CallManager.accept(telecomID!!)
            DECLINE_CALL -> CallManager.reject(telecomID!!)
        }
    }
}
