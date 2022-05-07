package com.artixtise.richdialer.presentation.ui.activity.login.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.artixtise.richdialer.R
import com.artixtise.richdialer.application.ErrorMessage.PHONENUMBER_IS_EMPTY
import com.artixtise.richdialer.application.ErrorMessage.PHONENUMBER_IS_INVALID
import com.artixtise.richdialer.application.ErrorMessage.PLEASE_WAIT
import com.artixtise.richdialer.base.BaseFragment
import com.artixtise.richdialer.databinding.FragmentLoginBinding
import com.artixtise.richdialer.domain.model.login.LoginSealed
import com.artixtise.richdialer.presentation.ui.activity.login.LoginActivity
import com.artixtise.richdialer.presentation.ui.activity.login.viewmodel.LoginViewmodel
import com.simplemobiletools.dialer.login.fragments.VerifyOtpFragment

class LoginFragment : BaseFragment(R.layout.fragment_login) {
    private lateinit var binding: FragmentLoginBinding
    private var countryCode = "91"

    companion object {

        var Instance: LoginFragment? = null
        var viewModel: LoginViewmodel? = null
        fun newInstance(viewmodel: LoginViewmodel): LoginFragment? {
            viewModel = viewmodel
            if (Instance == null) {
                Instance = LoginFragment()
            }
            return Instance
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        WorkStation()
        getAllScopeFunction()
        return binding.root
    }

    override fun WorkStation() {
        binding.sentOtp.setOnClickListener {
            if (binding.etNumber.text.toString().isNullOrBlank()) {
                showCustomAlert(PHONENUMBER_IS_EMPTY, binding.root)
            } else if (binding.etNumber.text.toString().length < 10) {
                showCustomAlert(PHONENUMBER_IS_INVALID, binding.root)
            } else {
                viewModel!!.SentOtpNow(
                    countryCode,
                    binding.etNumber.text.toString(),
                    requireActivity()
                )
            }
        }
    }

    private fun getAllScopeFunction() {
        lifecycleScope.launchWhenCreated {
            viewModel!!.loginState.collect {
                when (it) {
                    is LoginSealed.VerificationCodeState.Loading -> {
                        (requireActivity() as LoginActivity).showLoadingDialog(PLEASE_WAIT)!!
                            .show()
                    }
                    is LoginSealed.VerificationCodeState.Success -> {
                        (requireActivity() as LoginActivity).showLoadingDialog(PLEASE_WAIT)!!
                            .dismiss()
                        (requireActivity() as LoginActivity).startFragment(
                            VerifyOtpFragment.newInstance(
                                viewModel!!,it.response
                            ), addToBackStack = true, VerifyOtpFragment::class.java.simpleName
                        )
                    }
                    is LoginSealed.VerificationCodeState.Message -> {
                        (requireActivity() as LoginActivity).showLoadingDialog(PLEASE_WAIT)!!
                            .dismiss()
                        showCustomAlert(it.message, binding.root)
                    }
                }
            }
        }
    }
}
