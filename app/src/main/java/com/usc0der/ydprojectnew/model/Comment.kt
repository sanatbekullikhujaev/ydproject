package com.usc0der.ydprojectnew.model

import com.google.gson.annotations.SerializedName

data class Comment(
    @SerializedName("body")
    var body: String,
    @SerializedName("created_time")
    var created_time: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("item_id")
    val item_id: Int,
    @SerializedName("update_time")
    val update_time: String,
    @SerializedName("username")
    val username: String
)