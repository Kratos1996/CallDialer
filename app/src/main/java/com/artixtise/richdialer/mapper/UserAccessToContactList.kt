package com.artixtise.richdialer.mapper

import com.artixtise.richdialer.data.profile.model.UserAccessData
import com.artixtise.richdialer.database.roomdatabase.tables.ContactList

object UserAccessToContactList {
    fun convert(user:UserAccessData,contact:ContactList):ContactList{
        contact.isAvailableRichCall=true
        contact.profile=user.profilePicture
        contact.name=user.name!!
        contact.richaCallUserId=user.userId!!
        contact.deviceToken=user.deviceToken!!
        contact.instagramAccount=user.instagramUrl!!
        contact.instagramAccountVisible=user.instagramVisible!!
        contact.LinkedInAccount=user.linkedInUrl!!
        contact.linkedInAccountVisible=user.linkedInVisible!!
        contact.twitterAccount=user.twitterUrl!!
        contact.twitterAccountVisible=user.twitterVisible!!
        contact.webUrl=user.websiteUrl!!
        contact.webUrlVisible=user.websiteVisible!!
        return contact
    }
    fun convert(user:UserAccessData):ContactList{
        val contact=ContactList()
        contact.isAvailableRichCall=true
        contact.profile=user.profilePicture
        contact.phoneNumber=user.mobile
        contact.phoneNumber2=user.mobile2
        contact.email=user.email
        contact.name=user.name
        contact.richaCallUserId=user.userId
        contact.deviceToken=user.deviceToken
        contact.instagramAccount=user.instagramUrl
        contact.instagramAccountVisible=user.instagramVisible
        contact.LinkedInAccount=user.linkedInUrl
        contact.linkedInAccountVisible=user.linkedInVisible
        contact.twitterAccount=user.twitterUrl
        contact.twitterAccountVisible=user.twitterVisible
        contact.webUrl=user.websiteUrl
        contact.webUrlVisible=user.websiteVisible
        contact.facebookAccount=user.facebookUrl
        contact.facebookAccountVisible=user.facebookVisible
        return contact
    }
}