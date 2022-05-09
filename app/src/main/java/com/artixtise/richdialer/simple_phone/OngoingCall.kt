package com.artixtise.richdialer.simple_phone

import android.telecom.Call
import io.reactivex.subjects.BehaviorSubject
import android.telecom.VideoProfile

object OngoingCall {
    val state: BehaviorSubject<Int> = BehaviorSubject.create()

    private val callback = object : Call.Callback() {
        override fun onStateChanged(call: Call, newState: Int) {
            //Timber.d(call.toString())
            state.onNext(newState)
        }
    }

    var call: Call? = null
        set(value) {
            field?.unregisterCallback(callback)
            value?.let {
                it.registerCallback(callback)
                state.onNext(it.state)
            }
            field = value
        }

    fun answer() {
        call!!.answer(VideoProfile.STATE_AUDIO_ONLY)
    }

    fun hangup() {
        call!!.disconnect()
    }
    fun rejectWithMessage(reject:Boolean=true,message:String=""){
        call!!.reject(reject,message)
    }
    fun onHold(){
        call!!.hold()
    }

    fun onUnHold(){
        call!!.unhold()
    }
    fun


}
