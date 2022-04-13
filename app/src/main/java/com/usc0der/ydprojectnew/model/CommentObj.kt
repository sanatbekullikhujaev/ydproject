package com.usc0der.ydprojectnew.model

import com.google.gson.annotations.SerializedName

class CommentObj(
    @SerializedName("comment")
    val comment: Comment,
    val answer_count:String
)