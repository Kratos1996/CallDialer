package com.artixtise.richdialer.presentation.ui.activity.calling

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telecom.Call
import androidx.databinding.DataBindingUtil
import com.artixtise.richdialer.R
import com.artixtise.richdialer.base.BaseActivity
import com.artixtise.richdialer.databinding.ActivityCallBinding
import com.artixtise.richdialer.databinding.ActivityCallReceiverBinding
import com.artixtise.richdialer.simple_phone.OngoingCall
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import java.util.concurrent.TimeUnit

class CallReceiverActivity : BaseActivity() {

    private lateinit var binding: ActivityCallReceiverBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this, R.layout.activity_call_receiver)

    }

}