package com.artixtise.richdialer.database.roomdatabase.tables

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.type.PostalAddress
import kotlinx.parcelize.Parcelize
import org.jetbrains.annotations.NotNull

@Entity
@Parcelize
data class MyProfileTable(@ColumnInfo var name: String ="",
                          @ColumnInfo var gender: String ="",
                          @ColumnInfo var email: String ="",
                          @ColumnInfo var userId: String ="",
                          @PrimaryKey var mobile: String ="",
                          @ColumnInfo var mobile2: String ="",
                          @ColumnInfo var address: String ="",
                          @ColumnInfo var lastUpdatedProfile: Long = 0,
                          @ColumnInfo var profilePicture:String ="",
                          @ColumnInfo var instagramUrl:String ="",
                          @ColumnInfo var linkedInUrl:String ="",
                          @ColumnInfo var facebookUrl:String ="",
                          @ColumnInfo var websiteUrl:String ="",
                          @ColumnInfo var twitterUrl:String ="",
                          @ColumnInfo var deviceToken:String="",
                          @ColumnInfo var instagramVisible:Boolean=true,
                          @ColumnInfo var linkedInVisible:Boolean=true,
                          @ColumnInfo var facebookVisible:Boolean=true,
                          @ColumnInfo var websiteVisible:Boolean=true,
                          @ColumnInfo var twitterVisible:Boolean=true) : Parcelable
