package com.artixtise.richdialer.simple_phone

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telecom.Call
import android.view.View
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.artixtise.richdialer.R
import com.artixtise.richdialer.api.BaseDataSource
import com.artixtise.richdialer.base.BaseActivity
import com.artixtise.richdialer.databinding.ActivityCallBinding
import com.artixtise.richdialer.domain.model.contact.RichCallSealed
import com.bumptech.glide.Glide
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.coroutines.flow.collect
import java.util.concurrent.TimeUnit

class CallActivity : BaseActivity() {

    private lateinit var binding: ActivityCallBinding

    private val disposables = CompositeDisposable()

    private lateinit var number: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_call)
        number = intent.data!!.schemeSpecificPart

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
        binding.greenLinear.isVisible = state == Call.STATE_RINGING
        binding.llEnd.isVisible = state in listOf(
            Call.STATE_DIALING,
            Call.STATE_RINGING,
            Call.STATE_ACTIVE
        )
        if (number.startsWith("+")) {
            val numberMain = number.replace("+", "")
            if (state == Call.STATE_RINGING) {
                viewModel.getRichCallData(numberMain)
            } else if (state == Call.STATE_DIALING) {
                viewModel.getRichCallData(sharedPre.userMobile!!)
            }
        } else {
            val numberMain = "91" + number
            if (state == Call.STATE_RINGING) {
                viewModel.getRichCallData(numberMain)
            } else if (state == Call.STATE_DIALING) {
                viewModel.getRichCallData(sharedPre.userMobile!!)
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.getRichCallMutable.collect {
                when (it) {
                    is RichCallSealed.GetRichCalldata.Loading -> {

                    }
                    is RichCallSealed.GetRichCalldata.Success -> {
                        if (state == Call.STATE_RINGING) {
                            binding.callInfo.setText(it.response.data?.senderUserId)
                            binding.userName.setText(it.response.data?.senderName)
                        } else if (state == Call.STATE_DIALING) {
                            binding.callInfo.setText(it.response.data?.receiverUserId)
                            binding.userName.setText(it.response.data?.receiverName)
                        }
                        if (it.response.data?.image.isNullOrBlank()) {
                            binding.ivCallerImage.visibility = View.GONE
                        } else {
                            Glide.with(this@CallActivity).load(it.response.data?.image)
                                .into(binding.ivCallerImage)
                            binding.ivCallerImage.visibility = View.VISIBLE
                        }
                        binding.isRichCallData.visibility = View.VISIBLE
                        if (it.response.data?.instagramId.isNullOrBlank()) {
                            binding.instaAccount.visibility = View.GONE
                        }
                        if (it.response.data?.twitterId.isNullOrBlank()) {
                            binding.twitterAccount.visibility = View.GONE
                        }
                        if (it.response.data?.linkedID.isNullOrBlank()) {
                            binding.twitterAccount.visibility = View.GONE
                        }
                        if (it.response.data?.facebookId.isNullOrBlank()) {
                            binding.facebookAccount.visibility = View.GONE
                        }
                        if (it.response.data?.textMsg.isNullOrBlank()) {
                            binding.tvMessage.visibility = View.GONE
                        } else {
                            binding.tvMessage.setText(it.response.data?.textMsg)
                            binding.tvMessage.visibility = View.VISIBLE
                        }
                        if (it.response.data?.emoji.isNullOrBlank()) {
                            binding.tvEmoji.visibility = View.GONE
                        } else {
                            binding.tvEmoji.setText(it.response.data?.emoji)
                            binding.tvEmoji.visibility = View.VISIBLE
                        }

                        binding.callInfo.setText(it.response.data?.receiverUserId)
                    }
                    is RichCallSealed.GetRichCalldata.Error -> {
                        showCustomAlert("Error on Fetching RichCallData", binding.root)
                    }
                }
            }
        }
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

    private fun Long.toDurationString() =
        String.format("%02d:%02d:%02d", this / 3600, (this % 3600) / 60, (this % 60))
}
