package com.artixtise.richdialer.presentation.ui.activity.home

import android.Manifest
import android.R.attr.button
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.telecom.TelecomManager
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.artixtise.richdialer.R
import com.artixtise.richdialer.base.BaseActivity
import com.artixtise.richdialer.databinding.ActivityMainBinding
import com.artixtise.richdialer.mapper.UserAccessToProfile
import com.artixtise.richdialer.presentation.ui.activity.home.fragments.*
import com.artixtise.richdialer.presentation.ui.activity.userProfile.ProfileActivity
import com.artixtise.richdialer.utility.Utility
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.messaging.FirebaseMessaging
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener


class HomeActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this, R.layout.activity_main)
        getOnlineProfile()
    }


    override fun onResume() {
        super.onResume()
        Dexter.withContext(this)
            .withPermission(Manifest.permission.READ_CONTACTS)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(permissionGrantedResponse: PermissionGrantedResponse) {
                    viewModel.getContacts()
                }
                override fun onPermissionDenied(permissionDeniedResponse: PermissionDeniedResponse) {
                    showCustomAlert("Need Contact Permission", binding.root)
                }
                override fun onPermissionRationaleShouldBeShown(permissionRequest: PermissionRequest, permissionToken: PermissionToken
                ) {} }).check()
        init()
        startCallingSer()
        generateFCMToken()
        initViews()
        setupTabIcons()
    }
    private fun startCallingSer() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
           // val serviceIntent = Intent(applicationContext, HeadsUpNotificationService::class.java)
          // ContextCompat.startForegroundService(applicationContext, serviceIntent)
        }
    }

    private fun generateFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM Token Failed!!", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            // Log and toast
            Log.d("FCM Token Success!!", token)
            sharedPre.setFirebaseToken(token)
            viewModel.UpdateToken(token)
            Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
        })
    }

    private fun initViews() {
        binding.toolbar.settingSection.setOnClickListener {
            showPopup(it)
        }
    }

    private fun showPopup(view: View) {
        val popup = PopupMenu(this, view)
        popup.inflate(R.menu.pop_up)
        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->
            when (item!!.itemId) {
                R.id.account ->{
                    startActivity(Intent(baseContext,ProfileActivity::class.java))
                }
            }
            true
        })

        popup.show()
    }
    fun init() {
        with(binding) {

            fragmentsViewpager.adapter = FragmentsViewPagerAdapter()
            fragmentsViewpager.isUserInputEnabled = false
            TabLayoutMediator(binding.tabLayout, binding.fragmentsViewpager) { tab, position ->
                if (position == 0){
                    tab.customView = Utility.getSelectedCustomTab(this@HomeActivity, position,layoutInflater)
                } else{
                    tab.customView = Utility.getCustomTab(this@HomeActivity,position,layoutInflater)
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
                    Utility.getCustomTab(this@HomeActivity, tab!!.position,layoutInflater)
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.tabLayout.getTabAt(tab!!.position)!!.customView = null
                binding.tabLayout.getTabAt(tab!!.position)!!.customView =
                    Utility.getSelectedCustomTab(this@HomeActivity, tab!!.position,layoutInflater)
            }
        })
    }

    fun getOnlineProfile() {
        viewModel.getMyProfile().observe(this) {
            if (it== null) {
                lifecycleScope.launchWhenCreated {
                    viewModel.getOtherUserId(sharedPre.userMobile!!).observe(this@HomeActivity) { user ->
                        if (user != null) {
                            viewModel.insertMyProfile(UserAccessToProfile.convert(user))
                        }else{

                        }
                    }
                }

            }
        }

    }
    inner class FragmentsViewPagerAdapter :
        FragmentStateAdapter(supportFragmentManager, lifecycle) {

        override fun getItemCount() = 5

        override fun createFragment(position: Int) = when (position) {
            0 -> FavouriteFragment.newInstance(viewmodel = viewModel)!!
            1 -> RecentFragment.newInstance(viewmodel = viewModel)!!
            2 -> ContactFragment.newInstance(viewmodel = viewModel)!!
            3 -> AddOnFragment()
            else -> ConfrenceFragment()
        }

    }

}