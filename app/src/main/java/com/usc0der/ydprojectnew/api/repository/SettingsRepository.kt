package com.usc0der.ydprojectnew.api.repository

import com.usc0der.ydprojectnew.api.network.ApiHelper
import com.usc0der.ydprojectnew.api.network.UIState
import com.usc0der.ydprojectnew.eski_api.models.LoginData
import com.usc0der.ydprojectnew.eski_api.models.ChangeUser

class SettingsRepository (private val apiHelper: ApiHelper){

    suspend fun editPassword(body: ChangeUser): UIState<LoginData> {
        try {
            val response = apiHelper.editPassword(body)
            if (response.isSuccessful) {
                val response = response.body()!!
                if (response.success) {
                    return UIState.Success(response.data)
                }
            } else if (response.code() == 401) {
                return UIState.Error("Unauthorized")
            } else if (response.code() == 500) {
                return UIState.Error("Server xatoligi")
            }
        } catch (e: Exception) {
            return UIState.Error(e.localizedMessage ?: "Unknown error")
        }
        return UIState.Empty
    }
}