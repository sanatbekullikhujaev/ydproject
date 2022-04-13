package com.usc0der.ydprojectnew.connection.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

class LanguagesListResponse : Serializable {

    @SerializedName("items")
    var list: LinkedList<LanguagesListItemResponse>? = null

    @SerializedName("url")
    var url: String? = null

}