package com.usc0der.ydprojectnew.connection.models

import com.google.gson.annotations.SerializedName

class LanguagesResponse {

    @SerializedName("id")
    var id: Int? = null


    @SerializedName("name")
    var name: String? = null

    @SerializedName("icon")
    var icon: String? = null

}