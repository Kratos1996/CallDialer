package com.artixtise.richdialer.repositories.methods
/*
* © Copyright Ishant Sharma
* Android Developer
* JAVA/KOTLIN
* +91-7732993378
* ishant.sharma1947@gmail.com
* */

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.util.Base64
import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.artixtise.richdialer.R
import com.artixtise.richdialer.aws.FileUploadListener
import com.artixtise.richdialer.database.datastore.DataStoreBase
import java.io.File
import java.io.UnsupportedEncodingException
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MethodsRepo @Inject constructor(var context: Context, var dataStore: DataStoreBase) {


    fun isValidEmail(email: String?): Boolean {
        val EMAIL_PATTERN =
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
        return Pattern.compile(EMAIL_PATTERN).matcher(email).matches()
    }

    fun isValidPhoneNumber(phone: String?): Boolean {
        val mobilePattern = "[0-9]{10}"
        return Pattern.matches(mobilePattern, phone)
    }

    fun isValidName(name: String?): Boolean {
        val pattern = Pattern.compile("^[a-zA-Z\\s]*$")
        val matcher: Matcher = pattern.matcher(name)
        return matcher.matches()
    }

    fun isValidPassword(password: String?): Boolean {
        val pattern = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20}$")
        val matcher: Matcher = pattern.matcher(password)
        return matcher.matches()
    }

    fun isNetworkConnected(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        return cm!!.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
    }



    fun GetFormattedDate(context: Context?, smsTimeInMilis: Long): String? {
        val formatter = SimpleDateFormat("yyyy-MM-dd , h:mm aa")
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = smsTimeInMilis
        return formatter.format(calendar.time)
    }

    fun GetFormattedDate(context: Context?, smsTimeInMilis: Long, Format: String?): String? {
        val formatter = SimpleDateFormat(Format)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = smsTimeInMilis
        return formatter.format(calendar.time)
    }

    fun ConverMillsTo(mills: Long, yourFormat: String?): String? {
        val date = Date(mills)
        val formatter: DateFormat = SimpleDateFormat(yourFormat)
        return formatter.format(date)
    }

    fun GetMillsFromDateAndTime(dateOrTime: String?, format: String?): Long {
        val sdf = SimpleDateFormat(format)
        var date: Date? = null
        return try {
            date = sdf.parse(dateOrTime)
            date.time
        } catch (e: ParseException) {
            e.printStackTrace()
            0
        }
    }

    fun Base64Encode(text: String): String? {
        var encrpt = ByteArray(0)
        try {
            encrpt = text.toByteArray(charset("UTF-8"))
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return Base64.encodeToString(encrpt, Base64.DEFAULT)
    }

    fun Base64Decode(base64: String?): String? {
        val decrypt = Base64.decode(base64, Base64.DEFAULT)
        var text: String? = null
        try {
            text = String(decrypt, charset("UTF-8"))
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return text
    }

    fun setBackGround(context: Context?, view: View, @DrawableRes drawable: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(ContextCompat.getDrawable(context!!, drawable))
        } else {
            view.setBackgroundDrawable(ContextCompat.getDrawable(context!!, drawable))
        }
    }

    fun Visible(view: View) {
        view.visibility = View.VISIBLE
    }

    fun Invisible(view: View) {
        view.visibility = View.INVISIBLE
    }

    fun Gone(view: View) {
        view.visibility = View.GONE
    }

}