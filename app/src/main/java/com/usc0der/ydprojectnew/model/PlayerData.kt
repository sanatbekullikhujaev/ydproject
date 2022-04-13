package com.usc0der.ydprojectnew.model

import com.google.gson.annotations.SerializedName

data class PlayerData(
    @SerializedName("0")
    val data: X0,
    val dislike: String,
    val like: String
)