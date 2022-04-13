package com.usc0der.ydprojectnew.eski_api.models

import com.google.gson.annotations.SerializedName
import com.usc0der.ydprojectnew.eski_api.models.ChangeRequest

data class ChangeUser(
    @SerializedName("User")
    val changeRequest: ChangeRequest
)