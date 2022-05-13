package com.artixtise.richdialer.data.contact.contactRepository

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.provider.CallLog
import android.provider.ContactsContract
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.artixtise.richdialer.application.ErrorMessage
import com.artixtise.richdialer.base.RICH_CALL_HISTORY
import com.artixtise.richdialer.base.STATUS
import com.artixtise.richdialer.base.USER_DATA
import com.artixtise.richdialer.data.call.model.RichCallData
import com.artixtise.richdialer.data.media.MediaConstants
import com.artixtise.richdialer.data.media.MediaItem
import com.artixtise.richdialer.data.profile.model.UserAccessData
import com.artixtise.richdialer.database.datastore.DataStoreBase
import com.artixtise.richdialer.database.datastore.DataStoreCoroutinesHandler
import com.artixtise.richdialer.database.prefrence.SharedPre
import com.artixtise.richdialer.database.roomdatabase.AppDB
import com.artixtise.richdialer.database.roomdatabase.tables.ContactList
import com.artixtise.richdialer.domain.model.contact.OtherUserProfileSealed
import com.artixtise.richdialer.domain.model.contact.RichCallSealed
import com.artixtise.richdialer.domain.model.contact.UserStatusSealed
import com.artixtise.richdialer.domain.model.login.UserStatusModel
import com.artixtise.richdialer.domain.recent.RecentCallData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.lang.Long
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.Boolean
import kotlin.Exception
import kotlin.String
import kotlin.arrayOf
import kotlin.let
import kotlin.toString


