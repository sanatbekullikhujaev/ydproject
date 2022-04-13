package com.usc0der.ydprojectnew.api.network

import com.dasturchi.pm.data.model.AccessToken
import com.dasturchi.pm.data.model.VerifyTokenObject
import com.usc0der.ydprojectnew.model.RegResponse
import com.usc0der.ydprojectnew.eski_api.models.*
import com.usc0der.ydprojectnew.model.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import com.usc0der.ydprojectnew.eski_api.models.ChangeUser
import okhttp3.RequestBody
import org.json.JSONObject

interface ApiService {

    @POST("users/login")
    suspend fun login(
        @Body() body: LoginForm
    ): Response<LoginResponse>

    @POST("users/register")
    suspend fun registration(
        @Body() body: RegisterForm
    ): RegResponse

    @POST("users/change")
    suspend fun editPassword(
        @Body() body: ChangeUser
    ): Response<LoginResponse>

    @GET("token/refresh")
    fun checkToken(
        @Header("refresh") refresh: String?
    ): Call<AccessToken>

    @GET("token/verify")
    fun verifyToken(
        @Header("access") access: String?
    ): Call<VerifyTokenObject>

    @GET("languages/index")
    suspend fun getAllHomeList(): Home

    @GET("items/videos/")
    suspend fun getAllVideos(@Query("id") id: Int): LessonResponse

    @GET("items/audios/")
    suspend fun getAllAudios(@Query("id") id: Int): LessonResponse

    @GET("items/view")
    suspend fun getVideoAndAudio(@Body body: RequestBody): Player

    //////////// chat ///////////////////
    @POST("comments/create")
    suspend fun sendComment(@Body body: SendComment): ResponseObjError<Comment>

    @GET("comments")
    suspend fun getAllComment(@Query("itemId") id: Int): ResponseObjError<ArrayList<CommentObj>>

    @GET("answers")
    suspend fun getAllReplayByCommentId(@Query("commentId") id: Int): ResponseObjError<ArrayList<ReplayObj>>

    @POST("answers/create")
    suspend fun sendReplay(@Body body: SendReplay): ResponseObjError<ReplayComment>

    @DELETE("comments/{id}")
    suspend fun deleteComment(@Path("id") id: Int): ResponseObjError<String>

    @PUT("comments/{id}")
    suspend fun updateComment(@Path("id") id: Int, @Body body: Update): ResponseObjError<Comment>


    @DELETE("answers/{id}")
    suspend fun deleteReplay(@Path("id") id: Int): ResponseObjError<String>

    @PUT("answers/{id}")
    suspend fun updateReplay(
        @Path("id") id: Int,
        @Body body: Update
    ): ResponseObjError<ReplayComment>

    @GET("items/show")
    suspend fun getLesson(@Query("id") id: Int): Player

    @GET("questions/index")
    suspend fun getQuestion(@Query("itemId") id: Int): Response<QuestionsResponse>

    @GET("questions/history")
    suspend fun getHistoryTest(@Query("id") id: Int): Response<HistoryTestResponse>

    @POST("answers/like")
    suspend fun likeAndDislike(@Body() body: HashMap<String, Any>): Response<LikeAndDislike>

    @GET("questions/check")
    suspend fun postAnswer(@Query("json") json: String): Response<AddResultResponse>

    @GET("dictionary/index")
    suspend fun getDictionary(@Query("item_id") item_id: Int): Response<DictionaryResponse>
}