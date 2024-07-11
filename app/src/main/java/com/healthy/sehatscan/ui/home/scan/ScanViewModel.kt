package com.healthy.sehatscan.ui.home.scan

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.healthy.sehatscan.R
import com.healthy.sehatscan.classification.domain.Classification
import com.healthy.sehatscan.data.local.auth.AuthDataStore
import com.healthy.sehatscan.data.remote.ApiService
import com.healthy.sehatscan.data.remote.fruit.response.FruitDetailItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(
    private val apiService: ApiService,
    private val authDataStore: AuthDataStore
) : ViewModel() {

    var scanResult by mutableStateOf<Classification?>(null)
        private set

    fun onScanResultChange(results: Classification) {
        scanResult = results
    }

    // Fruit
    private val _isFruitLoading = MutableStateFlow(false)
    val isFruitLoading: StateFlow<Boolean> = _isFruitLoading

    private val _fruitDetailErrorMessage = MutableStateFlow<String?>(null)
    val fruitDetailErrorMessage: StateFlow<String?> = _fruitDetailErrorMessage

    private val _fruitDetail = MutableStateFlow<FruitDetailItem?>(null)
    val fruitDetail: StateFlow<FruitDetailItem?> = _fruitDetail

    // Api Call
    fun getFruitDetail(
        context: Context,
        fruitName: String
    ) {
        _isFruitLoading.value = true
        _fruitDetailErrorMessage.value = null
        viewModelScope.launch(Dispatchers.IO) {
            try {
                authDataStore.getTokenPreferenceState().collect { token ->
                    val response = apiService.getFruitDetail("Bearer $token", fruitName)
                    if (response.isSuccessful) {
                        _fruitDetail.value = response.body()?.data?.firstOrNull()
                        _isFruitLoading.value = false
                    } else {
                        val errMsg = response.errorBody()?.string()
                        if (errMsg != null) {
                            try {
                                val json = Gson().fromJson(errMsg, JsonObject::class.java)
                                _fruitDetailErrorMessage.value =
                                    json.getAsJsonObject("meta").get("message").asString
                            } catch (e: Exception) {
                                _fruitDetailErrorMessage.value =
                                    context.getString(R.string.failed_to_get_fruit_data)
                            }
                        } else {
                            _fruitDetailErrorMessage.value =
                                context.getString(R.string.failed_to_get_fruit_data)
                        }
                        _isFruitLoading.value = false
                    }
                }
            } catch (e: Exception) {
                _fruitDetailErrorMessage.value =
                    context.getString(R.string.failed_to_get_fruit_data)
                _isFruitLoading.value = false
                e.printStackTrace()
            }
        }
    }

    fun clearFruitDetailState() {
        _fruitDetailErrorMessage.value = null
    }
}