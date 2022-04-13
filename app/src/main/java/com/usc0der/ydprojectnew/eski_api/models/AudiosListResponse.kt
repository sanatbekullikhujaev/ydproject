package com.usc0der.ydprojectnew.api.models

import com.google.gson.annotations.SerializedName
import com.usc0der.ydprojectnew.connection.models.AudiosListItemResponse
import java.io.Serializable
import java.util.*

class AudiosListResponse : Serializable {

    @SerializedName("Result")
    var list: LinkedList<AudiosListItemResponse>? = null

}