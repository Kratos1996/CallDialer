package com.artixtise.richdialer.data.contact.contactRepository

import android.database.Cursor
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.artixtise.richdialer.application.ErrorMessage
import com.artixtise.richdialer.application.ErrorMessage.OTHER_USER_DATA
import com.artixtise.richdialer.data.call.model.RichCallData
import com.artixtise.richdialer.data.media.MediaItem
import com.artixtise.richdialer.data.profile.model.UserAccessData
import com.artixtise.richdialer.database.datastore.DataStoreCoroutinesHandler
import com.artixtise.richdialer.database.roomdatabase.tables.ContactList
import com.artixtise.richdialer.domain.model.contact.OtherUserProfileSealed
import com.artixtise.richdialer.domain.model.contact.RichCallSealed
import com.artixtise.richdialer.domain.model.login.LoginSealed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

interface ContactRepository {
    suspend fun insertContact(contact: ContactList)
    suspend fun getUserFromDB():List<ContactList>
    fun getContact(phone:String): Flow<ContactList>
    fun getPhoneWithoutFlow(phone:String): ContactList
    suspend fun getContact(phone:String,isFav: Boolean):ContactList
    fun setFavContact( phone:String,isFav:Boolean)
    suspend fun setRichCallData( number:String,name:String,userId:String,isAvailable:Boolean)
    fun deleteAll()
    fun deleteSingleContact(contact:ContactList)
    fun getContactList(): LiveData<List<ContactList>>
    fun getContactList(data:String): LiveData<List<ContactList>>
    fun getFavList(): LiveData<List<ContactList>>
    fun getFavList(data:String): LiveData<List<ContactList>>
    suspend fun loadContact()
    fun getMedia(cursor: Cursor?): Flow<MutableList<MediaItem>>
    suspend fun getProfileData(number: String): MutableLiveData<UserAccessData>
    suspend fun saveSenderData(data: RichCallData): MutableLiveData<String>
    suspend fun saveUserStatus(b: Boolean): MutableLiveData<String>
    suspend fun saveUserToken(token: String?)

}