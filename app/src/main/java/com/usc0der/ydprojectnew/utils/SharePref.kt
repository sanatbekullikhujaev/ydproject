package com.usc0der.ydprojectnew.utils

import android.content.Context
import android.content.SharedPreferences

class SharePref(private val context: Context) {


    private val sharedPref = context.getSharedPreferences("usc0der", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor =  sharedPref.edit()
    fun setToken(token:String){
        editor.putString("token",token).apply()
    }
    fun getToken():String{
        return sharedPref.getString("token","").toString()
    }
    fun getLanguageId():Int{
        return sharedPref.getInt("id",0)
    }
    fun setLanguageId(id:Int){
        editor.putInt("id",id).apply()
    }
    fun setVideoOrAudioId(id:Int){
        editor.putInt("videoOrAudioId",id).apply()
    }
    fun getVideoOrAudioId():Int{
        return sharedPref.getInt("videoOrAudioId",0)
    }
    fun setRegCount(count:Int){
        editor.putInt("regCount",count).apply()
    }
    fun getRegCount() = sharedPref.getInt("regCount",4)
}