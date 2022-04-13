package com.usc0der.ydprojectnew.eski_api.models

import com.google.gson.annotations.SerializedName

data class ChangeRequest(
    @SerializedName("password")
    var password: String? = null,
)