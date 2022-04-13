package com.usc0der.ydprojectnew.eski_api.models.error


import com.google.gson.annotations.SerializedName

data class ErrorsX(
    @SerializedName("username")
    val username: List<Any>
)