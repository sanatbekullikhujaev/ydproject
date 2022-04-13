package com.usc0der.ydprojectnew.api.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.usc0der.ydprojectnew.api.network.UIState
import com.usc0der.ydprojectnew.api.repository.Repository
import com.usc0der.ydprojectnew.model.VideosAndAudios
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VideoAndAudioViewModel(private val  repository: Repository):ViewModel() {

    private val _audioSateFlow = MutableStateFlow<UIState<List<VideosAndAudios>>>(UIState.Empty)
    val audioSateFlow : StateFlow<UIState<List<VideosAndAudios>>> = _audioSateFlow

    private val _videoSateFlow = MutableStateFlow<UIState<List<VideosAndAudios>>>(UIState.Empty)
    val videoSateFlow : StateFlow<UIState<List<VideosAndAudios>>> = _videoSateFlow

    fun initAudio(id:Int) = viewModelScope.launch(Dispatchers.IO) {
        _audioSateFlow.value = UIState.Loading
        _audioSateFlow.value = repository.getAllAudios(id)
    }
   fun initVideo(id: Int) = viewModelScope.launch (Dispatchers.IO){
       _videoSateFlow.value = UIState.Loading
       _videoSateFlow.value = repository.getAllVideos(id)
   }

}