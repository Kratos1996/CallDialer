package com.artixtise.richdialer.simple_phone

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.telecom.Call
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.artixtise.richdialer.R
import com.artixtise.richdialer.application.ErrorMessage.SOME_THIING_WRONG
import com.artixtise.richdialer.base.BaseActivity
import com.artixtise.richdialer.database.roomdatabase.tables.RichCallData
import com.artixtise.richdialer.databinding.ActivityCallBinding
import com.artixtise.richdialer.domain.model.contact.RichCallSealed
import com.artixtise.richdialer.mapper.RichCallApiToDatabase
import com.artixtise.richdialer.presentation.ui.activity.userProfile.viewmodel.ProfileViewmodel
import com.bumptech.glide.Glide
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.coroutines.flow.collect
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

class CallActivity : BaseActivity() {

    private lateinit var binding: ActivityCallBinding
    private val disposables = CompositeDisposable()
    private lateinit var number: String
    private var richCallHistory = RichCallData()
    var i_am_receiver = false
    var onHold = false
    var numberMain = ""
    private val viewmodel: ProfileViewmodel by viewModels()
    var recorder: MediaRecorder? = null
    var audiofile: File? = null
    private var recordstarted = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_call)
        number = intent.data!!.schemeSpecificPart

    }


    override fun onStart() {
        super.onStart()
        binding.hold.setOnClickListener {
            if(onHold){
                binding.hold.setImageResource(R.drawable.hold_off)
                onHold=false
                OngoingCall.onUnHold()
            }else{
                binding.hold.setImageResource(R.drawable.hold_on)
                onHold=true
                OngoingCall.onHold()
            }
        }
        binding.answer.setOnClickListener {
            OngoingCall.answer()
        }

        binding.hangup.setOnClickListener {
            richCallHistory.callEndTime = System.currentTimeMillis()
            viewModel!!.insertRichCallHistory(richCallHistory)
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
        if (state == Call.STATE_RINGING) {
            i_am_receiver = true
            binding.callingType.setText("Ringing Mobile")
        } else if (state == Call.STATE_DIALING) {
            i_am_receiver = false
            binding.callingType.setText("Calling Mobile")
        } else if (state == Call.STATE_ACTIVE) {
            binding.callingType.setText("Active")
        } else if (state == Call.STATE_CONNECTING) {
            binding.callingType.setText("Connecting...")
        } else if (state == Call.STATE_DISCONNECTED) {
            binding.callingType.setText("Disconnected")
        } else if (state == Call.STATE_CONNECTING) {
            binding.callingType.setText("Connecting...")
        } else if (state == Call.STATE_DISCONNECTING) {
            binding.callingType.setText("DisConnecting...")
        } else if (state == Call.STATE_HOLDING) {
            binding.callingType.setText("On Hold ")
        } else if (state == Call.REJECT_REASON_DECLINED) {
            binding.callingType.setText("Rejected")
        }
        binding.callInfo.text = "${state.asString().toLowerCase().capitalize()}\n$number"
        binding.greenLinear.isVisible = state == Call.STATE_RINGING
        binding.llEnd.isVisible = state in listOf(
            Call.STATE_DIALING,
            Call.STATE_RINGING,
            Call.STATE_ACTIVE
        )
        if (number.startsWith("+")) {
            numberMain = number.replace("+", "")
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
                        viewmodel.deletRichCall(it.response.data!!.senderUserId!!)
                        richCallHistory = RichCallApiToDatabase.convert(richCallHistory, it.response)
                        richCallHistory.receiverNumber = numberMain
                        if (i_am_receiver) {
                            richCallHistory.callType = "INCOMING"
                        } else {
                            richCallHistory.callType = "OUTGOING"
                        }
                        // richCallHistory.senderNumber=sharedPre.userMobile!!
                        viewModel!!.insertRichCallHistory(richCallHistory)
                        if (state == Call.STATE_RINGING) {
                            binding.callInfo.setText(it.response.data?.senderUserId)
                            binding.userName.setText(it.response.data?.senderName)
                        } else if (state == Call.STATE_DIALING) {
                            binding.callInfo.setText(it.response.data?.receiverUserId)
                            binding.userName.setText(it.response.data?.receiverName)
                        }
                        binding.isRichCallData.visibility = View.VISIBLE
                        if (it.response.data?.instagramId.isNullOrBlank()) {
                            binding.instaAccount.visibility = View.GONE
                        } else {
                            binding.instaAccount.visibility = View.VISIBLE
                        }
                        if (it.response.data?.twitterId.isNullOrBlank()) {
                            binding.twitterAccount.visibility = View.GONE
                        } else {
                            binding.twitterAccount.visibility = View.VISIBLE
                        }
                        if (it.response.data?.linkedID.isNullOrBlank()) {
                            binding.linkedIdAccount.visibility = View.GONE
                        } else {
                            binding.linkedIdAccount.visibility = View.VISIBLE
                        }
                        if (it.response.data?.facebookId.isNullOrBlank()) {
                            binding.facebookAccount.visibility = View.GONE
                        } else {
                            binding.facebookAccount.visibility = View.VISIBLE
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
                        if (!it.response.data!!.gif.isNullOrBlank()) {
                            Glide.with(this@CallActivity).load(it.response.data!!.gif)
                                .into(binding.ivCallerImage)
                            binding.ivCallerImage.visibility = View.VISIBLE
                        } else if (!it.response.data!!.image.isNullOrBlank()) {
                            val imageArray = convertStringToByteArray(it.response.data!!.image!!)
                            val bitmap = BitmapFactory.decodeByteArray(imageArray, 0, imageArray.size)
                            Glide.with(this@CallActivity).load(bitmap).into(binding.ivCallerImage)
                            binding.ivCallerImage.visibility = View.VISIBLE
                        } else {
                            binding.ivCallerImage.visibility = View.GONE
                        }

                        binding.callInfo.setText(it.response.data?.receiverUserId)
                    }
                    is RichCallSealed.GetRichCalldata.Error -> {
                        showCustomAlert("Error on Fetching RichCallData", binding.root)
                    }
                }
            }
        }
        lifecycleScope.launchWhenCreated {
            viewmodel.deletRichCallMutable.collect{
                when (it) {
                    is RichCallSealed.DeleteRichCall.Loading -> {

                    }
                    is RichCallSealed.DeleteRichCall.Error ->{
                        showCustomAlert(SOME_THIING_WRONG, binding.root)

                    }                    }
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

    private fun startRecording() {
        val sampleDir = File(Environment.getExternalStorageDirectory(), "/RichCallDialer")
        if (!sampleDir.exists()) {
            sampleDir.mkdirs()
        }
        val file_name = "RichCall" + System.currentTimeMillis()
        try {
            audiofile = File.createTempFile(file_name, ".amr", sampleDir)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val path = Environment.getExternalStorageDirectory().absolutePath
        recorder = MediaRecorder()
        //                          recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
        recorder!!.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION)
        recorder!!.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB)
        recorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        recorder!!.setOutputFile(audiofile!!.getAbsolutePath())
        try {
            recorder!!.prepare()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        recorder!!.start()
        recordstarted = true
    }

    private fun stopRecording() {
        if (recordstarted) {
            recorder!!.stop()
            recordstarted = false
        }
    }
    private fun Long.toDurationString() =
        String.format("%02d:%02d:%02d", this / 3600, (this % 3600) / 60, (this % 60))
}
