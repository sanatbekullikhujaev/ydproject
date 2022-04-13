package com.usc0der.ydprojectnew.model

data class ResponseObjError<T>(
    val success: Boolean,
    val data:T
    )
