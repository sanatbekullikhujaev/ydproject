package com.usc0der.ydprojectnew.eski_api.models

import com.google.gson.annotations.SerializedName
import com.usc0der.ydprojectnew.eski_api.models.Data
import java.io.Serializable

class AccountResponse : Serializable{

    @SerializedName("success")
    var success: Boolean? = null

    @SerializedName("data")
    var data: Data? = null
}