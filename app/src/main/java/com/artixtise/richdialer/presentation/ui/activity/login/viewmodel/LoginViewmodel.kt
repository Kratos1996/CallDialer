package com.artixtise.richdialer.presentation.ui.activity.login.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artixtise.richdialer.data.login.LoginRepository
import com.artixtise.richdialer.data.login.LoginRepositoryImpl
import com.artixtise.richdialer.data.profile.model.UserAccessData
import com.artixtise.richdialer.domain.model.contact.OtherUserProfileSealed
import com.artixtise.richdialer.domain.model.login.LoginModel
import com.artixtise.richdialer.domain.model.login.LoginSealed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewmodel @Inject constructor(val repositoryImpl: LoginRepositoryImpl) : ViewModel() {
    var loginState =
        MutableStateFlow<LoginSealed.VerificationCodeState>(LoginSealed.VerificationCodeState.Empty)
    var verificationState =
        MutableStateFlow<LoginSealed.VerificationState>(LoginSealed.VerificationState.Empty)
    var registerState =
        MutableStateFlow<LoginSealed.RegisterUserState>(LoginSealed.RegisterUserState.Empty)

    fun SentOtpNow(countryCode: String, phoneNumber: String, activity: Activity) {
        viewModelScope.launch {
            repositoryImpl.getPhoneAuthProvider(countryCode, phoneNumber = phoneNumber, activity)
            repositoryImpl.loginState.collect {
                when (it) {
                    is LoginSealed.VerificationCodeState.Loading -> {
                        loginState.value = it
                    }
                    is LoginSealed.VerificationCodeState.Success -> {
                        loginState.value = it
                    }
                    is LoginSealed.VerificationCodeState.Message -> {
                        loginState.value = it
                    }
                }
            }
        }
    }

    fun VerifyOtpNow(otp: String, data: LoginModel) {
        viewModelScope.launch {
            repositoryImpl.verifyOTP(otp, data)
            repositoryImpl.verificationState.collect {
                when (it) {
                    is LoginSealed.VerificationState.Loading -> {
                        verificationState.value = it
                    }
                    is LoginSealed.VerificationState.Success -> {
                        verificationState.value = it
                    }
                    is LoginSealed.VerificationState.Error -> {
                        verificationState.value = it
                    }
                }
            }
        }

    }

    fun saveUserData(data: UserAccessData) {
        viewModelScope.launch {
            repositoryImpl.saveUserData(data)
            repositoryImpl.registerState.collect {
                when (it) {
                    is LoginSealed.RegisterUserState.Loading -> {
                        registerState.value = it
                    }
                    is LoginSealed.RegisterUserState.Success -> {
                        registerState.value = it
                    }
                    is LoginSealed.RegisterUserState.Error -> {
                        registerState.value = it
                    }
                }
            }
        }

    }

    fun updateUserData(data: UserAccessData) {
        viewModelScope.launch {
            repositoryImpl.saveUserData(data)
            repositoryImpl.registerState.collect {
                when (it) {
                    is LoginSealed.RegisterUserState.Loading -> {
                        registerState.value = it
                    }
                    is LoginSealed.RegisterUserState.Success -> {
                        registerState.value = it
                    }
                    is LoginSealed.RegisterUserState.Error -> {
                        registerState.value = it
                    }
                }
            }
        }

    }

    suspend fun isUserAvailable(): Boolean {
        val data = viewModelScope.async {
            repositoryImpl.isUserAvailable()
        }
        return data.await()
    }
}