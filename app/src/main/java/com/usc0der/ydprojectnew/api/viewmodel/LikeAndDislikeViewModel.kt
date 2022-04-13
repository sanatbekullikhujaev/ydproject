package com.usc0der.ydprojectnew.api.viewmodel

import androidx.lifecycle.ViewModel
import com.usc0der.ydprojectnew.api.network.UIState
import com.usc0der.ydprojectnew.api.repository.Repository
import com.usc0der.ydprojectnew.model.LikeData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LikeAndDislikeViewModel(private val repository: Repository):ViewModel() {

    private val _likeState = MutableStateFlow<UIState<LikeData>>(UIState.Loading)
    val likeState: StateFlow<UIState<LikeData>> = _likeState
    suspend fun likeAndDislike(body: HashMap<String,Any>) {
        _likeState.value = repository.likeAndDislike(body)
    }

}