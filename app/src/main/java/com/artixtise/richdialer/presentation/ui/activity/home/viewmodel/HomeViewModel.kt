package com.artixtise.richdialer.presentation.ui.activity.home.viewmodel

import android.database.Cursor
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artixtise.richdialer.base.USER_DATA
import com.artixtise.richdialer.data.call.model.RichCallData
import com.artixtise.richdialer.data.contact.contactRepository.ContactRepository
import com.artixtise.richdialer.data.contact.contactRepository.ContactsRepositoryImpl
import com.artixtise.richdialer.data.profile.model.UserAccessData
import com.artixtise.richdialer.database.prefrence.SharedPre
import com.artixtise.richdialer.database.roomdatabase.tables.ContactList
import com.artixtise.richdialer.domain.model.contact.OtherUserProfileSealed
import com.artixtise.richdialer.domain.model.contact.RichCallSealed
import com.artixtise.richdialer.domain.model.login.LoginSealed
import com.google.firebase.firestore.CollectionReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val sharedPre: SharedPre,
    val repositoryImpl: ContactRepository,
    val collectionRef: CollectionReference
) : ViewModel() {

    var isRichCall = false

    var otherUserId = MutableStateFlow<OtherUserProfileSealed.FetchOtherUserState>(OtherUserProfileSealed.FetchOtherUserState.Empty)
    var richCallState = MutableStateFlow<RichCallSealed.SaveRichCallState>(RichCallSealed.SaveRichCallState.Empty)
    var otherUserDetail = MutableLiveData<UserAccessData>()
    var richCallST = MutableLiveData<String>()
    var updateStatus = MutableLiveData<String>()
    var selectedData = MutableLiveData<String>()

    fun getContacts() = viewModelScope.launch(Dispatchers.IO) { repositoryImpl.loadContact() }

    fun getContactsList(): LiveData<List<ContactList>> = repositoryImpl.getContactList()

    fun getFavContactsList(): LiveData<List<ContactList>> = repositoryImpl.getFavList()

    fun getMedia(cursor: Cursor?) = repositoryImpl.getMedia(cursor = cursor)

     fun getOtherUserId(number: String): MutableLiveData<UserAccessData> {
        viewModelScope.async (Dispatchers.IO) {
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

    //rich call data save
    suspend fun saveSenderData(richData: RichCallData) : MutableLiveData<String> {
        val data =viewModelScope.async (Dispatchers.IO) {
            richCallST=repositoryImpl.saveSenderData(richData)
            richCallST
        }
        return data.await()
    }

    //update status
    suspend fun saveUserStatus(b: Boolean) : MutableLiveData<String> {
        val data =viewModelScope.async (Dispatchers.IO) {
            updateStatus=repositoryImpl.saveUserStatus(b)
            updateStatus
        }
        return data.await()
    }

    fun UpdateToken(token: String?) {
        viewModelScope.async (Dispatchers.IO) {
        repositoryImpl.saveUserToken(token)
        }

    }
}