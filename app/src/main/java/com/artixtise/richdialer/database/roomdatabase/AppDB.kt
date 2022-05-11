package com.artixtise.richdialer.database.roomdatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.artixtise.richdialer.database.roomdatabase.tables.CallData
import com.artixtise.richdialer.database.roomdatabase.tables.ContactList
import com.artixtise.richdialer.database.roomdatabase.tables.MyProfileTable
import com.artixtise.richdialer.database.roomdatabase.tables.RichCallData

@Database(entities = [CallData::class, ContactList::class, RichCallData::class, MyProfileTable::class], version = 1, exportSchema = false)
abstract class AppDB : RoomDatabase() {
    abstract fun getDao(): MyDao
}