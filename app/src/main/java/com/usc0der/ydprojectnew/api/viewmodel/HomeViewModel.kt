package com.usc0der.ydprojectnew.api.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.usc0der.ydprojectnew.api.network.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.usc0der.ydprojectnew.api.repository.Repository
import com.usc0der.ydprojectnew.model.Home
import com.usc0der.ydprojectnew.model.HomeModel
import kotlinx.coroutines.Dispatchers

class HomeViewModel(private val repository: Repository) : ViewModel() {

    private val _homeState = MutableStateFlow<UIState<Home>>(UIState.Empty)
    val homeSate: StateFlow<UIState<Home>> = _homeState

    init {
        initHome()
    }

    fun initHome() = viewModelScope.launch(Dispatchers.IO) {
        _homeState.value = UIState.Loading
        _homeState.value = repository.getAllHomeList()
    }

    sealed class HomeUiState {
        data class Success(var data: List<HomeModel>) : HomeUiState()
        data class Error(val error: String) : HomeUiState()
        object Loading : HomeUiState()
        object Empty : HomeUiState()
    }
}