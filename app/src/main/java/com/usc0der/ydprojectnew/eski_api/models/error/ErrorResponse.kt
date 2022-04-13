package com.usc0der.ydprojectnew.eski_api.models.error

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ErrorResponse(

    @SerializedName("message")
    var message: String? = null

) : Serializable

