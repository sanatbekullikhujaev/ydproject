package com.usc0der.ydprojectnew.api.repository

import android.util.Log
import com.usc0der.ydprojectnew.api.network.ApiHelper
import com.usc0der.ydprojectnew.api.network.UIState
import com.usc0der.ydprojectnew.model.LikeData
import com.usc0der.ydprojectnew.model.SendComment
import com.usc0der.ydprojectnew.model.SendReplay
import com.usc0der.ydprojectnew.model.Update
import okhttp3.RequestBody

class Repository(private val apiHelper: ApiHelper) {

    suspend fun getAllHomeList() = try {
        UIState.Success(apiHelper.getAllHomeList())

    } catch (e: Exception) {
        UIState.Error(e.message.toString())
    }

    suspend fun getAllVideos(id: Int)  = try {
        val response = apiHelper.getAllVideos(id)
        if(response.error_message.isNullOrBlank()){
            UIState.Success(response.data)
        }
        else{
            UIState.Error(response.error_message)
        }
    }catch (e:Exception){
        UIState.Error(e.message.toString())
    }
    suspend fun getAllAudios(id: Int)  = try {
        val response = apiHelper.getAllAudios(id)
        if(response.error_message.isNullOrBlank()){
            UIState.Success(response.data)
        }
        else{
            UIState.Error(response.error_message)
        }
    }catch (e:Exception){
        UIState.Error(e.message.toString())
    }
    suspend fun getVideoAndAudio(body: RequestBody) = apiHelper.getVideoAndAudio(body)

    /////// chat//////

    suspend fun sendComment(comment: SendComment) = apiHelper.sendComment(comment)
    suspend fun getAllComment(id: Int) = apiHelper.getAllComment(id)

    suspend fun deleteComment(id: Int) = apiHelper.deleteComment(id)
    suspend fun updateComment(id: Int, body: Update) = apiHelper.updateComment(id, body)

    suspend fun getAllReplayByCommentId(id: Int) = apiHelper.getAllReplayByCommentId(id)
    suspend fun sendReplay(sendReplay: SendReplay) = apiHelper.sendReplay(sendReplay)

    suspend fun deleteReplay(id: Int) = apiHelper.deleteReplay(id)
    suspend fun updateReplay(id: Int, body: Update) = apiHelper.updateReplay(id, body)

    suspend fun getLesson(id: Int) = apiHelper.getLesson(id)
    //suspend fun likeAndDislike(body:HashMap<String,String>) = apiHelper.likeAndDislike(body)



    suspend fun likeAndDislike(body: HashMap<String, Any>): UIState<LikeData> {
        try {
            val response = apiHelper.likeAndDislike(body)
            if (response.isSuccessful) {
                val response = response.body()!!
                return if (response.success) {
                    UIState.Success(response.data)
                } else {
                    UIState.Error(response.error)
                }
            }


        } catch (e: Exception) {
            return UIState.Error(e.localizedMessage ?: "Unknown error")
        }
        return UIState.Empty
    }
}