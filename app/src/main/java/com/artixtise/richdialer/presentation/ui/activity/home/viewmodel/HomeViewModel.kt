package com.artixtise.richdialer.presentation.ui.activity.home.viewmodel

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artixtise.richdialer.api.Resource
import com.artixtise.richdialer.base.USER_DATA
import com.artixtise.richdialer.data.call.model.RichCallData
import com.artixtise.richdialer.data.contact.contactRepository.ContactRepository
import com.artixtise.richdialer.data.profile.ProfileRepository
import com.artixtise.richdialer.data.profile.model.UserAccessData
import com.artixtise.richdialer.data.richcall.RichCallRepository
import com.artixtise.richdialer.database.prefrence.SharedPre
import com.artixtise.richdialer.database.roomdatabase.tables.ContactList
import com.artixtise.richdialer.database.roomdatabase.tables.MyProfileTable
import com.artixtise.richdialer.domain.model.contact.OtherUserProfileSealed
import com.artixtise.richdialer.domain.model.contact.RichCallSealed
import com.artixtise.richdialer.domain.recent.RecentCallData
import com.artixtise.richdialer.domain.remote.apiUsecase.getData.GetRicCallDataUseCase
import com.artixtise.richdialer.domain.remote.apiUsecase.setData.SetRicCallDataUseCase
import com.artixtise.richdialer.domain.remote.apiUsecase.uplodeImage.UplodeImageUseCase
import com.google.firebase.firestore.CollectionReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File
import java.util.ArrayList
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val sharedPre: SharedPre,
    val repositoryImpl: ContactRepository,
    val richCallRepo: RichCallRepository,
    val collectionRef: CollectionReference,
    val profileRepo: ProfileRepository,
    val getRichCallUseCase: GetRicCallDataUseCase,
    val uplodeImageUseCase: UplodeImageUseCase,
    val setRichCallUseCase: SetRicCallDataUseCase
) : ViewModel() {
    var isRichCall = false
    var otherUserId =
        MutableStateFlow<OtherUserProfileSealed.FetchOtherUserState>(OtherUserProfileSealed.FetchOtherUserState.Empty)
    var richCallState =
        MutableStateFlow<RichCallSealed.SaveRichCallState>(RichCallSealed.SaveRichCallState.Empty)
    var otherUserDetail = MutableLiveData<UserAccessData>()
    var richCallST = MutableLiveData<String>()
    var updateStatus = MutableLiveData<String>()
    var selectedData = MutableLiveData<String>()
    var getRichCallMutable =
        MutableStateFlow<RichCallSealed.GetRichCalldata>(RichCallSealed.GetRichCalldata.Empty)
    var setRichCallMutable =
        MutableStateFlow<RichCallSealed.SaveRichCalldata>(RichCallSealed.SaveRichCalldata.Empty)

    var uplodeImageMutable =
        MutableStateFlow<RichCallSealed.UplodeImage>(RichCallSealed.UplodeImage.Empty)

    fun uplodeImage(senderUserId: String,image:File) {
        uplodeImageUseCase(senderUserId,image).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    uplodeImageMutable.value =
                        RichCallSealed.UplodeImage.Success(response = result.data!!)
                }
                is Resource.Error -> {
                    uplodeImageMutable.value = RichCallSealed.UplodeImage.Error(
                        result.message ?: "An unexpected error occured"
                    )
                }
                is Resource.Loading -> {
                    uplodeImageMutable.value =
                        RichCallSealed.UplodeImage.Loading(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    //get
    fun getRichCallData(senderUserId: String) {
        getRichCallUseCase(senderUserId).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    getRichCallMutable.value =
                        RichCallSealed.GetRichCalldata.Success(response = result.data!!)
                }
                is Resource.Error -> {
                    getRichCallMutable.value = RichCallSealed.GetRichCalldata.Error(
                        result.message ?: "An unexpected error occured"
                    )
                }
                is Resource.Loading -> {
                    getRichCallMutable.value =
                        RichCallSealed.GetRichCalldata.Loading(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    //save
    fun saveRichCallData(
        richCallData: com.artixtise.richdialer.database.roomdatabase.tables.RichCallData
    ) {
        setRichCallUseCase(richCallData).onEach { result ->
            when (result) {
                is Resource.Success -> setRichCallMutable.value =
                    RichCallSealed.SaveRichCalldata.Success(response = result.data!!)

                is Resource.Error -> setRichCallMutable.value =
                    RichCallSealed.SaveRichCalldata.Error(
                        result.message ?: "An unexpected error occured"
                    )

                is Resource.Loading -> setRichCallMutable.value =
                    RichCallSealed.SaveRichCalldata.Loading(isLoading = true)

            }
        }.launchIn(viewModelScope)
    }


    fun getContacts() = viewModelScope.launch(Dispatchers.IO) { repositoryImpl.loadContact() }

    fun getContactsList(): LiveData<List<ContactList>> = repositoryImpl.getContactList()

    fun getContactsList(data:String): LiveData<List<ContactList>> = repositoryImpl.getContactList(data)

    fun getFavContactsList(): LiveData<List<ContactList>> = repositoryImpl.getFavList()

    fun getFavContactsList(data:String): LiveData<List<ContactList>> = repositoryImpl.getFavList(data)

    fun getMedia(cursor: Cursor?) = repositoryImpl.getMedia(cursor = cursor)

    fun getOtherUserId(number: String): MutableLiveData<UserAccessData> {
        viewModelScope.async(Dispatchers.IO) {
            otherUserId.value = OtherUserProfileSealed.FetchOtherUserState.Loading(true)
            try {
                val data =
                    collectionRef.document(number).collection(USER_DATA).document(number).get()
                        .await().toObject(UserAccessData::class.java)
                if (data != null && !data.userId.isNullOrBlank()) {
                    otherUserDetail.postValue(data!!)
                    otherUserId.value = OtherUserProfileSealed.FetchOtherUserState.Success(data)
                }
            } catch (e: Exception) {
                otherUserId.value =
                    OtherUserProfileSealed.FetchOtherUserState.Error(e.message ?: "")
            }
        }
        return otherUserDetail
    }

    //rich call data save
    suspend fun saveSenderData(richData: RichCallData): MutableLiveData<String> {
        val data = viewModelScope.async(Dispatchers.IO) {
            richCallST = repositoryImpl.saveSenderData(richData)
            richCallST
        }
        return data.await()
    }

    //update status
    suspend fun saveUserStatus(b: Boolean): MutableLiveData<String> {
        val data = viewModelScope.async(Dispatchers.IO) {
            updateStatus = repositoryImpl.saveUserStatus(b)
            updateStatus
        }
        return data.await()
    }

    fun UpdateToken(token: String?) {
        viewModelScope.async(Dispatchers.IO) {
            repositoryImpl.saveUserToken(token)
        }

    }

    fun insertRichCallHistory(richCallData: com.artixtise.richdialer.database.roomdatabase.tables.RichCallData) {
        viewModelScope.launch(Dispatchers.IO) { richCallRepo.insertRichCallHistory(richCallData) }
    }

    fun deleteRichCallHistory(id: Long) {
        viewModelScope.launch(Dispatchers.IO) { richCallRepo.deleteSingleRichCall(id) }
    }

    fun getRichCallData(id: Long): LiveData<com.artixtise.richdialer.database.roomdatabase.tables.RichCallData> {
        return richCallRepo.getRichCallData(id)
    }

    fun getRichCallDataList(): LiveData<List<com.artixtise.richdialer.database.roomdatabase.tables.RichCallData>> {
        return richCallRepo.getRichCallDataList()
    }

    fun UpdateEmoji(id: Long, emoji: String) {
        viewModelScope.launch(Dispatchers.IO) {
            richCallRepo.updateRichCallEmojiData(emoji, id)
        }
    }
    fun UpdateImage(id: Long, image: String) {
        viewModelScope.launch(Dispatchers.IO) {
            richCallRepo.updateImageData(image, id)
        }
    }
    fun UpdateGif(id: Long, gif: String) {
        viewModelScope.launch(Dispatchers.IO) {
            richCallRepo.updateGifData(gif, id)
        }
    }
    fun UpdateLocation(id: Long, lat: String,long: String) {
        viewModelScope.launch(Dispatchers.IO) {
            richCallRepo.updateLocation(lat,long, id)
        }
    }

    fun UpdateText(id: Long, text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            richCallRepo.updateRichCallTextData(text, id)
        }
    }

    fun getRecentData(): LiveData<ArrayList<RecentCallData>>{
        return repositoryImpl.loadRecentCalls()
    }
    fun insertMyProfile(table: MyProfileTable){
        viewModelScope.launch (Dispatchers.IO){
            profileRepo.insertMyProfile(table)
        }
    }
    fun getMyProfile()=profileRepo.getMyProfile()
}