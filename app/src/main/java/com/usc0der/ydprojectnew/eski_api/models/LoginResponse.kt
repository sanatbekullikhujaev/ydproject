package com.usc0der.ydprojectnew.eski_api.models

data class LoginResponse (

    var success:Boolean,
    var data:LoginData,
    var errors: LoginError,
    var examples:String

)

data class LoginData(
    var token:String,
    var end_date:String,
    var count:Int
)
data class LoginError(
    var password:List<String>?,
    var device_id:List<String>?
)