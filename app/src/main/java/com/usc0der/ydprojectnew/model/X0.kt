package com.usc0der.ydprojectnew.model

import com.google.gson.annotations.Expose

data class X0(
    val audio_file: String?,
    val content: String?,
    val description: String?,
    val file_name: String?,
    val free: Int?,
    val id: Int?,
    val image_file: String?,
    val next_id: Any?,
    val text_file: String?,
    val title: String?,
    var video_file: String?,
    var file_path:String?,
    @Expose
    var telegram_link:String,
)