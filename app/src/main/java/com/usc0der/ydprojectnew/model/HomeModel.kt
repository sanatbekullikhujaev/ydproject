package com.usc0der.ydprojectnew.model

import com.google.gson.annotations.SerializedName

data class Home(

    @SerializedName("items")
    var list: List<HomeModel>,
    @SerializedName("url")
    var url: String

)

data class HomeModel(
    val icon: String,
    val id: Int,
    val name: String,
    val telegram_link: String,
    val block_audio: Boolean,
    val block_video: Boolean
)