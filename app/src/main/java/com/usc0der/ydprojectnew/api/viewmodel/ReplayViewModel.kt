package com.usc0der.ydprojectnew.api.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.usc0der.ydprojectnew.api.repository.Repository
import com.usc0der.ydprojectnew.model.ReplayObj
import com.usc0der.ydprojectnew.model.SendReplay
import com.usc0der.ydprojectnew.model.Update
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.usc0der.ydprojectnew.model.ReplayComment

class ReplayViewModel(private val repository: Repository) : ViewModel() {

    private val _replaySateFlow = MutableStateFlow<ReplayUiState>(ReplayUiState.Empty)
    var replaySateFlow: StateFlow<ReplayUiState> = _replaySateFlow

    fun initAllReplay(id: Int) = viewModelScope.launch {

        try {
            val response = repository.getAllReplayByCommentId(id)
            if (response.success) {
                _replaySateFlow.value = ReplayUiState.AllReplay(response.data)
            } else {
                _replaySateFlow.value = ReplayUiState.Error("Chatni boshlang")
            }
        } catch (e: Exception) {
            _replaySateFlow.value = ReplayUiState.Error(e.message.toString())
        }
    }

    private val _sendReplay = MutableStateFlow<ReplayUiState>(ReplayUiState.Empty)
    var sendReplay: StateFlow<ReplayUiState> = _sendReplay

    fun sendReplay(sendReplay: SendReplay) = viewModelScope.launch {
        try {
            val response = repository.sendReplay(sendReplay)
            if (response.success) {
                _sendReplay.value = ReplayUiState.SendReplay(response.data)
            } else {
                _sendReplay.value = ReplayUiState.Error("Serverda xatolik yuz berdi")
            }
        } catch (e: Exception) {
            _sendReplay.value = ReplayUiState.Error(e.message.toString())
        }
    }

    private val _delete = MutableStateFlow<ReplayUiState>(ReplayUiState.Empty)
    var delete: StateFlow<ReplayUiState> = _delete

    fun delete(id: Int) = viewModelScope.launch {
        try {
            val response = repository.deleteReplay(id)
            _delete.value = ReplayUiState.Delete(response.success)
        } catch (e: Exception) {
            _delete.value = ReplayUiState.Error(e.message.toString())
        }
    }

    private val _update = MutableStateFlow<ReplayUiState>(ReplayUiState.Empty)
    var update: StateFlow<ReplayUiState> = _update
    fun sendUpdate(id: Int, body: Update) = viewModelScope.launch {

        try {
            val response = repository.updateReplay(id, body)
            if (response.success) {
                _update.value = ReplayUiState.Update(response.data)
            }
        } catch (e: Exception) {
            _update.value = ReplayUiState.Error(e.message.toString())
        }
    }


    sealed class ReplayUiState {
        data class AllReplay(val data: ArrayList<ReplayObj>) : ReplayUiState()
        data class SendReplay(val data: ReplayComment) : ReplayUiState()
        data class Delete(val status: Boolean) : ReplayUiState()
        data class Update(val status: ReplayComment) : ReplayUiState()
        data class Error(val error: String) : ReplayUiState()
        object Loading : ReplayUiState()
        object Empty : ReplayUiState()
    }
}