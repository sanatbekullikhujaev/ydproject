package com.usc0der.ydprojectnew.api.network

import com.usc0der.ydprojectnew.eski_api.models.LoginForm
import com.usc0der.ydprojectnew.eski_api.models.RegisterForm
import com.usc0der.ydprojectnew.model.SendComment
import com.usc0der.ydprojectnew.model.SendReplay
import com.usc0der.ydprojectnew.model.Update
import com.usc0der.ydprojectnew.eski_api.models.ChangeUser
import okhttp3.RequestBody
import org.json.JSONObject

class ApiHelper(private val apiService: ApiService) {

    suspend fun getAllHomeList() = apiService.getAllHomeList()
    suspend fun getAllVideos(id:Int) = apiService.getAllVideos(id)
    suspend fun getAllAudios(id:Int) = apiService.getAllAudios(id)
    suspend fun getVideoAndAudio(body : RequestBody) = apiService.getVideoAndAudio(body)

    suspend fun login(body: LoginForm) = apiService.login(body)
    suspend fun registration(body: RegisterForm) = apiService.registration(body)
    suspend fun editPassword(body: ChangeUser) = apiService.editPassword(body)

    /// chat/////////

    suspend fun sendComment(comment: SendComment) = apiService.sendComment(comment)
    suspend fun getAllComment(id:Int) = apiService.getAllComment(id)

    suspend fun deleteComment(id:Int) = apiService.deleteComment(id)
    suspend fun updateComment(id: Int,body:Update) = apiService.updateComment(id,body)

    suspend fun getAllReplayByCommentId(id: Int) = apiService.getAllReplayByCommentId(id)
    suspend fun sendReplay(sendReplay: SendReplay) = apiService.sendReplay(sendReplay)

    suspend fun deleteReplay(id:Int) = apiService.deleteReplay(id)
    suspend fun updateReplay(id:Int,body:Update) = apiService.updateReplay(id,body)

    suspend fun getLesson(id:Int) = apiService.getLesson(id)

    suspend fun likeAndDislike(body: HashMap<String, Any>) = apiService.likeAndDislike(body)

    suspend fun getQuestion(body: Int) = apiService.getQuestion(body)
    suspend fun getHistoryTest(body: Int) = apiService.getHistoryTest(body)

    suspend fun postAnswer(body: String) = apiService.postAnswer(body)
    suspend fun getDictionary(itemId: Int) = apiService.getDictionary(itemId)

}