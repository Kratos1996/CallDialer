package com.artixtise.richdialer.brodcast

class MyProperties protected constructor() {
    var NewIncomingCall = false
    var CallId = 0
    var PhoneNumber = ""

    companion object {
        private var mInstance: MyProperties? = null

        @get:Synchronized
        val instance: MyProperties?
            get() {
                if (null == mInstance) {
                    mInstance = MyProperties()
                }
                return mInstance
            }
    }
}