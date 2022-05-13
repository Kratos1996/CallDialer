package com.artixtise.richdialer.data.richcall

import androidx.lifecycle.LiveData
import com.artixtise.richdialer.database.roomdatabase.tables.RichCallData

interface RichCallRepository {
    suspend fun insertRichCallHistory(richCall: RichCallData)
    fun getRichCallData(id: Long): LiveData<RichCallData>
    fun getRichCallDataList(): LiveData<List<RichCallData>>
    suspend fun deleteSingleRichCall(id: Long)
    suspend fun updateRichCallTextData(text:String,id: Long)
    suspend fun updateRichCallEmojiData(emoji:String,id: Long)
    suspend fun updateImageData(image: String, id: Long)
    suspend fun updateGifData(image: String, id: Long)
    suspend fun updateLocation(lat: String,long:String ,id: Long)


}