package com.artixtise.richdialer.presentation.managers


import android.content.Intent
import android.telecom.Call
import android.telecom.VideoProfile
import com.artixtise.richdialer.presentation.services.CallService
import com.artixtise.richdialer.presentation.ui.activity.calling.CallingActivity

// inspired by https://github.com/Chooloo/call_manage
class CallManager {
    companion object {

         var isConferenceCall: Boolean = false
         var callMap:MutableMap<String, Call>? = null
         var callServiceMap:MutableMap<String, CallService>? = null
         var callIdentfierMap:MutableMap<Call, String>? = null
         var activeConferenceCall:Call? = null
         var activeConferenceIdentfier: String? = null
         var conferenceCallInstance: CallingActivity? = null
         var foregroundCall:Call? = null
         var backgroundCall:Call? = null

        fun accept(telecomID: String) {
            var thisCall: Call? = null
            if(telecomID!=null && callMap!!.containsKey(telecomID)){
                thisCall = callMap!!.get(telecomID)
            }
            thisCall?.answer(VideoProfile.STATE_AUDIO_ONLY)

        }

        fun getChildren():List<Call>{
            var childList:List<Call> = mutableListOf<Call>()
            if(activeConferenceCall!=null){
                childList = CallManager.activeConferenceCall!!.children
                return childList!!
            }
            return childList!!
        }


        fun reject(telecomID: String) {
            var thisCall: Call? = null
            if(telecomID!=null && callMap!!.containsKey(telecomID)) {
                thisCall = callMap!!.get(telecomID)
            }
            if (thisCall != null) {
                if (thisCall!!.state == Call.STATE_RINGING) {
                    thisCall!!.reject(false, null)
                } else {
                    thisCall!!.disconnect()
                }
            }
        }

        fun rejectCall(call: Call) {
            var thisCall:Call = call
            if(thisCall!=null) {
                thisCall.disconnect()
            }
        }

        fun registerCallback(callback: Call.Callback, telecomID:String) {
            var thisCall:Call? = null
            if(callMap!!.containsKey(telecomID)) {
                thisCall = callMap!!.get(telecomID)
            }
            if (thisCall != null) {
                thisCall!!.registerCallback(callback)
            }
        }

        fun unregisterCallback(callback: Call.Callback, telecomID: String) {
            var thisCall:Call? = null
            if(callMap!!.containsKey(telecomID)) {
                thisCall = callMap!!.get(telecomID)
            }
            if(thisCall!=null) {
                thisCall!!.unregisterCallback(callback)
            }
        }

     /*   fun getState() = if (call == null) {
            Call.STATE_DISCONNECTED
        } else {
            call!!.state
        } */

        fun getState(telecomID:String): Int{
            var thisCall:Call? = null
            if(callMap!!.containsKey(telecomID)) {
                thisCall = callMap!!.get(telecomID)
            }
            if(thisCall==null) return Call.STATE_DISCONNECTED
            else return thisCall.state
        }

        fun getIdentifier(call:Call): String {
            var thisCall:Call? = null
            var callIdentifier: String? = null
            if(call!=null) thisCall = call
            if(callIdentfierMap!!.containsKey(thisCall)) {
                callIdentifier = callIdentfierMap!!.get(thisCall)
            }
            return callIdentifier!!
        }

        fun keypad(c: Char, telecomID: String) {
            var thisCall:Call? = null
            if(telecomID!=null && callMap!!.containsKey(telecomID)) {
                thisCall = callMap!!.get(telecomID)
            }
            thisCall?.playDtmfTone(c)
            thisCall?.stopDtmfTone()
        }
        fun showConferenceGUI() {
               val intent = Intent(conferenceCallInstance, CallingActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            conferenceCallInstance!!.startActivity(intent)
        }

        fun setRoute(value :Int, telecomID: String) {
            var callService:CallService? = null
            if(telecomID!=null && callServiceMap!!.containsKey(telecomID)){
                callService = callServiceMap!!.get(telecomID)
            }
            callService!!.setAudioRoute(value)
        }

        fun setMute(value : Boolean, telecomID: String) {
            var callService:CallService? = null
            if(telecomID!=null && callServiceMap!!.containsKey(telecomID)) {
                callService = callServiceMap!!.get(telecomID)
            }
            callService!!.setMuted(value)
        }

        fun getCall(telecomID: String?): Call? {
            var thisCall:Call ? = null
            if(telecomID!=null && callMap!!.containsKey(telecomID)){
                thisCall = callMap!!.get(telecomID)

            }

            return thisCall
        }


    }
}
