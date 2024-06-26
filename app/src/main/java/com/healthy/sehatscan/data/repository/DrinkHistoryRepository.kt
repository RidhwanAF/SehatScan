package com.healthy.sehatscan.data.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.healthy.sehatscan.R
import com.healthy.sehatscan.data.local.auth.AuthDataStore
import com.healthy.sehatscan.data.remote.ApiService
import com.healthy.sehatscan.data.remote.drink.response.HistoryDataItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DrinkHistoryRepository @Inject constructor(
    private val context: Context,
    private val apiService: ApiService,
    private val authDataStore: AuthDataStore
) {
    // List
    private var _drinkHistoryList = MutableStateFlow<List<HistoryDataItem>>(emptyList())
    val drinkHistoryList: StateFlow<List<HistoryDataItem>> = _drinkHistoryList

    private var _isHistoryLoading = MutableStateFlow(false)
    val isHistoryLoading: StateFlow<Boolean> = _isHistoryLoading

    private var _historyErrorMessage = MutableStateFlow<String?>(null)
    val historyErrorMessage: StateFlow<String?> = _historyErrorMessage

    init {
        CoroutineScope(Dispatchers.IO).launch {
            getDrinkHistory()
        }
    }

    // Get List
    suspend fun getDrinkHistory() {
        _historyErrorMessage.value = null
        withContext(Dispatchers.IO) {
            try {
                _isHistoryLoading.value = true
                authDataStore.getTokenPreferenceState().collect { token ->
                    val response = apiService.getDrinkHistory("Bearer $token")
                    if (response.isSuccessful) {
                        _drinkHistoryList.value = response.body()?.data ?: emptyList()
                        _isHistoryLoading.value = false
                    } else {
                        val errMsg = response.errorBody()?.string()
                        if (errMsg != null) {
                            try {
                                val json = Gson().fromJson(errMsg, JsonObject::class.java)
                                _historyErrorMessage.value =
                                    json.getAsJsonObject("meta").get("message").asString
                            } catch (e: Exception) {
                                _historyErrorMessage.value =
                                    context.getString(R.string.failed_to_get_drink_history)
                            }
                        } else {
                            _historyErrorMessage.value =
                                context.getString(R.string.failed_to_get_drink_history)
                        }
                        _isHistoryLoading.value = false
                    }
                }
            } catch (e: Exception) {
                _isHistoryLoading.value = false
                _historyErrorMessage.value =
                    context.getString(R.string.failed_to_get_drink_history)
                e.printStackTrace()
            }
        }
    }
}