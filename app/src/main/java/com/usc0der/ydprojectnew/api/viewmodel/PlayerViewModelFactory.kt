package com.usc0der.ydprojectnew.api.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.usc0der.ydprojectnew.api.network.ApiHelper
import com.usc0der.ydprojectnew.api.repository.Repository
import java.lang.IllegalArgumentException

class PlayerViewModelFactory(private val apiHelper: ApiHelper): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
       if(modelClass.isAssignableFrom(PlayerViewModel::class.java)){
           return PlayerViewModel(Repository(apiHelper)) as T
       }
           throw IllegalArgumentException("Unknown class name")
    }
}