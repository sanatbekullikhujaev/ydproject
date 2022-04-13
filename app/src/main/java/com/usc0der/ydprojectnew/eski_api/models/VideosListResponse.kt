package com.usc0der.ydprojectnew.connection.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

class VideosListResponse : Serializable {

    @SerializedName("Result")
    var list: LinkedList<VideosListItemResponse>? = null

}