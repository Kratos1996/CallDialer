package com.artixtise.richdialer.presentation.ui.activity.login.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.artixtise.richdialer.R
import com.artixtise.richdialer.application.ErrorMessage.PLEASE_WAIT
import com.artixtise.richdialer.base.BaseFragment
import com.artixtise.richdialer.data.profile.model.UserAccessData
import com.artixtise.richdialer.databinding.FragmentRegistrationBinding
import com.artixtise.richdialer.domain.model.login.LoginSealed
import com.artixtise.richdialer.presentation.ui.activity.home.HomeActivity
import com.artixtise.richdialer.presentation.ui.activity.login.LoginActivity
import com.artixtise.richdialer.presentation.ui.activity.login.viewmodel.LoginViewmodel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.simplemobiletools.dialer.login.fragments.VerifyOtpFragment
import kotlinx.coroutines.delay

class RegistrationFragment : BaseFragment(R.layout.fragment_registration) {
    private lateinit var binding: FragmentRegistrationBinding
    private var token: String? = null

    companion object {

        var Instance: RegistrationFragment? = null
        var viewModel: LoginViewmodel? = null
        fun newInstance(viewmodel: LoginViewmodel): RegistrationFragment? {
            viewModel = viewmodel
            if (Instance == null) {
                Instance = RegistrationFragment()
            }
            return Instance
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_registration, container, false)
        generateFCMToken()
        WorkStation()
        getAllScopes()
        return binding.root
    }

    private fun getAllScopes() {
        lifecycleScope.launchWhenCreated {
            viewModel!!.registerState.collect {
                when (it) {
                    is LoginSealed.RegisterUserState.Loading -> {
                        (requireActivity() as LoginActivity).showLoadingDialog(PLEASE_WAIT)!!
                            .show()
                    }
                    is LoginSealed.RegisterUserState.Success -> {
                        (requireActivity() as LoginActivity).showLoadingDialog(it.response)!!
                            .dismiss()
                        showCustomAlert(it.response, binding.root)
                        delay(2000)
                        startActivity(Intent(requireActivity(), HomeActivity::class.java))
                        requireActivity().finish()
                    }
                    is LoginSealed.RegisterUserState.Error -> {
                        (requireActivity() as LoginActivity).showLoadingDialog(it.response)!!
                            .dismiss()
                        showCustomAlert(it.response, binding.root)
                    }
                }
            }
        }

    }

    private fun generateFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(
                    "FCM Token Failed!!",
                    "Fetching FCM registration token failed",
                    task.exception
                )
                return@OnCompleteListener
            }
            // Get new FCM registration token
            token = task.result
            getSharedPre().setFirebaseToken(token?:"")
            // Log and toast

        })
    }

    override fun WorkStation() {
        binding.btnSignup.setOnClickListener {
            when {
                binding.etFirstName.text.isNullOrEmpty() -> {
                    showCustomAlert("Please enter full name", binding.rootContainer)
                }
                binding.etLastName.text.isNullOrEmpty() -> {
                    showCustomAlert("Please enter last name", binding.rootContainer)
                }
                binding.etEmail.text.isNullOrEmpty() -> {
                    showCustomAlert("Please enter email name", binding.rootContainer)
                }
                else -> {
                    val data = UserAccessData(
                            name = binding.etFirstName.text.toString() + " " + binding.etLastName.text.toString(),
                            gender = "",
                            email = binding.etEmail.text.toString(),
                            userId = getSharedPre().userId?:"",
                            mobile = getSharedPre().userMobile?:"",
                            mobile2 = "",
                            "",
                            lastUpdatedProfile= System.currentTimeMillis(),
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            token?:""

                        )
                    saveData(data)
                }
            }
        }
    }

    private fun saveData(data: UserAccessData) {
        viewModel?.saveUserData(data)
    }


}
