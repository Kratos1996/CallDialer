package com.artixtise.richdialer.mapper

import com.artixtise.richdialer.data.profile.model.UserAccessData
import com.artixtise.richdialer.database.roomdatabase.tables.ContactList

object UserAccessToContactList {
    fun convert(user:UserAccessData,contact:ContactList):ContactList{
        contact.isAvailableRichCall=true
        contact.name=user.name!!
        contact.richaCallUserId=user.userId!!
        contact.deviceToken=user.deviceToken!!
        return contact
    }
}