package com.artixtise.richdialer.database.datastore
/*
* © Copyright Ishant Sharma
* Android Developer
* JAVA/KOTLIN
* +91-7732993378
* ishant.sharma1947@gmail.com
* */
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.preferencesKey

object PreferenceKeys {
    val BOOLEAN_KEY : Preferences.Key<Boolean> = preferencesKey<Boolean>("boolean_key")
    val STRING_KEY : Preferences.Key<String> = preferencesKey<String>("string_key")
    val INTEGER_KEY : Preferences.Key<Int> = preferencesKey<Int>("integer_key")
    val DOUBLE_KEY : Preferences.Key<Double> = preferencesKey<Double>("double_key")
    val LONG_KEY : Preferences.Key<Long> = preferencesKey<Long>("long_key")
    val APP_NAME : Preferences.Key<String> = preferencesKey<String>("Dumboo")
    val IS_APP_LOGGED_IN : Preferences.Key<Boolean> = preferencesKey<Boolean>("app_logged_in")
    val IS_HELLO_SCREEN_VISIBLE : Preferences.Key<Boolean> = preferencesKey<Boolean>("helloScreenVisible")
    val IS_LOCATION_SHARE : Preferences.Key<Boolean> = preferencesKey<Boolean>("isLocationShared")
    val IS_PRIVATED : Preferences.Key<Boolean> = preferencesKey<Boolean>("isYourInformationPrivate")
    val NAME : Preferences.Key<String> = preferencesKey<String>("name")
    val USERID : Preferences.Key<String> = preferencesKey<String>("userId")
    val EMAIL : Preferences.Key<String> = preferencesKey<String>("email")
    val MOBILE : Preferences.Key<String> = preferencesKey<String>("phone")
    val COUNTRY_CODE : Preferences.Key<String> = preferencesKey<String>("countryCode")
    //val LIST_MODEL_KEY : Preferences.Key<List<CustomModel>> = preferencesKey<List<CustomModel>>("list_model_key")
}