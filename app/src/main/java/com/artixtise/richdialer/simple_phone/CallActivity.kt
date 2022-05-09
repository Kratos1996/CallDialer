package com.artixtise.richdialer.simple_phone

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telecom.Call
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.artixtise.richdialer.R
import com.artixtise.richdialer.api.BaseDataSource
import com.artixtise.richdialer.base.BaseActivity
import com.artixtise.richdialer.databinding.ActivityCallBinding
import com.bumptech.glide.Glide
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import java.util.concurrent.TimeUnit

class CallActivity : BaseActivity() {

    private lateinit var binding: ActivityCallBinding

    private val disposables = CompositeDisposable()

    private lateinit var number: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this, R.layout.activity_call)
        number = intent.data!!.schemeSpecificPart
        initObserver()
    }

    private fun initObserver() {
        viewModel.getRichCallData(
            "7014586715"
        ).observe(this, Observer {
            when (it.status) {
                BaseDataSource.Resource.Status.LOADING -> {}
                BaseDataSource.Resource.Status.SUCCESS -> {
                    binding.apply {
                        it.data?.data.let {
                            textView2.text = "Text message : ${it!!.textMsg}"
                            callInfo.text = it.simNumber
                            if (it.gif.isNullOrEmpty()){
                                Glide.with(this@CallActivity).load(it.image).into(ivCallerImage)
                            }
                            else{
                               tvEmoji.text = getEmoji(it.emoji!!.toInt())
                            }
                        }
                    }
                    showCustomAlert(it.data!!.data.textMsg,binding.root)

                }
                BaseDataSource.Resource.Status.ERROR -> {
                    showCustomAlert(it.data!!.Message,binding.root)
                }
            }
        })
    }

    fun getEmoji(unicode: Int): String {
        return String(Character.toChars(unicode))
    }

    override fun onStart() {
        super.onStart()

        binding.answer.setOnClickListener {
            OngoingCall.answer()
        }

        binding.hangup.setOnClickListener {
            OngoingCall.hangup()
        }

        OngoingCall.state
            .subscribe(::updateUi)
            .addTo(disposables)

        OngoingCall.state
            .filter { it == Call.STATE_DISCONNECTED }
            .delay(1, TimeUnit.SECONDS)
            .firstElement()
            .subscribe { finish() }
            .addTo(disposables)
    }

    @SuppressLint("SetTextI18n")
    private fun updateUi(state: Int) {
        binding.callInfo.text = "${state.asString().toLowerCase().capitalize()}\n$number"

        binding.answer.isVisible = state == Call.STATE_RINGING
        binding.hangup.isVisible = state in listOf(
            Call.STATE_DIALING,
            Call.STATE_RINGING,
            Call.STATE_ACTIVE
        )
    }

    override fun onStop() {
        super.onStop()
        disposables.clear()
    }

    companion object {
        fun start(context: Context, call: Call) {
            Intent(context, CallActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .setData(call.details.handle)
                .let(context::startActivity)
        }
    }
}