@Singleton
class ContactsRepositoryImpl @Inject constructor(
    val db: AppDB,
    val context: Context,
    val auth: FirebaseAuth,
    val dataStore: DataStoreBase,
    val collectionRef: CollectionReference,
    val sharedPre: SharedPre,
) :
    ContactRepository {

    var otherUserId =
        MutableStateFlow<OtherUserProfileSealed.FetchOtherUserState>(OtherUserProfileSealed.FetchOtherUserState.Empty)
    var userStatusState =
        MutableStateFlow<UserStatusSealed.SaveUserStatusState>(UserStatusSealed.SaveUserStatusState.Empty)
    var richCallState=
        MutableStateFlow<RichCallSealed.SaveRichCallState>(RichCallSealed.SaveRichCallState.Empty)
    var otherUserDetail = MutableLiveData<UserAccessData>()
    var richCallST = MutableLiveData<String>()
    var updateStatus = MutableLiveData<String>()

    override suspend fun insertContact(contact: ContactList) = db.getDao().insert(contact)
    override suspend fun updateContact(contact: ContactList) {
      db.getDao().update(contact)
    }

    override suspend fun getProfileData(number: String): MutableLiveData<UserAccessData> {
        otherUserId.value = OtherUserProfileSealed.FetchOtherUserState.Loading(true)
        try {
            val data = collectionRef.document(number).collection(USER_DATA).document(number).get().await().toObject(UserAccessData::class.java)
            if (data != null && !data.userId.isNullOrBlank()) {
                otherUserDetail.postValue(data!!)
                otherUserId.value = OtherUserProfileSealed.FetchOtherUserState.Success(data)
            }
        } catch (e: Exception) {
            otherUserId.value = OtherUserProfileSealed.FetchOtherUserState.Error(e.message ?: "")
        }
        return otherUserDetail
    }


    override suspend fun saveSenderData(data: RichCallData): MutableLiveData<String>  {
        richCallState.value = RichCallSealed.SaveRichCallState.Loading(true)
        collectionRef.document(sharedPre.userMobile!!).collection(RICH_CALL_HISTORY).document(sharedPre.userMobile!!).set(data).addOnSuccessListener {
            richCallState.value = RichCallSealed.SaveRichCallState.Success(ErrorMessage.SAVE_RICH_CALL_DATA)
            DataStoreCoroutinesHandler.io() {}
           // Log.d("Success_data_store", it.toString())
            richCallST.postValue(ErrorMessage.SAVE_RICH_CALL_DATA)
        }.addOnFailureListener {
            DataStoreCoroutinesHandler.io() {}
            richCallState.value = RichCallSealed.SaveRichCallState.Success(it.message.toString())
            //Log.d("Failure", it.toString())
            richCallST.postValue(ErrorMessage.ERROR)
        }
        return richCallST
    }

    override suspend fun saveUserStatus(b: Boolean): MutableLiveData<String> {
        var status = UserStatusModel(sharedPre.userId,sharedPre.userMobile,b)
        userStatusState.value = UserStatusSealed.SaveUserStatusState.Loading(true)
        collectionRef.document(sharedPre.userMobile!!).collection(STATUS).document(sharedPre.userMobile!!).set(status).addOnSuccessListener {
            userStatusState.value = UserStatusSealed.SaveUserStatusState.Success(ErrorMessage.SAVE_RICH_CALL_DATA)
            DataStoreCoroutinesHandler.io() {}
            // Log.d("Success_data_store", it.toString())
            updateStatus.postValue(ErrorMessage.SAVE_RICH_CALL_DATA)
        }.addOnFailureListener {
            userStatusState.value = UserStatusSealed.SaveUserStatusState.Success(it.message.toString())
            //Log.d("Failure", it.toString())
            updateStatus.postValue(ErrorMessage.ERROR)
        }
        return updateStatus
    }

    override suspend fun saveUserToken(token: String?) {
        try {
            val data = collectionRef.document(sharedPre.userMobile!!).collection(USER_DATA).document(sharedPre.userMobile!!).get().await().toObject(UserAccessData::class.java)
            if (data != null && !data.userId.isNullOrBlank()) {
               data.deviceToken=token?:""
                collectionRef.document(sharedPre.userMobile!!).collection(USER_DATA).document(sharedPre.userMobile!!).set(data).await()
            }
        } catch (e: Exception) {

        }
    }

    override suspend fun getUserFromDB(): List<ContactList> {
        val data = db.getDao().GetFavContactList(true)
        return data

    }

    override fun getContact(phone: String): Flow<ContactList> {
        return db.getDao().GetPhone(phone)
    }

    override fun getPhoneWithoutFlow(phone: String): ContactList {
        return db.getDao().GetPhoneWithoutFlow(phone)
    }

    override suspend fun getContact(phone: String, isFav: Boolean): ContactList {
        return db.getDao().GetPhone(phone, isFav)
    }

    override fun setFavContact(phone: String, isFav: Boolean) {
        GlobalScope.launch {
            val int = db.getDao().setFavPhone(phone, isFav)
            val data = int
        }
    }

    override suspend  fun setRichCallData(contact: ContactList) {
        db.getDao().update(contact)
    }

    override fun deleteAll() {
        GlobalScope.launch {
            db.getDao().DeleteAllContacts()
        }
    }

    override fun deleteSingleContact(contact: ContactList) {
        GlobalScope.launch {
            db.getDao().DeleteSingleContacts(contact.phoneNumber)
        }
    }

    override fun getContactList(): LiveData<List<ContactList>> {
        return db.getDao().GetContactList()
    }

    override fun getContactList(data: String): LiveData<List<ContactList>> {
        return db.getDao().GetContactList(data)
    }

    override fun getFavList(): LiveData<List<ContactList>> {
        return db.getDao().GetFavContactListLive(true)
    }

    override fun getFavList(data: String): LiveData<List<ContactList>> {
        return db.getDao().GetFavContactListLive(data, true)
    }

    @SuppressLint("Range")
    override suspend fun loadContact() {

        val resolver: ContentResolver = context.contentResolver
        val cursor = resolver.query(
            ContactsContract.Contacts.CONTENT_URI, null, null, null,
            null
        )

        if (cursor != null) {
            val mobileNoSet = HashSet<String>()
            try {
                val nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)

                var name: String = ""
                var number: String = ""
                while (cursor.moveToNext()) {
                    val hasPhoneNumber =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                            .toInt()
                    val id =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                    if (hasPhoneNumber > 0) {
                        name = cursor.getString(nameIndex)
                        val phoneCursor: Cursor = context.contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            arrayOf<String>(id),
                            null
                        )!!
                        if (phoneCursor.moveToNext()) {
                            var phoneNumber = phoneCursor.getString(
                                phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                            )
                            phoneNumber=getMeMyNumber(phoneNumber)
                            val photo = phoneCursor.getString(
                                phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI)
                            )
                            val email = phoneCursor.getString(
                                phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)
                            )
                            if (phoneNumber.length > 10 && phoneNumber.length < 13) {
                                number = phoneNumber.replace("\\s+".toRegex(), "")
                                saveContact(number, name, email, photo?:"", mobileNoSet)
                            } else if (phoneNumber.length == 10) {
                                number = phoneNumber.replace("\\s+".toRegex(), "")
                                saveContact("91" + number, name, email, photo?:"", mobileNoSet)
                            } else {
                                Log.e("RichCall","Nothing")
                            }


                            phoneCursor.close()
                        }

                    }

                }
            } finally {
                cursor.close()
            }
        }
    }

    override fun loadRecentCalls():ArrayList<RecentCallData> {
        var ricentCallList=ArrayList<RecentCallData>()
        val sb = StringBuffer()
        val details = arrayOf(
            CallLog.Calls.NUMBER,
            CallLog.Calls.TYPE,
            CallLog.Calls.DURATION,
            CallLog.Calls.CACHED_NAME,
            CallLog.Calls._ID
        )
        val managedCursor: Cursor = context.contentResolver.query(CallLog.Calls.CONTENT_URI, details, null, null, CallLog.Calls._ID + " DESC")!!
        val number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER)
        val type = managedCursor.getColumnIndex(CallLog.Calls.TYPE)
        val date = managedCursor.getColumnIndex(CallLog.Calls.DATE)
        val duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION)
        sb.append("Call Details :")
        while (managedCursor.moveToNext()) {
            val phNumber = managedCursor.getString(number) // mobile number
            val callType = managedCursor.getString(type) // call type
            val callDate = managedCursor.getString(date) // call date
            val callDayTime = Date(Long.valueOf(callDate))
            val callDuration = managedCursor.getString(duration)
            var dir: String? = null
            val dircode = callType.toInt()
            when (dircode) {
                CallLog.Calls.OUTGOING_TYPE -> dir = "OUTGOING CALL"
                CallLog.Calls.INCOMING_TYPE -> dir = "INCOMING CALL"
                CallLog.Calls.MISSED_TYPE -> dir = "MISSED CALL"
            }
            val data= RecentCallData(phNumber,callType,callDate,callDayTime.toString(),callDuration,dir)
            ricentCallList.add(data)
            sb.append("\nPhone Number:--- $phNumber \nCall Type:--- $dir \nCall Date:--- $callDayTime \nCall duration in sec :--- $callDuration")
            sb.append("\n----------------------------------")
        }
        managedCursor.close()
        Log.e("Agil value --- ", sb.toString())
        return ricentCallList
    }
    fun getMeMyNumber(number: String, countryCode: String="91"): String? {
        return number.replace("[^0-9\\+]".toRegex(), "") //remove all the non numbers (brackets dashes spaces etc.) except the + signs
            .replace("(^[1-9].+)".toRegex(), "$countryCode$1") //if the number is starting with no zero and +, its a local number. prepend cc
            .replace("(.)(\\++)(.)".toRegex(), "$1$3") //if there are left out +'s in the middle by mistake, remove them
            .replace("(^0{2}|^\\+)(.+)".toRegex(), "$2") //make 00XXX... numbers and +XXXXX.. numbers into XXXX...
            .replace("^0([1-9])".toRegex(), "$countryCode$1") //make 0XXXXXXX numbers into CCXXXXXXXX numbers
    }
    private suspend fun saveContact(
        number: String,
        name: String,
        email: String,
        photo: String,
        mobileNoSet: HashSet<String>
    ) {
        if (!mobileNoSet.contains(number)) {
            mobileNoSet.add(number)
            val data = getPhoneWithoutFlow(number)
            if (data == null && number.length > 9) {
                if (photo.isNullOrBlank()) {
                    var contact = ContactList(
                        number,
                        name,
                        profile = "",
                        email,
                    )
                    insertContact(contact)
                }
                else {
                    var contact = ContactList(
                        number,
                        name,
                        photo,
                        email
                    )
                    insertContact(contact)
                }

            }

        }
    }

    override fun getMedia(cursor: Cursor?) = flow<MutableList<MediaItem>> {
        cursor?.let {
            val mediaList = mutableListOf<MediaItem>()
            CoroutineScope(Dispatchers.IO).async {
                val index = cursor.getColumnIndex(MediaStore.Files.FileColumns._ID)
                val dateIndex = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATE_ADDED)
                val typeIndex = cursor.getColumnIndex(MediaStore.Files.FileColumns.MEDIA_TYPE)
                val durationIndex = cursor.getColumnIndex(MediaStore.Video.Media.DURATION)
                val titleIndex = cursor.getColumnIndex(MediaStore.Files.FileColumns.TITLE)
                val pathIndex = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA)
                while (it.moveToNext()) {
                    val id = cursor.getLong(index)
                    val path = ContentUris.withAppendedId(MediaConstants.IMAGE_VIDEO_URI, id)
                    val mediaType = cursor.getInt(typeIndex)
                    val longDate = cursor.getLong(dateIndex)
                    val absolutePath = cursor.getString(pathIndex)
                    val title = cursor.getString(titleIndex)
                    val duration = cursor.getLong(durationIndex)
                    //Log.e("Media", "getMedia: $path $longDate $mediaDate $absolutePath $duration")
                    var type = if (mediaType == 1) {
                        "IMAGE"
                    } else {
                        "VIDEO"
                    }
                    if (mediaType == 1) {
                        mediaList.add(
                            MediaItem(
                                id,
                                path.toString(),
                                type,
                                longDate,
                                absolutePath,
                                duration,
                                title
                            )
                        )
                    }
                }
            }.await()
            emit(mediaList)
        }
    }
}

