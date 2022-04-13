package com.usc0der.ydprojectnew.app

import android.annotation.SuppressLint
import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.usc0der.ydprojectnew.utils.Constants
import java.text.SimpleDateFormat
import java.util.*

class App : Application() {

    private var mToken: String? = null

    override fun onCreate() {
        super.onCreate()
        mApp = this
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        saveDate()
    }

    fun saveAutoEnter(autoEnterValue:Boolean){
        val editor =
            getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE)
        editor.edit()
            .putBoolean(Constants.AUTO_ENTER, autoEnterValue )
            .apply()
    }

    fun getAutoEnter():Boolean = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE)
        .getBoolean(Constants.AUTO_ENTER, false)

    fun saveToken(token: String) {
        mToken = token
    }

    fun getToken(): String {
        return "Bearer ${mToken ?: ""}"
    }

    fun saveUserData(login: String?, password: String?) {
        val editor = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE)
        editor.edit()
            .putString(Constants.USER_LOGIN, login)
            .putString(Constants.USER_PASSWORD, password)
            .apply()
    }

    @SuppressLint("SimpleDateFormat")
    fun saveDate() {
        val editor =
            getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE)
        editor.edit()
            .putString(Constants.DATE, SimpleDateFormat("ddMMyyyy").format(Date()))
            .apply()
    }

    fun saveVideoUrl(url: String?) {
        val editor =
            getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE)
        editor.edit()
            .putString(Constants.VIDEO_URL_CONST, url)
            .apply()
    }

    fun getVideoUrl(): String? = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE)
        .getString(Constants.VIDEO_URL_CONST, null)

    fun getDate(): String? = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE)
        .getString(Constants.DATE, "")

    fun getUserLogin(): String? = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE)
        .getString(Constants.USER_LOGIN, null)

    fun getUserPassword(): String? = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE)
        .getString(Constants.USER_PASSWORD, null)


    companion object {
        @JvmStatic
        lateinit var mApp: App
    }
}