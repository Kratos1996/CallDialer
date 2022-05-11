package com.artixtise.richdialer.data.richcall

import androidx.lifecycle.LiveData
import com.artixtise.richdialer.database.roomdatabase.tables.RichCallData

interface RichCallRepository {
    suspend fun insertRichCallHistory(richCall: RichCallData)
    fun getRichCallData(id: Long): LiveData<RichCallData>
    fun getRichCallDataList(): LiveData<List<RichCallData>>
    suspend fun deleteSingleRichCall(id: Long)
}