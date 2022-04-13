package com.usc0der.ydprojectnew.eski_api.models


import com.google.gson.annotations.SerializedName

data class RegResponse(
    @SerializedName("User")
    val user: UserX
)