package com.usc0der.ydprojectnew.api.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.usc0der.ydprojectnew.api.network.UIState
import com.usc0der.ydprojectnew.api.repository.SettingsRepository
import com.usc0der.ydprojectnew.eski_api.models.ChangeUser
import com.usc0der.ydprojectnew.eski_api.models.LoginData

class SettingsViewModel(private val repository: SettingsRepository) : ViewModel()  {

    private val _editPasswordState = MutableStateFlow<UIState<LoginData>>(UIState.Loading)
    val editPasswordState: StateFlow<UIState<LoginData>> = _editPasswordState
    suspend fun editPassword(body: ChangeUser) {
        _editPasswordState.value = repository.editPassword(body)
    }
}