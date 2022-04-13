package com.usc0der.ydprojectnew.eski_api.models.error


import com.google.gson.annotations.SerializedName

data class Errors(
    @SerializedName("errors")
    val errors: ErrorsX,
    @SerializedName("success")
    val success: Boolean
)