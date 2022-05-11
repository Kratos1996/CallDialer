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
data class ContactList(@PrimaryKey(autoGenerate = false)
                       var phoneNumber:String="",
                       @ColumnInfo var name: String="",
                       @ColumnInfo var profile: String="",
                       @ColumnInfo var email: String="",
                       @ColumnInfo var phoneNumber2: String="",
                       @ColumnInfo var twitterAccount: String="",
                       @ColumnInfo var instagramAccount: String="",
                       @ColumnInfo var facebookAccount: String="",
                       @ColumnInfo var LinkedInAccount: String="",
                       @ColumnInfo var webUrl: String="",
                       @ColumnInfo var isFav:Boolean=false,
                       @ColumnInfo var richaCallUserId: String="",
                       @ColumnInfo var isAvailableRichCall: Boolean=false,
                       @ColumnInfo var deviceToken: String="",
                       @ColumnInfo var twitterAccountVisible: Boolean=false,
                       @ColumnInfo var instagramAccountVisible: Boolean=false,
                       @ColumnInfo var linkedInAccountVisible: Boolean=false,
                       @ColumnInfo var facebookAccountVisible: Boolean=false,
                       @ColumnInfo var webUrlVisible: Boolean=false,
                       ): Parcelable
