package com.artixtise.richdialer.mapper

import com.artixtise.richdialer.data.profile.model.UserAccessData
import com.artixtise.richdialer.database.roomdatabase.tables.ContactList
import com.artixtise.richdialer.database.roomdatabase.tables.MyProfileTable

object UserAccessToProfile {
    fun convert(user: UserAccessData): MyProfileTable {
      val profile=MyProfileTable()
      profile.name=user.name
      profile.gender=user.gender
      profile.email=user.email
      profile.mobile=user.mobile
      profile.mobile2=user.mobile2
      profile.userId=user.userId
      profile.lastUpdatedProfile=user.lastUpdatedProfile
      profile.profilePicture=user.profilePicture
      profile.instagramUrl=user.instagramUrl
      profile.facebookUrl=user.facebookUrl
      profile.linkedInUrl=user.linkedInUrl
      profile.websiteUrl=user.websiteUrl
      profile.twitterUrl=user.twitterUrl
      profile.deviceToken=user.deviceToken
      profile.instagramVisible=user.instagramVisible
      profile.facebookVisible=user.facebookVisible
      profile.linkedInVisible=user.linkedInVisible
      profile.twitterVisible=user.twitterVisible
      profile.websiteVisible=user.websiteVisible
      profile.address=user.address
     return profile
    }
  fun reverce(user: MyProfileTable): UserAccessData {
    val profile=UserAccessData()
    profile.name=user.name
    profile.gender=user.gender
    profile.email=user.email
    profile.mobile=user.mobile
    profile.mobile2=user.mobile2
    profile.userId=user.userId
    profile.lastUpdatedProfile=user.lastUpdatedProfile
    profile.profilePicture=user.profilePicture
    profile.instagramUrl=user.instagramUrl
    profile.facebookUrl=user.facebookUrl
    profile.linkedInUrl=user.linkedInUrl
    profile.websiteUrl=user.websiteUrl
    profile.twitterUrl=user.twitterUrl
    profile.deviceToken=user.deviceToken
    profile.instagramVisible=user.instagramVisible
    profile.facebookVisible=user.facebookVisible
    profile.linkedInVisible=user.linkedInVisible
    profile.twitterVisible=user.twitterVisible
    profile.websiteVisible=user.websiteVisible
    profile.address=user.address
    return profile
  }
}