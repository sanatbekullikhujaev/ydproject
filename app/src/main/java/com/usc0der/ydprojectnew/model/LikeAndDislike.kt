package com.usc0der.ydprojectnew.model

data class LikeAndDislike(
    val success: Boolean,
    val data: LikeData,
    val error:String
)

data class LikeData(
    val dislike: String,
    val like: String
)