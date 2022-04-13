package com.usc0der.ydprojectnew.eski_api.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Data : Serializable{

    @SerializedName("user")
    var user: User?= null
}