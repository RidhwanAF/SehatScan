package com.healthy.sehatscan.ui.home.drink

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.healthy.sehatscan.R
import com.healthy.sehatscan.data.local.auth.AuthDataStore
import com.healthy.sehatscan.data.remote.ApiService
import com.healthy.sehatscan.data.remote.drink.response.DrinkItem
import com.healthy.sehatscan.data.remote.drink.response.DrinkRecommendReqBody
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DrinkViewModel @Inject constructor(
    private val authDataStore: AuthDataStore,
    private val apiService: ApiService
) : ViewModel() {

    var isDrinkLoading by mutableStateOf(false)
        private set
    var drinkListResult by mutableStateOf<List<DrinkItem>>(emptyList())
        private set
    var drinkListErrorMessage by mutableStateOf<String?>(null)
        private set

    fun getDrinkRecommendation(
        context: Context,
        data: DrinkRecommendReqBody
    ) {
        isDrinkLoading = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                authDataStore.getTokenPreferenceState().collect { token ->
                    val response = apiService.getDrinkRecommendation("Bearer $token", data)
                    if (response.isSuccessful) {
                        drinkListResult = response.body()?.data ?: emptyList()
                        isDrinkLoading = false
                    } else {
                        val errMsg = response.errorBody()?.string()
                        if (errMsg != null) {
                            try {
                                val json = Gson().fromJson(errMsg, JsonObject::class.java)
                                drinkListErrorMessage =
                                    json.getAsJsonObject("meta").get("message").asString
                            } catch (e: Exception) {
                                drinkListErrorMessage =
                                    context.getString(R.string.failed_to_get_fruit_data)
                            }
                        } else {
                            drinkListErrorMessage =
                                context.getString(R.string.failed_to_get_fruit_data)
                        }
                        isDrinkLoading = false
                    }
                }
            } catch (e: Exception) {
                isDrinkLoading = false
                e.printStackTrace()
            }
        }
    }

}