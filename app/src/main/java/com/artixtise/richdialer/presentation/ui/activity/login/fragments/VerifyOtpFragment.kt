package com.simplemobiletools.dialer.login.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.artixtise.richdialer.R
import com.artixtise.richdialer.application.ErrorMessage.ENTER_OTP
import com.artixtise.richdialer.application.ErrorMessage.INVALID_OTP
import com.artixtise.richdialer.base.BaseFragment
import com.artixtise.richdialer.databinding.FragmentVerifyOtpBinding
import com.artixtise.richdialer.domain.model.login.LoginModel
import com.artixtise.richdialer.domain.model.login.LoginSealed
import com.artixtise.richdialer.presentation.ui.activity.home.HomeActivity
import com.artixtise.richdialer.presentation.ui.activity.login.LoginActivity
import com.artixtise.richdialer.presentation.ui.activity.login.fragments.RegistrationFragment
import com.artixtise.richdialer.presentation.ui.activity.login.viewmodel.LoginViewmodel

class VerifyOtpFragment : BaseFragment(R.layout.fragment_verify_otp) {
    private lateinit var binding: FragmentVerifyOtpBinding

    companion object {

        var Instance: VerifyOtpFragment? = null
        var viewModel: LoginViewmodel? = null
        var modelMain: LoginModel? = null
        fun newInstance(viewmodel: LoginViewmodel,model:LoginModel): VerifyOtpFragment? {
            viewModel = viewmodel
            modelMain=model
            Log.e("State", viewmodel.loginState.value.toString())
            if (Instance == null) {
                Instance = VerifyOtpFragment()
            }
            return Instance
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_verify_otp, container, false)
        WorkStation()
        return binding.root
    }

    override fun WorkStation() {
        getAllScopes()
        binding.verifyNow.setOnClickListener {
            if (binding.editTextCode.text.toString().isNullOrBlank()) {
                showCustomAlert(ENTER_OTP, binding.root)
            }else if(binding.editTextCode.text.toString().length<6){
                showCustomAlert(INVALID_OTP, binding.root)
            }else{
                viewModel!!.VerifyOtpNow(binding.editTextCode.text.toString(),modelMain!!)
            }

        }
    }
    fun getAllScopes(){
        lifecycleScope.launchWhenCreated {
            viewModel!!.verificationState.collect {
                when (it) {
                    is LoginSealed.VerificationState.Loading -> {
                        (requireActivity() as LoginActivity).showLoadingDialog("")!!
                            .show()
                    }
                    is LoginSealed.VerificationState.Success -> {
                        if(!it.response.isLoggedIn){
                            (requireActivity() as LoginActivity).showLoadingDialog(it.response.message!!)!!
                                .show()
                        }else{
                            (requireActivity() as LoginActivity).showLoadingDialog(it.response.message!!)!!
                                .dismiss()

                            if( viewModel!!.isUserAvailable()){
                                StartActivityNow()
                            }else{
                                (requireActivity() as LoginActivity).startFragment(
                                    RegistrationFragment.newInstance(viewModel!!),true,
                                    RegistrationFragment::class.java.simpleName)
                            }

                        }
                        showCustomAlert(it.response.message!!,binding.root)

                    }
                    is LoginSealed.VerificationState.Error -> {
                        (requireActivity() as LoginActivity).showLoadingDialog(it.response.message!!)!!
                            .dismiss()
                        showCustomAlert(it.response.message!!,binding.root)
                    }
                }
            }
        }
    }

    private fun StartActivityNow() {
        startActivity(Intent(requireContext(),HomeActivity::class.java))
        requireActivity().finish()
    }
}
