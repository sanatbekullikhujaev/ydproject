package com.usc0der.ydprojectnew.api.repository

import com.usc0der.ydprojectnew.api.network.UIState
import com.usc0der.ydprojectnew.api.service.LoginService
import okhttp3.RequestBody
import javax.inject.Inject


class LoginRepository @Inject constructor(val service: LoginService) {

    suspend fun login(body: RequestBody) = try {
        UIState.Success(service.login(body).body())
    } catch (e: Exception) {
    }
}