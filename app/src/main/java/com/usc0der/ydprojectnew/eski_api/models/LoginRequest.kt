package com.usc0der.ydprojectnew.eski_api.models

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("username")
    var login: String? = null,
    @SerializedName("password")
    var password: String? = null,
    @SerializedName("device_id")
    var device_id: String? = null,
    @SerializedName("firebase_key")
    var firebase_key: String? = null

)


