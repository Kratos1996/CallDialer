package com.artixtise.richdialer.presentation.ui.activity.userProfile

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.artixtise.richdialer.R
import com.artixtise.richdialer.base.BaseActivity
import com.artixtise.richdialer.data.profile.model.UserAccessData
import com.artixtise.richdialer.databinding.ActivityProfileBinding
import com.artixtise.richdialer.presentation.ui.activity.login.viewmodel.LoginViewmodel

class ProfileActivity : BaseActivity() {
    private lateinit var binding: ActivityProfileBinding
    val loginVModel: LoginViewmodel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)
        binding.backNow.setOnClickListener {
            finish()
        }
        fetchData()
        initViews()
    }

    private fun initViews() {

        binding.update.setOnClickListener {
            val data = UserAccessData(
                binding.etName.text.toString().trim(),
                "",
                binding.etEmail.text.toString(),
                sharedPre.userId ?: "",
                binding.etPhone1.text.toString(),
                binding.etPhone2.text.toString(),
                binding.etAddress.text.toString(),
                System.currentTimeMillis(),
                "",
                binding.instagramId.text.toString(),
                binding.linkedId.text.toString(),
                binding.facebookId.text.toString(),
                binding.websiteId.text.toString(),
                binding.twitterId.text.toString(),
                sharedPre.firebaseDeviceToken ?: "",
                binding.instaCheckbox.isChecked,
                binding.linkedinCheckbox.isChecked,
                binding.facebookCheckBox.isChecked,
                binding.webCheckbox0.isChecked,
                binding.twitterCheckbox.isChecked,
            )
            updateData(data)
        }
    }

    private fun updateData(data: UserAccessData) {
        loginVModel.updateUserData(data)
    }

    fun fetchData() {
        lifecycleScope.launchWhenCreated {
            viewModel.getOtherUserId(sharedPre.userMobile!!).observe(this@ProfileActivity,
                Observer {
                    if (it != null) {
                        setData(it)
                    }

                })
        }


    }

    private fun setData(userAccessData: UserAccessData) {
        binding.data = userAccessData
    }
}