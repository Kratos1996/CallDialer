package com.artixtise.richdialer.presentation.ui.activity.userProfile.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artixtise.richdialer.api.Resource
import com.artixtise.richdialer.base.USER_DATA
import com.artixtise.richdialer.data.contact.contactRepository.ContactRepository
import com.artixtise.richdialer.data.login.LoginRepositoryImpl
import com.artixtise.richdialer.data.profile.ProfileRepository
import com.artixtise.richdialer.data.profile.model.UserAccessData
import com.artixtise.richdialer.database.roomdatabase.tables.ContactList
import com.artixtise.richdialer.database.roomdatabase.tables.MyProfileTable
import com.artixtise.richdialer.domain.model.contact.OtherUserProfileSealed
import com.artixtise.richdialer.domain.model.contact.RichCallSealed
import com.artixtise.richdialer.domain.model.login.LoginSealed
import com.artixtise.richdialer.domain.remote.apiUsecase.delete.UseCaseDelete
import com.google.firebase.firestore.CollectionReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileViewmodel @Inject constructor(val repositoryImpl: ContactRepository,
                                            val collectionRef: CollectionReference,
                                           val profileRepo:ProfileRepository,
                                           val useCaseDelete: UseCaseDelete,
                                           val loginRepositoryImpl: LoginRepositoryImpl
) : ViewModel() {
    var otherUserDetail = MutableLiveData<UserAccessData>()
    var otherUserId =
        MutableStateFlow<OtherUserProfileSealed.FetchOtherUserState>(OtherUserProfileSealed.FetchOtherUserState.Empty)
    var deletRichCallMutable =
        MutableStateFlow<RichCallSealed.DeleteRichCall>(RichCallSealed.DeleteRichCall.Empty)

    var registerState = MutableStateFlow<LoginSealed.RegisterUserState>(LoginSealed.RegisterUserState.Empty)
    fun getContactDetail(phone:String): Flow<ContactList> =repositoryImpl.getContact(phone)
    fun insertContact(contact:ContactList)= viewModelScope.launch(Dispatchers.IO) { repositoryImpl.updateContact(contact) }
    fun updateRichCallData(contact:ContactList)= viewModelScope.launch(Dispatchers.IO) { repositoryImpl.setRichCallData(contact) }
    fun getOtherUserId(number: String): MutableLiveData<UserAccessData> {
        viewModelScope.launch  (Dispatchers.IO) {
            otherUserId.value = OtherUserProfileSealed.FetchOtherUserState.Loading(true)
            try {
                val data =
                    collectionRef.document(number).collection(USER_DATA).document(number).get().await().toObject(UserAccessData::class.java)
                if (data != null && !data.userId.isNullOrBlank()) {
                    otherUserDetail.postValue(data!!)
                    otherUserId.value = OtherUserProfileSealed.FetchOtherUserState.Success(data)
                }
            } catch (e: Exception) {
                otherUserId.value = OtherUserProfileSealed.FetchOtherUserState.Error(e.message ?: "")
            }

        }
        return otherUserDetail
    }
    fun getMyProfile()=profileRepo.getMyProfile()
    fun insertMyProfile(table:MyProfileTable){
        viewModelScope.launch (Dispatchers.IO){
            profileRepo.insertMyProfile(table)
        }
    }
    fun updateUserData(data: UserAccessData) {
        viewModelScope.launch {
            loginRepositoryImpl.saveUserData(data)
            loginRepositoryImpl.registerState.collect {
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
    fun deletRichCall(senderUserId: String) {
        useCaseDelete(senderUserId).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    deletRichCallMutable.value =
                        RichCallSealed.DeleteRichCall.Success(response = result.data!!)
                }
                is Resource.Error -> {
                    deletRichCallMutable.value = RichCallSealed.DeleteRichCall.Error(
                        result.message ?: "An unexpected error occured"
                    )
                }
                is Resource.Loading -> {
                    deletRichCallMutable.value =
                        RichCallSealed.DeleteRichCall.Loading(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}