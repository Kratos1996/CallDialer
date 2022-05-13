package com.artixtise.richdialer.database.roomdatabase

import androidx.lifecycle.LiveData
import androidx.room.*
import com.artixtise.richdialer.database.roomdatabase.tables.CallData
import com.artixtise.richdialer.database.roomdatabase.tables.ContactList
import com.artixtise.richdialer.database.roomdatabase.tables.MyProfileTable
import com.artixtise.richdialer.database.roomdatabase.tables.RichCallData
import kotlinx.coroutines.flow.Flow

@Dao
interface MyDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(contact: ContactList)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(richCallData: RichCallData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(myProfile: MyProfileTable)

    @Update
    fun update(contact: ContactList)

    @Delete
    fun delete(contact: ContactList)

    @Query("Select * From ContactList")
    fun GetContactList(): LiveData<List<ContactList>>

    @Query("Select * From RichCallData where id=:id")
    fun GetRichCallData(id:Long): LiveData<RichCallData>

    @Query("Select * From MyProfileTable")
    fun GetMyProfile(): LiveData<MyProfileTable>

    @Query("Select * From RichCallData")
    fun GetRichCallDataList(): LiveData<List<RichCallData>>

    @Query("Delete From RichCallData where id=:id")
    fun DeleteSingleRichCall(id: Long)

    @Query("Update RichCallData set emoji=:emoji  Where id=:richCallId")
    fun updateRichCallEmojiData(emoji: String,richCallId:Long): Int

    @Query("Update RichCallData set textMsg=:text  Where id=:richCallId")
    fun updateRichCallTextData(text: String,richCallId:Long): Int

    @Query("Update RichCallData set image=:image ,gif=:gif  Where id=:richCallId")
    fun updateRichCallImageData(image: String,richCallId:Long,gif: String=""): Int

    @Query("Update RichCallData set gif=:gif , image=:image  Where id=:richCallId")
    fun updateRichCallGifData(gif: String,richCallId:Long ,image:String =""): Int

    @Query("Update RichCallData set lat=:lat , lng=:lang  Where id=:richCallId")
    fun updateLocationData(lat: String,lang:String,richCallId:Long): Int

    @Query("Select * From ContactList where Name Like '%' || :data || '%' or PhoneNumber  Like '%' || :data || '%' ")
    fun GetContactList(data: String): LiveData<List<ContactList>>

    @Query("Select * From ContactList where Name Like '%' || :data || '%' or PhoneNumber  Like '%' || :data || '%' and isFav=:isFav ")
    fun GetFavContactListLive(data: String, isFav: Boolean): LiveData<List<ContactList>>

    @Query("Delete From ContactList")
    fun DeleteAllContacts()

    @Query("Delete From ContactList where PhoneNumber=:contact")
    fun DeleteSingleContacts(contact: String)

    @Query("Select * From ContactList where PhoneNumber=:phone")
    fun GetPhone(phone: String): Flow<ContactList>

    @Query("Select * From ContactList where PhoneNumber=:phone")
    fun GetPhoneWithoutFlow(phone: String): ContactList

    @Query("Select * From ContactList where PhoneNumber=:phone and isFav=:isFav")
    fun GetPhone(phone: String, isFav: Boolean): ContactList


    @Query("Update ContactList set isFav=:isFav Where PhoneNumber=:phone")
    fun setFavPhone(phone: String, isFav: Boolean): Int


    @Query("Update ContactList set richaCallUserId=:userId and isAvailableRichCall=:isAvailable Where PhoneNumber=:phone")
    fun updateRichCallData(phone: String,userId:String, isAvailable: Boolean): Int

    @Query("Select * From ContactList Where isFav=:isFav")
    fun GetFavContactList(isFav: Boolean): List<ContactList>

    @Query("Select * From ContactList Where isFav=:isFav")
    fun GetFavContactListLive(isFav: Boolean): LiveData<List<ContactList>>
}