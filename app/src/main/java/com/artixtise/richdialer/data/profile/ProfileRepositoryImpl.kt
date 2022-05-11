package com.artixtise.richdialer.data.profile

import android.content.Context
import androidx.lifecycle.LiveData
import com.artixtise.richdialer.database.roomdatabase.AppDB
import com.artixtise.richdialer.database.roomdatabase.tables.MyProfileTable
import com.artixtise.richdialer.database.roomdatabase.tables.RichCallData
import com.artixtise.richdialer.domain.model.contact.OtherUserProfileSealed
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ProfileRepositoryImpl @Inject constructor(
    val db: AppDB,
    val context: Context
) :
    ProfileRepository {
    var otherUserId =
        MutableStateFlow<OtherUserProfileSealed.FetchOtherUserState>(OtherUserProfileSealed.FetchOtherUserState.Empty)

    override suspend fun insertMyProfile(table: MyProfileTable) {
        db.getDao().insert(table)
    }

    override fun getMyProfile(): LiveData<MyProfileTable> {
        return  db.getDao().GetMyProfile()
    }



}