package com.usc0der.ydprojectnew.eski_api.models

import com.google.gson.annotations.SerializedName

class RestResponse<T> {

    @SerializedName("data")
    var result: T? = null
}
