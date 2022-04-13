package com.usc0der.ydprojectnew.api.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.usc0der.ydprojectnew.model.RegResponse
import com.usc0der.ydprojectnew.api.network.UIState
import com.usc0der.ydprojectnew.api.repository.AuthRepository
import com.usc0der.ydprojectnew.eski_api.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _loginState = MutableStateFlow<UIState<LoginData>>(UIState.Empty)
    val loginState: StateFlow<UIState<LoginData>> = _loginState
    suspend fun login(body: LoginForm) = viewModelScope.launch(Dispatchers.IO) {
        _loginState.value = repository.login(body)
    }

    private val _regState = MutableStateFlow<UIState<RegResponse>>(UIState.Empty)
    val regState: StateFlow<UIState<RegResponse>> = _regState
    suspend fun reg(body: RegisterForm) {
        _regState.value = repository.reg(body)
    }

    fun reset()=viewModelScope.launch {
        _regState.value=UIState.Empty
        _loginState.value=UIState.Empty
    }



}