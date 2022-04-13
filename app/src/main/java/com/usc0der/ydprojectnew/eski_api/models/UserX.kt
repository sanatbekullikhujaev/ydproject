package com.usc0der.ydprojectnew.eski_api.models


import com.google.gson.annotations.SerializedName

data class UserX(
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("username")
    val username: String
)