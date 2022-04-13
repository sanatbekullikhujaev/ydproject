package com.usc0der.ydprojectnew.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPref(context: Context) {
    private var mySharedPref: SharedPreferences =
        context.getSharedPreferences("preferences", Context.MODE_PRIVATE)


    var device_token: String
        set(value) = mySharedPref.edit().putString("device_token", value).apply()
        get() = mySharedPref.getString("device_token", "")!!

    var deviceDate: String
        set(value) = mySharedPref.edit().putString("device_date", value).apply()
        get() = mySharedPref.getString("device_date", "")!!


    fun setCommentId(id: Int) {
        val editor = mySharedPref.edit()
        editor.putInt("commentId", id)
        editor.apply()
    }

    fun getCommentId(): Int {
        return mySharedPref.getInt("commentId", 0)
    }

    fun setPDFUrl(url: String) {
        val editor = mySharedPref.edit()
        editor.putString("pdfUrel", url)
        editor.apply()
    }

    fun getPDFUrl() = mySharedPref.getString("pdfUrel", "").toString()
}