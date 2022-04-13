package com.usc0der.ydprojectnew.eski_api.models

import com.google.gson.annotations.SerializedName

data class LoginForm(
    @SerializedName("LoginForm")
    val loginRequest: LoginRequest
)