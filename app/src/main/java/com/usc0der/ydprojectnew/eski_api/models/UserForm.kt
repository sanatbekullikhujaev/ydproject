package com.usc0der.ydprojectnew.eski_api.models

import com.google.gson.annotations.SerializedName
import com.usc0der.ydprojectnew.eski_api.models.RegisterRequest
import java.io.Serializable

class UserForm(@SerializedName("User") val registerRequest: RegisterRequest) : Serializable