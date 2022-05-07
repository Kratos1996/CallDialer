package com.artixtise.richdialer.data.login

import android.app.Activity
import com.artixtise.richdialer.data.profile.model.UserAccessData
import com.artixtise.richdialer.domain.model.login.LoginModel
import com.artixtise.richdialer.domain.model.login.LoginSealed
import com.artixtise.richdialer.repositories.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow

interface LoginRepository {
    fun getPhoneAuthProvider(countryCode:String,phoneNumber:String,activity:Activity)
    suspend fun verifyOTP(otp: String, data: LoginModel)
    suspend fun isUserAvailable():Boolean
    fun saveUserData(data: UserAccessData)
}