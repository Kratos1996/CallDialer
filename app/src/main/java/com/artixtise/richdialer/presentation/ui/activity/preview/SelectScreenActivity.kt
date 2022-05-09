package com.artixtise.richdialer.presentation.ui.activity.preview

import android.Manifest
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.artixtise.richdialer.R
import com.artixtise.richdialer.api.BaseDataSource
import com.artixtise.richdialer.base.BaseActivity
import com.artixtise.richdialer.custom.RichCallFragment
import com.artixtise.richdialer.database.roomdatabase.tables.ContactList
import com.artixtise.richdialer.databinding.ActivitySelectScreenBinding
import com.artixtise.richdialer.presentation.ui.activity.home.fragments.FavouriteFragment
import com.artixtise.richdialer.presentation.ui.activity.home.fragments.rich_call_fragments.*
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_screen)

        init()
        initViews()
        initObserver()
        setupTabIcons()
    }

    private fun initViews() {
        binding.initiateCal.setOnClickListener {
            selectSim()
        }
        binding.backNow.setOnClickListener{
            finish()
        }
    }

    private fun initObserver() {
        //save data
        viewModel.saveRichCallData(
            "emoji",
            "image",
            "lat",
            "lng",
            "text_msg",
            "senderId",
            "senderName",
            "gif",
            "instaId",
            "fbId",
            "linkedId",
            "twitterId",
            "simNumber",
            "isRichCall",
            "receiverName",
            "receiverId",
            "receiverDeveiceId"
        ).observe(this, Observer {
            when (it.status) {
                BaseDataSource.Resource.Status.LOADING -> {}
                BaseDataSource.Resource.Status.SUCCESS -> {
                    showCustomAlert(it.data!!.Params.textMsg,binding.root)

                }
                BaseDataSource.Resource.Status.ERROR -> {
                    showCustomAlert(it.data!!.Message,binding.root)
                }
            }
        })

        val data: ContactList = intent?.getParcelableExtra<ContactList>("FAVDATA")!!
        binding.apply {
            nameOfContact.setText(data.name)
            contactNumber.setText(data.phoneNumber)
        }
        FavouriteFragment.viewModel!!.selectedData.observe(this) {
            binding.apply {
                clPreview.visibility = View.VISIBLE
                tvPreview.visibility = View.VISIBLE
                tvPreview.text = getEmoji(it.toInt())
            }
        }

    }

    fun getEmoji(unicode: Int): String {
        return String(Character.toChars(unicode))
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
            0 -> EmojiFragment.newInstance(
                viewModel,
                intent?.getParcelableExtra<ContactList>("FAVDATA")!!
            )!!
            1 -> GifFragment.newInstance(
                viewModel,
                intent?.getParcelableExtra<ContactList>("FAVDATA")!!
            )!!
            2 -> GalleryFragment.newInstance(
                viewModel,
                intent?.getParcelableExtra<ContactList>("FAVDATA")!!
            )!!
            3 -> RecentTextFragment.newInstance(
                viewModel,
                intent?.getParcelableExtra<ContactList>("FAVDATA")!!
            )!!
            else -> LocationFragment.newInstance(
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
                    richCallFragment = RichCallFragment.newInstance(viewModel, "")!!
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