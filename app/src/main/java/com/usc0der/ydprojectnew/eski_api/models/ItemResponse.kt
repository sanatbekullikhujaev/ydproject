package com.usc0der.ydprojectnew.connection.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ItemResponse : Serializable {

    @SerializedName("id")
    var id: Int? = null

//    @SerializedName("next_id")
//    var nextId: Int? = null


    @SerializedName("title")
    var title: String? = null

    @SerializedName("video_file")
    var video_file: String? = null

    @SerializedName("audio_file")
    var audio_file: String? = null

    @SerializedName("file_name")
    var file_name: String? = null

    @SerializedName("text_file")
    var text_file: String? = null

    @SerializedName("image_file")
    var image_file: String? = null

    @SerializedName("content")
    var content: String? = null

    @SerializedName("description")
    var description: String? = null

}