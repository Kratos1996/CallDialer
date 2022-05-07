package com.artixtise.richdialer.presentation.ui.activity.userProfile.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artixtise.richdialer.data.contact.contactRepository.ContactRepository
import com.artixtise.richdialer.data.profile.model.UserAccessData
import com.artixtise.richdialer.database.roomdatabase.tables.ContactList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewmodel @Inject constructor( val repositoryImpl: ContactRepository) : ViewModel() {
    var otherUserDetail = MutableLiveData<UserAccessData>()
    fun getContactDetail(phone:String): Flow<ContactList> =repositoryImpl.getContact(phone)
    fun insertContact(contact:ContactList)= viewModelScope.launch(Dispatchers.IO) { repositoryImpl.insertContact(contact) }
    fun updateRichCallData(number:String,userId:String,name:String,isAvailable:Boolean)= viewModelScope.launch(Dispatchers.IO) { repositoryImpl.setRichCallData(number,name,userId,isAvailable) }
    fun getOtherUserId(number: String): MutableLiveData<UserAccessData> {
        viewModelScope.async (Dispatchers.IO) {
            otherUserDetail=repositoryImpl.getProfileData(number)
        }
        return otherUserDetail
    }
}