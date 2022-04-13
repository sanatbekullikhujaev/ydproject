package com.usc0der.ydprojectnew.model

import com.google.gson.annotations.SerializedName

data class ReplayComment(
    @SerializedName("body")
    var body: String,
    @SerializedName("comment_id")
    val comment_id: Int,
    @SerializedName("created_time")
    var created_date: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("spamCount")
    val spamCount: Int,
    @SerializedName("update_time")
    val update_time: String,
    @SerializedName("username")
    val username: String
)