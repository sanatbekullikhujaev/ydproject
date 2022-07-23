package com.usc0der.ydprojectnew.api.service

import com.usc0der.ydprojectnew.eski_api.models.LoginForm
import com.usc0der.ydprojectnew.eski_api.models.LoginResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

// Created by usc0der on 02.07.2022.

interface LoginService {

    @POST("users/login")
    suspend fun login(
        @Body() body: RequestBody
    ): Response<LoginResponse>

    @POST("")
    suspend fun checkDeviceId(@Body body: RequestBody)
}