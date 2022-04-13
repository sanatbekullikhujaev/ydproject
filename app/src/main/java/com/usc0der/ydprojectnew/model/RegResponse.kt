package com.usc0der.ydprojectnew.model
import com.google.gson.annotations.SerializedName
data class RegResponse(
    @SerializedName("data")
    val data: RegData,
    @SerializedName("errors")
    val error: Errors,
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("examples")
    val examples:String?,
)
data class RegData(
    @SerializedName("count")
    val count: Int?,
    @SerializedName("device_id")
    val device_id: String?=null
)
data class Errors(
    @SerializedName("count")
    val count: Int?
)