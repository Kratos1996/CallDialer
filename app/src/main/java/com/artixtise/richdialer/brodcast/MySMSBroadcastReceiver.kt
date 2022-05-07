package com.artixtise.richdialer.brodcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import java.util.regex.Pattern

class MySMSBroadcastReceiver(val otpReceiveListener: OTPReceiveListener) : BroadcastReceiver() {



    override fun onReceive(context: Context, intent: Intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
            val extras = intent.extras
            if (extras != null) {
                val status = extras[SmsRetriever.EXTRA_STATUS] as Status?
                if (status != null) when (status.statusCode) {
                    CommonStatusCodes.SUCCESS -> {
                        // Get SMS message contents
                        val message = extras[SmsRetriever.EXTRA_SMS_MESSAGE] as String?
                        if (message != null) {
                            val pattern = Pattern.compile("(\\d{6})")
                            //   \d is for a digit
                            //   {} is the number of digits here 6.
                            val matcher = pattern.matcher(message)
                            var `val`: String? = ""
                            if (matcher.find()) {
                                `val` = matcher.group(0) // 4 digit number
                                if (otpReceiveListener != null) otpReceiveListener!!.onOTPReceived(
                                    `val`
                                )
                            } else {
                                if (otpReceiveListener != null) otpReceiveListener!!.onOTPReceived(
                                    null
                                )
                            }
                        }
                    }
                    CommonStatusCodes.TIMEOUT -> if (otpReceiveListener != null) otpReceiveListener!!.onOTPTimeOut()
                }
            }
        }
    }

    interface OTPReceiveListener {
        fun onOTPReceived(otp: String?)
        fun onOTPTimeOut()
    }
}