package com.usc0der.ydprojectnew.api.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.usc0der.ydprojectnew.model.RegResponse
import com.usc0der.ydprojectnew.api.network.UIState
import com.usc0der.ydprojectnew.api.repository.AuthRepository
import com.usc0der.ydprojectnew.eski_api.models.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor( val repository: AuthRepository) : ViewModel() {


}