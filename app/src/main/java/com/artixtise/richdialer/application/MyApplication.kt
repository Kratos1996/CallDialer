package com.artixtise.richdialer.application
/*Android Developer
* Ishant Sharma
* Java and Kotlin
* */

import android.app.Application
import com.artixtise.richdialer.application.ThemeHelper.applyTheme
import com.artixtise.richdialer.database.prefrence.SharedPre
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class MyApplication : Application(){
    companion object{
        private var instance: MyApplication? = null


        //private lateinit var appDatabase: AppDB

        @JvmStatic
        fun getInstance(): MyApplication? {
            if (instance == null) {
                instance = MyApplication()
            }
            return instance
        }
    }
    private lateinit var sharedPre:SharedPre
    override fun onCreate() {
        super.onCreate()
        instance = this
        sharedPre = SharedPre.getInstance(getApplicationContext())!!
        val themePref = sharedPre.GetCurrentTheme
        if(!themePref.isNullOrEmpty()){
            if(themePref.equals(ThemeHelper.LIGHT_MODE)){
                sharedPre.isDarkModeEnable=false
            }else if(themePref.equals(ThemeHelper.DARK_MODE)){
                sharedPre.isDarkModeEnable=true
            }
            applyTheme(themePref)
        }else{
            sharedPre.isDarkModeEnable=false
            applyTheme(ThemeHelper.LIGHT_MODE)

        }

    }
    fun isNightModeEnabled(): Boolean {
        return  sharedPre.isDarkModeEnable
    }
    fun setIsNightModeEnabled(isNightModeEnabled: Boolean) {
        sharedPre.isDarkModeEnable=isNightModeEnabled
    }

}