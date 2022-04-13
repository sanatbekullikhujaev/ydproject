package com.usc0der.ydprojectnew.model

import com.google.gson.annotations.SerializedName

data class SendReplay(
    @SerializedName("comment_id")
    var comment_id: Int,
    @SerializedName("username")
    var username: String,
    @SerializedName("body")
    var body: String
)