package com.artixtise.richdialer.data.login

import android.app.Activity
import android.content.Context
import android.util.Log
import com.artixtise.richdialer.application.ErrorMessage.OTP_VERIFICATION_FAILED
import com.artixtise.richdialer.application.ErrorMessage.OTP_VERIFICATION_SUCCESSFULLY
import com.artixtise.richdialer.application.ErrorMessage.USER_REGISTERED
import com.artixtise.richdialer.base.USER_DATA
import com.artixtise.richdialer.data.profile.ProfileRepository
import com.artixtise.richdialer.data.profile.model.UserAccessData
import com.artixtise.richdialer.database.datastore.DataStoreBase
import com.artixtise.richdialer.database.datastore.DataStoreCoroutinesHandler
import com.artixtise.richdialer.database.prefrence.SharedPre
import com.artixtise.richdialer.database.roomdatabase.AppDB
import com.artixtise.richdialer.domain.model.login.LoginModel
import com.artixtise.richdialer.domain.model.login.LoginSealed
import com.artixtise.richdialer.domain.model.login.VerificationModel
import com.artixtise.richdialer.mapper.UserAccessToProfile
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepositoryImpl @Inject constructor(
    val db: AppDB,
    val context: Context,
    val auth: FirebaseAuth,
    val dataStore: DataStoreBase,
    val document: CollectionReference,
    val sharedPre: SharedPre,
    val profileRepo:ProfileRepository
) : LoginRepository {

    var loginState =
        MutableStateFlow<LoginSealed.VerificationCodeState>(LoginSealed.VerificationCodeState.Empty)
    var verificationState =
        MutableStateFlow<LoginSealed.VerificationState>(LoginSealed.VerificationState.Empty)
    var registerState =
        MutableStateFlow<LoginSealed.RegisterUserState>(LoginSealed.RegisterUserState.Empty)


    override fun getPhoneAuthProvider(
        countryCode: String,
        phoneNumber: String,
        activity: Activity
    ) {
        loginState.value = LoginSealed.VerificationCodeState.Loading(true)
        val callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                loginState.value = LoginSealed.VerificationCodeState.Loading(false)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                loginState.value = LoginSealed.VerificationCodeState.Message(e.message)
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                loginState.value = LoginSealed.VerificationCodeState.Loading(false)
                loginState.value = LoginSealed.VerificationCodeState.Success(
                    LoginModel(
                        verificationId,
                        "$countryCode$phoneNumber"
                    )
                )
            }
        }
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+$countryCode$phoneNumber")
            .setTimeout(30L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callback)
            .build()
        DataStoreCoroutinesHandler.io {
            dataStore.setCountryCode(countryCode)
            dataStore.setPhoneNumber(phoneNumber)
            sharedPre.setUserMobile("$countryCode$phoneNumber")
            sharedPre.setCountryCode(countryCode)
        }
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    override suspend fun verifyOTP(otp: String, data: LoginModel) {
        try {
            verificationState.value = LoginSealed.VerificationState.Loading(true)
            val phoneAuthCredential = PhoneAuthProvider.getCredential(
                data.verificationId!!,
                otp
            )
            val authData = auth.signInWithCredential(phoneAuthCredential).await()
            DataStoreCoroutinesHandler.io() {
                dataStore.setUserId(authData.user!!.uid)
                sharedPre.setUserId(authData.user!!.uid)
                sharedPre.isLoggedIn=true
                dataStore.setIsLoggedIn(true)
            }
            verificationState.value = LoginSealed.VerificationState.Success(
                VerificationModel(
                    OTP_VERIFICATION_SUCCESSFULLY,
                    authData.user!!.uid,
                    true
                )
            )
        } catch (e: Exception) {
            DataStoreCoroutinesHandler.io() {
                dataStore.setUserId("")
                sharedPre.setUserId("")
                sharedPre.isLoggedIn=false
                dataStore.setIsLoggedIn(false)
            }
            verificationState.value = LoginSealed.VerificationState.Error(
                VerificationModel(
                    OTP_VERIFICATION_FAILED,
                    "",
                    false
                )
            )
        }
    }

    override fun saveUserData(data: UserAccessData) {
        GlobalScope.launch {  profileRepo.insertMyProfile(UserAccessToProfile.convert(data)) }
        registerState.value = LoginSealed.RegisterUserState.Loading(true)
        document.document(data.mobile).collection(USER_DATA).document(data.mobile).set(data).addOnSuccessListener {
            registerState.value = LoginSealed.RegisterUserState.Success(USER_REGISTERED)
            DataStoreCoroutinesHandler.io() {
                dataStore.setIsLoggedIn(true)
                sharedPre.isLoggedIn=true
                sharedPre.setIsregister(true)
            }

        }.addOnFailureListener {
            DataStoreCoroutinesHandler.io() {
                dataStore.setIsLoggedIn(true)
                sharedPre.setIsregister(false)
            }
            registerState.value = LoginSealed.RegisterUserState.Success(it.message.toString())

        }
    }


    override suspend fun isUserAvailable(): Boolean {
        val isAvailable = GlobalScope.async {
                val data =  document.document(sharedPre.userMobile!!).collection(USER_DATA).document(sharedPre.userMobile!!).get().await().toObject(UserAccessData::class.java)
            if(data != null)  {
                return@async true
            }else{
                return@async false
            }

            }
        return isAvailable.await()
    }

}

