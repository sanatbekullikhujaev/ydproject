package com.usc0der.ydprojectnew.model

import com.google.gson.annotations.SerializedName

class LessonResponse(
    @SerializedName("error_message")
    val error_message:String,
    @SerializedName("data")
    val data:List<VideosAndAudios>
)