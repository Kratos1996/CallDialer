package com.artixtise.richdialer.data.richcall

import android.content.Context
import androidx.lifecycle.LiveData
import com.artixtise.richdialer.database.roomdatabase.AppDB
import com.artixtise.richdialer.database.roomdatabase.tables.RichCallData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RichRepositoryImpl @Inject constructor(
    val db: AppDB,
    val context: Context
) :
    RichCallRepository {

    override suspend fun insertRichCallHistory(richCall: RichCallData) =
        db.getDao().insert(richCall)

    override fun getRichCallDataList(): LiveData<List<RichCallData>> {
        val data = db.getDao().GetRichCallDataList()
        return data

    }

    override fun getRichCallData(id: Long): LiveData<RichCallData> {
        return db.getDao().GetRichCallData(id)
    }


    override suspend fun deleteSingleRichCall(id: Long) = db.getDao().DeleteSingleRichCall(id)

    override suspend fun updateRichCallEmojiData(emoji:String,id: Long) {
        db.getDao().updateRichCallEmojiData(emoji,id)
    }

    override suspend fun updateRichCallTextData(text:String,id: Long) {
        db.getDao().updateRichCallTextData(text,id)
    }


}