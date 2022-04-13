package com.usc0der.ydprojectnew.api.repository

import com.usc0der.ydprojectnew.api.network.ApiHelper
import com.usc0der.ydprojectnew.api.network.UIState
import com.usc0der.ydprojectnew.model.*
import org.json.JSONObject

class TestRepository(private val apiHelper: ApiHelper) {


    suspend fun postAnswer(body: String): UIState<AddResult> {
        try {
            val response = apiHelper.postAnswer(body)
            if (response.isSuccessful) {
                val response = response.body()!!
                return if (response.success) {
                    UIState.Success(response.data)
                } else {
                    UIState.Error("Error")
                }
            }


        } catch (e: Exception) {
            return UIState.Error(e.localizedMessage ?: "Unknown error")
        }
        return UIState.Empty
    }


    suspend fun getDictionary(itemId: Int): UIState<List<Dictionary>> {
        try {
            val response = apiHelper.getDictionary(itemId)
            if (response.isSuccessful) {
                val response = response.body()!!
                return if (response.success) {
                    UIState.Success(response.data)
                } else {
                    UIState.Error("Error")
                }
            }

        } catch (e: Exception) {
            return UIState.Error(e.localizedMessage ?: "Unknown error")
        }
        return UIState.Empty
    }


    suspend fun getQuestion(body: Int): UIState<List<Question>> {
        try {
            val response = apiHelper.getQuestion(body)
            if (response.isSuccessful) {
                val response = response.body()!!
                return if (response.success) {
                    UIState.Success(response.data)
                } else {
                    UIState.Error("error")
                }
            }


        } catch (e: Exception) {
            return UIState.Error(e.localizedMessage ?: "Unknown error")
        }
        return UIState.Empty
    }

    suspend fun getHistoryTest(body: Int): UIState<List<HistoryTest>> {
        try {
            val response = apiHelper.getHistoryTest(body)
            if (response.isSuccessful) {
                val response = response.body()!!
                return if (response.success) {
                    UIState.Success(response.data)
                } else {
                    UIState.Error("error")
                }
            }


        } catch (e: Exception) {
            return UIState.Error(e.localizedMessage ?: "Unknown error")
        }
        return UIState.Empty
    }
}