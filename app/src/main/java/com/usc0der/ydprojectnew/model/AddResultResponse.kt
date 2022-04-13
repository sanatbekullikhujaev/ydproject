package com.usc0der.ydprojectnew.model

data class AddResultResponse(
    var success: Boolean,
    var data: AddResult
)

data class AddResult(
    var historyId: Int,
    var countAnswers: Int,
    var correctAnswers: Int,
    var percent: Float
)