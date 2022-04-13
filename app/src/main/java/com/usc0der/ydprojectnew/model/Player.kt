package com.usc0der.ydprojectnew.model

import com.google.gson.annotations.SerializedName

data class Player(
    val data: PlayerData?,
    val success: Boolean?,
    @SerializedName("errors")
    var errors:String?,
)