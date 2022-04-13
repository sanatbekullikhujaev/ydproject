package com.usc0der.ydprojectnew.eski_api.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class User : Serializable{

    @SerializedName("full_name")
    var full_name: String? = null

    @SerializedName("phone")
    var phone: String? = null
}