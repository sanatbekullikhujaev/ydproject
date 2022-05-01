package com.usc0der.ydprojectnew.api.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.usc0der.ydprojectnew.api.network.ApiHelper
import com.usc0der.ydprojectnew.api.repository.Repository
import com.usc0der.ydprojectnew.api.repository.TestRepository

class TestViewModelFactory (private val apiHelper: ApiHelper): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(TestViewModel::class.java)){
            return TestViewModel(TestRepository(apiHelper)) as T
        }
        throw IllegalAccessException("Unknown class name")
    }
}