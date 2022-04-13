package com.usc0der.ydprojectnew.eski_api.models

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("username")
    var login: String? = null,

    @SerializedName("password")
    var password: String? = null,

    @SerializedName("phone")
    var phone: String? = null,

    @SerializedName("full_name")
    var full_name: String? = null,
    @SerializedName("device_id")
    var device_id: String? = null

)