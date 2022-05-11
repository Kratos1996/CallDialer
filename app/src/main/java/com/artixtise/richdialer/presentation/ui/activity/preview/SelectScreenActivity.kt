package com.artixtise.richdialer.presentation.ui.activity.preview

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.telecom.TelecomManager
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.artixtise.richdialer.R
import com.artixtise.richdialer.api.BaseDataSource
import com.artixtise.richdialer.base.BaseActivity
import com.artixtise.richdialer.custom.RichCallFragment
import com.artixtise.richdialer.database.roomdatabase.tables.ContactList
import com.artixtise.richdialer.database.roomdatabase.tables.RichCallData
import com.artixtise.richdialer.databinding.ActivitySelectScreenBinding
import com.artixtise.richdialer.mapper.UserAccessToProfile
import com.artixtise.richdialer.presentation.ui.activity.home.fragments.FavouriteFragment
import com.artixtise.richdialer.presentation.ui.activity.home.fragments.rich_call_fragments.*
import com.artixtise.richdialer.presentation.ui.activity.userProfile.viewmodel.ProfileViewmodel
import com.artixtise.richdialer.utility.Utility
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

class SelectScreenActivity : BaseActivity() {
    private lateinit var binding: ActivitySelectScreenBinding
    lateinit var richCallFragment: RichCallFragment
    val viewmodel: ProfileViewmodel by viewModels()
    lateinit var data: ContactList
    var richCallData =RichCallData()
    val richCallId by lazy { System.currentTimeMillis() }
    var richCallDataObj=com.artixtise.richdialer.database.roomdatabase.tables.RichCallData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_screen)
        data = intent?.getParcelableExtra<ContactList>("FAVDATA")!!
        richCallData.id=richCallId
        init()
        initViews()
        initObserver()
        setupTabIcons()
    }

    private fun initViews() {
        binding.initiateCal.setOnClickListener {
            selectSim()
        }
        binding.backNow.setOnClickListener {
            finish()
        }
        binding.ivClearUrl.setOnClickListener {
            binding.webLay.visibility=View.GONE
            richCallData.webUrl=""
        }
        binding.ivClearText.setOnClickListener {
            binding.messageLay.visibility=View.GONE
            richCallData.textMsg=""
        }
    }

    private fun initObserver() {
        viewmodel!!.getMyProfile().observe(this) {senderProfile ->
            if (senderProfile != null) {
                if(senderProfile.facebookVisible)binding.swFacebook.isChecked=true else false
                if(senderProfile.twitterVisible)binding.swTwitter.isChecked=true else false
                if(senderProfile.linkedInVisible)binding.swLinked.isChecked=true else false
                if(senderProfile.websiteVisible)binding.swUrl.isChecked=true else false
                if(senderProfile.instagramVisible)binding.swInsta.isChecked=true else false
                if(senderProfile.websiteVisible)binding.urlPreview.setText(senderProfile.websiteUrl) else ""
                binding.swUrl.setOnClickListener {
                    if(binding.swUrl.isChecked){
                        binding.webLay.visibility=View.VISIBLE
                        richCallData.webUrl=senderProfile.websiteUrl
                    }else{
                        binding.webLay.visibility=View.GONE
                        richCallData.webUrl=""
                    }
                }
                binding.swInsta.setOnClickListener {
                    if(binding.swInsta.isChecked){
                        richCallData.instaID=senderProfile.instagramUrl
                    }else{
                        richCallData.instaID=""
                    }
                }
                binding.swFacebook.setOnClickListener {
                    if(binding.swFacebook.isChecked){
                        richCallData.fbID=senderProfile.facebookUrl
                    }else
                    {
                        richCallData.fbID=""
                    }
                }
                binding.swTwitter.setOnClickListener {
                    if(binding.swTwitter.isChecked){
                        richCallData.webUrl=senderProfile.twitterUrl
                    }else{
                        richCallData.twitterID=""
                    }
                }
                binding.swLinked.setOnClickListener {
                    if(binding.swLinked.isChecked){
                        richCallData.linkedinID=senderProfile.linkedInUrl
                    }else{
                        richCallData.twitterID=""
                    }
                }
                viewModel!!.getRichCallData(richCallId).observe(this@SelectScreenActivity) {
                    if (it != null) {
                        richCallDataObj=it
                        if(!it.emoji.isNullOrBlank()){
                            binding.ivPreviewEmoji.setText(it.emoji)
                            binding.ivPreviewEmoji.visibility=View.VISIBLE
                        }else{
                            binding.ivPreviewEmoji.visibility=View.GONE

                        }
                        if(!it.textMsg.isNullOrBlank()){
                            binding.textPreview.setText(it.textMsg)
                            binding.messageLay.visibility=View.VISIBLE

                        }else{
                            binding.textPreview.setText("")
                            binding.messageLay.visibility=View.GONE
                        }
                    } else{
                         richCallData=com.artixtise.richdialer.database.roomdatabase.tables.RichCallData(
                            richCallId, receiverName = data.name
                            , receiverUserId = data.phoneNumber
                            , receiverDeviceToken =data.deviceToken
                            , isRichCall = true
                            , senderName =senderProfile.name?:""
                            , senderUserId = senderProfile.mobile?:""
                            , senderNumber = senderProfile.mobile?:""
                            , instaID = if(senderProfile.instagramVisible) senderProfile.instagramUrl else ""
                            , twitterID =if(binding.swFacebook.isChecked) senderProfile.twitterUrl else ""
                            , linkedinID=if(binding.swLinked.isChecked) senderProfile.linkedInUrl else ""
                            , fbID=if(binding.swFacebook.isChecked) senderProfile.linkedInUrl else ""
                            , webUrl=if(senderProfile.websiteVisible) senderProfile.websiteUrl else "",)
                        viewModel!!.insertRichCallHistory(richCallData)
                    }

                }
            }
        }

        //save data

        binding.apply {
            nameOfContact.setText(data.name)
            contactNumber.setText(data.phoneNumber)
        }


    }


    fun init() {
        with(binding) {
            fragmentsViewpager.adapter = FragmentsViewPagerAdapter()
            fragmentsViewpager.isUserInputEnabled = false
            TabLayoutMediator(binding.tabLayout, binding.fragmentsViewpager) { tab, position ->
                if (position == 0) {
                    tab.customView = Utility.getSelectedRichCallCustomTab(
                        this@SelectScreenActivity,
                        position,
                        layoutInflater
                    )
                } else {
                    tab.customView = Utility.getRichCallCustomTab(
                        this@SelectScreenActivity,
                        position,
                        layoutInflater
                    )
                }
            }.attach()
        }
    }

    private fun setupTabIcons() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {}

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                binding.tabLayout.getTabAt(tab!!.position)!!.customView = null
                binding.tabLayout.getTabAt(tab!!.position)!!.customView =
                    Utility.getRichCallCustomTab(
                        this@SelectScreenActivity,
                        tab!!.position,
                        layoutInflater
                    )
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.tabLayout.getTabAt(tab!!.position)!!.customView = null
                binding.tabLayout.getTabAt(tab!!.position)!!.customView =
                    Utility.getSelectedRichCallCustomTab(
                        this@SelectScreenActivity,
                        tab!!.position,
                        layoutInflater
                    )
            }
        })
    }


    inner class FragmentsViewPagerAdapter :
        FragmentStateAdapter(supportFragmentManager, lifecycle) {

        override fun getItemCount() = 5

        override fun createFragment(position: Int) = when (position) {
            0 -> EmojiFragment.newInstance(idRichcall = richCallId,
                viewModel,
                intent?.getParcelableExtra<ContactList>("FAVDATA")!!
            )!!
            1 -> GifFragment.newInstance(idRichcall = richCallId,
                viewModel,
                intent?.getParcelableExtra<ContactList>("FAVDATA")!!
            )!!
            2 -> GalleryFragment.newInstance(idRichcall = richCallId,
                viewModel,
                intent?.getParcelableExtra<ContactList>("FAVDATA")!!
            )!!
            3 -> RecentTextFragment.newInstance(idRichcall = richCallId,
                viewModel,
                intent?.getParcelableExtra<ContactList>("FAVDATA")!!
            )!!
            else -> LocationFragment.newInstance(idRichcall = richCallId,
                viewModel,
                this@SelectScreenActivity,
                intent?.getParcelableExtra<ContactList>("FAVDATA")!!
            )!!
        }

    }


    private fun selectSim() {
        Dexter.withContext(this)
            .withPermission(Manifest.permission.READ_PHONE_STATE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(permissionGrantedResponse: PermissionGrantedResponse) {
                    viewModel.isRichCall = true
                    richCallFragment = RichCallFragment.newInstance(richCallId,viewModel, data.phoneNumber)!!
                    richCallFragment.show(supportFragmentManager, "add_richcall_dialog_fragment")

                }

                override fun onPermissionDenied(permissionDeniedResponse: PermissionDeniedResponse) {
                    showCustomAlert("Need Calling  Permission", binding.root)
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissionRequest: PermissionRequest, permissionToken: PermissionToken
                ) {
                }
            }).check()

    }
}