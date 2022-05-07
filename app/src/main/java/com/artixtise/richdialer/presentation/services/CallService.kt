package com.artixtise.richdialer.presentation.services

import android.content.Intent
import android.net.Uri
import android.telecom.Call
import android.telecom.Call.Details.PROPERTY_CONFERENCE
import android.telecom.InCallService
import com.artixtise.richdialer.presentation.managers.CallManager
import com.artixtise.richdialer.presentation.ui.activity.calling.CallingActivity
import java.util.*
import kotlin.collections.HashMap

class CallService : InCallService() {
    private var normalizedNumber: String? = null
   
    override fun onCallAdded(call: Call) {
        val uniqueId:String = UUID.randomUUID().toString()
        super.onCallAdded(call)
        var uri= call!!.details.handle
        if(uri!=null){
            /* For now this is only for India numbers, will have to have
            this changed.
             */
            normalizedNumber=Uri.decode(uri.toString())
            normalizedNumber= normalizedNumber!!.substringAfter("+91:")

        }else{
            /* For instances when the calling number/name is masked, need a better way to handle this */
            normalizedNumber="12345"

        }

        if(CallManager.callMap==null) {
            CallManager.callMap = HashMap<String, Call>()
        }
        if(CallManager.callServiceMap==null) {
            CallManager.callServiceMap = HashMap<String, CallService>()
        }
        if(CallManager.callIdentfierMap==null) {
            CallManager.callIdentfierMap = HashMap<Call, String>()
        }
        CallManager.callMap?.put(uniqueId, call)
        CallManager.callServiceMap?.put(uniqueId, this)
        CallManager.callIdentfierMap?.put(call, uniqueId)

        if(CallManager.isConferenceCall && call.details.hasProperty(PROPERTY_CONFERENCE)) {
            CallManager.activeConferenceCall = call
            CallManager.activeConferenceIdentfier = uniqueId
          /*  CallActivity.handleConferenceAdd(call, uniqueId)
            val intent = Intent(this, CallActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            startActivity(intent)*/
        }else if(CallManager.isConferenceCall /* && !call.details.hasProperty(PROPERTY_CONFERENCE)*/) {
           if(call.parent==CallManager.activeConferenceCall && (call.state != Call.STATE_DIALING || call.state != Call.STATE_CONNECTING)) {
                /*  don't do anything, as phone manufacturers disconnect existing individual calls and
                * re-instate all childen of the conference call */

            }else if(CallManager.foregroundCall!=null && CallManager.backgroundCall!=null){
                CallManager.reject(uniqueId)
            } else {
              // CallingActivity.handleAddActive(uniqueId, normalizedNumber!!)
                val intent = Intent(this, CallingActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                startActivity(intent)
            }
        }else if(!CallManager.isConferenceCall && call.parent==null) {
            if(CallManager.foregroundCall!=null && CallManager.backgroundCall!=null) {
                CallManager.reject(uniqueId)
            }else if(CallManager.foregroundCall!=null && call.state == Call.STATE_RINGING) {
             //   CallingActivity.handleIncomingActive(uniqueId, normalizedNumber!!)
            }
            else if(CallManager.foregroundCall!=null && CallManager.backgroundCall==null){
              //  CallingActivity.handleAddActive(uniqueId, normalizedNumber!!)
                val intent = Intent(this, CallingActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                startActivity(intent)
            }else {
                val intent = Intent(this, CallingActivity::class.java)
                intent.putExtra("callingNumber", normalizedNumber)
                intent.putExtra("telecomCallID", uniqueId)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }

      /*  CallManager.call = call
        CallManager.inCallService = this */
    }

    override fun onCallRemoved(call: Call) {
        super.onCallRemoved(call)
        var destroyhandlestring:String? = null
        var destroyedHandle = call.details!!.handle
        if(destroyedHandle!=null) {
             destroyhandlestring = destroyedHandle.toString()
        }
        var identifier: String? = null
        identifier = CallManager.callIdentfierMap!!.get(call)
        if(call!=null && CallManager.callMap!!.containsKey(identifier)) CallManager.callMap!!.remove(identifier)
        if(call!=null && CallManager.callServiceMap!!.containsKey(identifier)) CallManager.callServiceMap!!.remove(identifier)
        if(call!=null && CallManager.callIdentfierMap!!.containsKey(call)){
            CallManager.callIdentfierMap!!.remove(call)
        }

       /* CallManager.call = null
        CallManager.inCallService = null */
    }

}
