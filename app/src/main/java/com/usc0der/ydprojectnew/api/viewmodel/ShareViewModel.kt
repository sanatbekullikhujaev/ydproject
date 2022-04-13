package com.usc0der.ydprojectnew.api.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShareViewModel() : ViewModel() {

    val status = MutableLiveData<Int>()
    fun sendId(id: Int) {
        status.value = id
    }
}