package com.artixtise.richdialer.presentation.ui.activity.calling


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.artixtise.richdialer.R
import com.artixtise.richdialer.base.BaseActivity
import com.artixtise.richdialer.brodcast.MyProperties
import com.artixtise.richdialer.brodcast.PhonecallReceiver
import com.artixtise.richdialer.databinding.ActivityCallingBinding
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.*


class CallingActivity  : BaseActivity() {
    companion object {
        fun getStartIntent(context: Context): Intent {
            val openAppIntent = Intent(context, CallingActivity::class.java)
            openAppIntent.flags = Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT or Intent.FLAG_ACTIVITY_NEW_TASK
            return openAppIntent
        }
    }
    private lateinit var binding: ActivityCallingBinding
    //: BaseActivity() {
    var MetallurgTelNumber = "+917014586715"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this, R.layout.activity_calling)
        val rv = PhonecallReceiver()

        val myThread = Thread(myRunnable)
        myThread.start()
        initObserver()
    }

    private fun initObserver() {
        //fast networking
//        AndroidNetworking.get("https://fierce-cove-29863.herokuapp.com/getAllUsers/{pageNumber}")
//            .addQueryParameter("limit", "3")
//            .setTag("test")
//            .setPriority(Priority.LOW)
//            .build()
//            .getAsJSONArray(object : JSONArrayRequestListener() {
//                fun onResponse(response: JSONArray?) {
//                    // do anything with response
//                }
//
//                fun onError(error: ANError?) {
//                    // handle error
//                }
//            })
    }

    fun CallToMetallurgGates() {
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:$MetallurgTelNumber")
        startActivity(callIntent)
    }


    var myRunnable = Runnable {
        while (true) {
            try {
                Thread.sleep(50) // Waits for 1 second (1000 milliseconds)
                if (MyProperties.instance!!.NewIncomingCall) {
                    CallToMetallurgGates()
                    MyProperties.instance!!.NewIncomingCall = false
                    MyProperties.instance!!.CallId++
                    val dbg_str =
                        Integer.toString(MyProperties.instance!!.CallId) + " " + MyProperties.instance!!.PhoneNumber
                    binding.textView3.post(Runnable {
                        val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm:ss")
                        val currentDateandTime: String = sdf.format(Date())
                        binding.textView3.append(
                            """
                                  
                                  $currentDateandTime
                                  """.trimIndent()
                        )
                        binding.textView3.append(
                            """
                                  
                                  (${Integer.toString(MyProperties.instance!!.CallId)}): ${MyProperties.instance!!.PhoneNumber}
                                  """.trimIndent()
                        )
                        binding.textView3.append("\nЗвоню на ворота: $MetallurgTelNumber")
                    })
                    Thread.sleep(5000) // Waits for 1 second (1000 milliseconds)
                }
            } catch (e: InterruptedException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }
        }
    }
}