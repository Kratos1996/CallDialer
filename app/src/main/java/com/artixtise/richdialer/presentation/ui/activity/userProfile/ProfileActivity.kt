package com.artixtise.richdialer.presentation.ui.activity.userProfile


import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.artixtise.richdialer.R
import com.artixtise.richdialer.application.ErrorMessage
import com.artixtise.richdialer.application.ErrorMessage.ERROR_ON_GETTING_USER
import com.artixtise.richdialer.base.BaseActivity
import com.artixtise.richdialer.data.profile.model.UserAccessData
import com.artixtise.richdialer.databinding.ActivityProfileBinding
import com.artixtise.richdialer.domain.model.login.LoginSealed
import com.artixtise.richdialer.mapper.UserAccessToProfile
import com.artixtise.richdialer.presentation.ui.activity.userProfile.viewmodel.ProfileViewmodel


class ProfileActivity : BaseActivity() {
    private lateinit var binding: ActivityProfileBinding
    val viewmodel: ProfileViewmodel by viewModels()

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
                binding.etName.text.toString(),
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
                deviceToken = sharedPre.firebaseDeviceToken ?: "",
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
        viewmodel.updateUserData(data)
    }

    fun fetchData() {
        viewmodel.getMyProfile().observe(this) {
            if (it != null) {
                setData(UserAccessToProfile.reverce(it))
                binding.emptyData.visibility=View.GONE
            } else {
                getOnlineProfile()
                binding.emptyData.visibility=View.VISIBLE
            }
        }
        lifecycleScope.launchWhenCreated {
            viewmodel.registerState.collect {
                when (it) {
                    is LoginSealed.RegisterUserState.Loading -> {
                        showLoadingDialog(ErrorMessage.PLEASE_WAIT)!!
                            .show()
                    }
                    is LoginSealed.RegisterUserState.Success -> {
                        showLoadingDialog(it.response)!!
                            .dismiss()
                        showCustomAlert(it.response, binding.root)
                    }
                    is LoginSealed.RegisterUserState.Error -> {
                        showLoadingDialog(it.response)!!.dismiss()
                        showCustomAlert(it.response, binding.root)
                    }
                }
            }
        }
    }

    fun getOnlineProfile() {
        lifecycleScope.launchWhenCreated {
            viewmodel.getOtherUserId(sharedPre.userMobile!!).observe(this@ProfileActivity) { user ->
                if (user != null) {
                    viewmodel.insertMyProfile(UserAccessToProfile.convert(user))
                    binding.emptyData.visibility=View.GONE
                }else{
                    binding.emptyData.visibility=View.VISIBLE
                }
            }
        }

    }

    private fun setData(userAccessData: UserAccessData) {
        binding.data = userAccessData
    }
}