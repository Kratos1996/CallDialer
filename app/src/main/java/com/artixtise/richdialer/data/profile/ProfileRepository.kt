package com.artixtise.richdialer.data.profile

import androidx.lifecycle.LiveData
import com.artixtise.richdialer.database.roomdatabase.tables.MyProfileTable
import com.artixtise.richdialer.database.roomdatabase.tables.RichCallData

interface ProfileRepository {
    suspend fun insertMyProfile(table: MyProfileTable)
    fun getMyProfile(): LiveData<MyProfileTable>
}