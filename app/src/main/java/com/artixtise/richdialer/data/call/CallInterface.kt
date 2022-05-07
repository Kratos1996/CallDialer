package com.artixtise.richdialer.data.call

import androidx.viewpager2.widget.ViewPager2
import com.artixtise.richdialer.database.roomdatabase.tables.ContactList
import com.google.android.material.tabs.TabLayout

interface CallInterface {
    fun onCallStart(number:String)
    fun onRichCallStart(list: ContactList)
}