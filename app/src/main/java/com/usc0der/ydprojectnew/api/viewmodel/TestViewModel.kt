package com.usc0der.ydprojectnew.api.viewmodel

import androidx.lifecycle.ViewModel
import com.usc0der.ydprojectnew.api.network.UIState
import com.usc0der.ydprojectnew.api.repository.TestRepository
import com.usc0der.ydprojectnew.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.json.JSONObject

class TestViewModel(private val repository: TestRepository)  : ViewModel()  {

    private val _questionState = MutableStateFlow<UIState<List<Question>>>(UIState.Loading)
    val questionState: StateFlow<UIState<List<Question>>> = _questionState
    suspend fun getQuestion(body: Int) {
        _questionState.value = repository.getQuestion(body)
    }

    private val _historyTestState = MutableStateFlow<UIState<List<HistoryTest>>>(UIState.Loading)
    val historyTestState: StateFlow<UIState<List<HistoryTest>>> = _historyTestState
    suspend fun getHistoryTest(body: Int) {
        _historyTestState.value = repository.getHistoryTest(body)
    }



    private val _resultState = MutableStateFlow<UIState<AddResult>>(UIState.Loading)
    val resultState: StateFlow<UIState<AddResult>> = _resultState
    suspend fun postResult(body: String) {
        _resultState.value = repository.postAnswer(body)
    }

    private val _dictionaryState = MutableStateFlow<UIState<List<Dictionary>>>(UIState.Loading)
    val dictionaryState: StateFlow<UIState<List<Dictionary>>> = _dictionaryState
    suspend fun getDictionary(itemId: Int) {
        _dictionaryState.value = repository.getDictionary(itemId)
    }

}