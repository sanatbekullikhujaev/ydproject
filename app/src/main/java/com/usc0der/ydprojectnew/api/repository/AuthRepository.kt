package com.usc0der.ydprojectnew.api.repository

import com.usc0der.ydprojectnew.api.network.ApiHelper
import com.usc0der.ydprojectnew.api.network.UIState
import com.usc0der.ydprojectnew.eski_api.models.*

class AuthRepository(private val apiHelper: ApiHelper) {

    suspend fun login(body: LoginForm): UIState<LoginData> {
        try {
            val response = apiHelper.login(body)
            if (response.isSuccessful) {
                val response = response.body()!!
                if (response.success) {
                    return UIState.Success(response.data)
                } else {
                    if (response.errors.device_id?.get(0) == null) {
                        return UIState.LoginExample(
                            "",
                            response.errors.password?.get(0).toString()
                        )

                    } else if (response.errors.password?.get(0) == null) {
                        return UIState.LoginExample(
                            response.errors.device_id?.get(0).toString(),
                            ""
                        )
                    } else {
                        return UIState.LoginExample(
                            "", ""
                        )
                    }

                }
            }


        } catch (e: Exception) {
            return UIState.Error(e.localizedMessage ?: "Unknown error")
        }
        return UIState.Empty
    }


    suspend fun reg(body: RegisterForm) = try {
        val response = apiHelper.registration(body)
        if (response.success) {
            UIState.Success(response)
        } else {
            if (response.error.count != null && response.error.count == 0) {
                UIState.LoginExample("Siz barcha imkoniyatlardan foydalanib bo'ldingiz", "")
            }
            else{
                UIState.LoginExample("Bunday login mavjud", response.examples ?: "")
            }
        }
    } catch (e: Exception) {
        UIState.Error(e.localizedMessage ?: "Unknown error")
    }
}