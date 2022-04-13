package com.usc0der.ydprojectnew.api.network

sealed class UIState<out T> {
    data class Success<out T>(val data: T) : UIState<T>()
    data class Error(val error: String) : UIState<Nothing>()
    data class LoginExample(val error: String,val example:String) : UIState<Nothing>()
    object Loading : UIState<Nothing>()
    object Empty : UIState<Nothing>()
}

