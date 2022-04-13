package com.usc0der.ydprojectnew.api.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.usc0der.ydprojectnew.api.repository.Repository
import com.usc0der.ydprojectnew.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CommentViewModel(private val id:Int, private val repository: Repository):ViewModel() {

    private val _commentSateFlow = MutableStateFlow<ChatUiSate>(ChatUiSate.Loading)
    val commentSateFlow :StateFlow<ChatUiSate> = _commentSateFlow

    private val _getAllComment = MutableStateFlow<ChatUiSate>(ChatUiSate.Loading)
    val getAllComment :StateFlow<ChatUiSate> = _getAllComment

    fun sendComment(comment: SendComment) = viewModelScope.launch {
        try {
            val response = repository.sendComment(comment)
            if(response.success )
            _commentSateFlow.value = ChatUiSate.SendCommentSuccess(response.data)
            else{
                _commentSateFlow.value = ChatUiSate.Error("Serverda xatolik yuz berdi ${response.toString()}")
            }
        }
        catch (e:Exception){
            _commentSateFlow.value = ChatUiSate.Error(e.message.toString())
        }
    }
    init {
        initAllComment(id)
    }

    fun initAllComment(id:Int) = viewModelScope.launch {
        try {
            val response = repository.getAllComment(id)
            if(response.success){
                _getAllComment.value = ChatUiSate.AllComment(response.data)
            }
            else{
                _getAllComment.value = ChatUiSate.Error("Chatni boshlang")
            }
        }
        catch (e:Exception){
            _getAllComment.value = ChatUiSate.Error(e.message.toString())
        }
    }

    private val _delete = MutableStateFlow<ChatUiSate>(ChatUiSate.Empty)
    val deleteStateFlow : StateFlow<ChatUiSate> = _delete
    fun deleteComment(id: Int) = viewModelScope.launch {
        try {
            val response = repository.deleteComment(id)
            _delete.value = ChatUiSate.Delete(response.success)
        }
        catch (e:Exception){
            _delete.value = ChatUiSate.Error(e.message.toString())
        }
    }
    private val _update = MutableStateFlow<ChatUiSate>(ChatUiSate.Empty)
    val updateStateFlow : StateFlow<ChatUiSate> = _update
    fun updateComment(id: Int, body: Update) = viewModelScope.launch {
        try {
            val response = repository.updateComment(id,body)
            if(response.success){
            _update.value = ChatUiSate.Update(response.data)
            }
        }
        catch (e:Exception){
            _update.value = ChatUiSate.Error(e.message.toString())
        }
    }
    sealed class ChatUiSate{
        data class SendCommentSuccess(val comment: Comment):ChatUiSate()
        data class AllComment(val data:ArrayList<CommentObj>):ChatUiSate()
        data class Delete(val status:Boolean):ChatUiSate()
        data class Update(val data: Comment):ChatUiSate()
        data class Error(val error:String):ChatUiSate()
        object Loading:ChatUiSate()
        object Empty:ChatUiSate()
    }
}
