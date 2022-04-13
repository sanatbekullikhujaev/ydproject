package com.usc0der.ydprojectnew.connection.models.error

import com.usc0der.ydprojectnew.eski_api.models.error.ErrorResponse
import com.usc0der.ydprojectnew.utils.Constants
import okhttp3.ResponseBody
import org.json.JSONObject

class ErrorParser {

    companion object {

        private const val UNKNOWN_ERROR = "Unknown error structure, we can not find error message ((("

        @JvmStatic
        fun getError(errorBody: ResponseBody?): ErrorResponse? {
            val errorDTO = ErrorResponse()

            try {
                val jsonObject = JSONObject(errorBody?.string())

                if (jsonObject.has(Constants.ERRORS)
                    && jsonObject.getJSONObject(Constants.ERRORS).has(Constants.MESSAGE)
                )
                    errorDTO.message = jsonObject.getJSONObject(Constants.ERRORS).getString(Constants.MESSAGE)
                else
                    errorDTO.message = UNKNOWN_ERROR

            } catch (e: Exception) {
                e.printStackTrace()
            }

            return errorDTO
        }

    }

}
