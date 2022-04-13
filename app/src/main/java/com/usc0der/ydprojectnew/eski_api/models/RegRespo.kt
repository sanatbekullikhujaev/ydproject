package com.usc0der.ydprojectnew.eski_api.models


import com.google.gson.annotations.SerializedName

data class RegRespo(
    @SerializedName("data")
    val `data`: List<RegData>,
    @SerializedName("success")
    val success: Boolean,
    val errors: RegError,
    var examples:String
)

data class RegData(
    val token:String
)

data class RegError(
    var username:List<String>
)