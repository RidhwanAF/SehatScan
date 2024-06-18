package com.healthy.sehatscan.ui.home.drink

import android.content.Context
import android.widget.Toast
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
import com.healthy.sehatscan.data.remote.drink.response.FavoriteDrink
import com.healthy.sehatscan.data.repository.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DrinkViewModel @Inject constructor(
    private val authDataStore: AuthDataStore,
    private val apiService: ApiService,
    private val favRepo: FavoriteRepository
) : ViewModel() {

    // List
    var isDrinkLoading by mutableStateOf(false)
        private set
    var drinkListResult by mutableStateOf<List<DrinkItem>>(emptyList())
        private set
    var drinkListErrorMessage by mutableStateOf<String?>(null)
        private set

    // Favorite
    val favoriteDrink = favRepo.favoriteList
    val isFavoriteLoading = favRepo.isFavoriteLoading

    private val _isAddRemoveLoading = MutableStateFlow(false)
    val isAddRemoveLoading: StateFlow<Boolean> = _isAddRemoveLoading

    private val _addRemoveErrorMessage = MutableStateFlow<String?>(null)
    val addRemoveErrorMessage: StateFlow<String?> = _addRemoveErrorMessage

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

    private fun getFavoriteDrink() {
        viewModelScope.launch(Dispatchers.IO) {
            favRepo.getUserFavorite()
        }
    }

    fun addFavorite(
        context: Context,
        data: FavoriteDrink.AddRemoveFavoriteReqBody
    ) {
        viewModelScope.launch {
            try {
                _isAddRemoveLoading.value = true
                authDataStore.getTokenPreferenceState().collect { token ->
                    val response = favRepo.addFavorite("Bearer $token", data)
                    if (response.isSuccessful) {
                        getFavoriteDrink()
                        Toast.makeText(
                            context,
                            response.body()?.meta?.message
                                ?: context.getString(R.string.success_add_to_favorite),
                            Toast.LENGTH_SHORT
                        ).show()
                        _isAddRemoveLoading.value = false
                    } else {
                        val errMsg = response.errorBody()?.string()
                        if (errMsg != null) {
                            try {
                                val json = Gson().fromJson(errMsg, JsonObject::class.java)
                                _addRemoveErrorMessage.value =
                                    json.getAsJsonObject("meta").get("message").asString
                            } catch (e: Exception) {
                                _addRemoveErrorMessage.value =
                                    context.getString(R.string.failed_to_add_favorite_drink)
                            }
                        } else {
                            _addRemoveErrorMessage.value =
                                context.getString(R.string.failed_to_add_favorite_drink)
                        }
                        _isAddRemoveLoading.value = false
                    }
                }
            } catch (e: Exception) {
                _isAddRemoveLoading.value = false
                e.printStackTrace()
            }
        }
    }

    fun removeFavorite(
        context: Context,
        data: FavoriteDrink.AddRemoveFavoriteReqBody
    ) {
        viewModelScope.launch {
            try {
                _isAddRemoveLoading.value = true
                authDataStore.getTokenPreferenceState().collect { token ->
                    val response = favRepo.removeFavorite("Bearer $token", data)
                    if (response.isSuccessful) {
                        getFavoriteDrink()
                        Toast.makeText(
                            context,
                            response.body()?.meta?.message
                                ?: context.getString(R.string.success_add_to_favorite),
                            Toast.LENGTH_SHORT
                        ).show()
                        _isAddRemoveLoading.value = false
                    } else {
                        val errMsg = response.errorBody()?.string()
                        if (errMsg != null) {
                            try {
                                val json = Gson().fromJson(errMsg, JsonObject::class.java)
                                _addRemoveErrorMessage.value =
                                    json.getAsJsonObject("meta").get("message").asString
                            } catch (e: Exception) {
                                _addRemoveErrorMessage.value =
                                    context.getString(R.string.failed_to_add_favorite_drink)
                            }
                        } else {
                            _addRemoveErrorMessage.value =
                                context.getString(R.string.failed_to_add_favorite_drink)
                        }
                        _isAddRemoveLoading.value = false
                    }
                }
            } catch (e: Exception) {
                _isAddRemoveLoading.value = false
                e.printStackTrace()
            }
        }
    }

    fun clearErrorMessage() {
        viewModelScope.launch {
            _addRemoveErrorMessage.value = null
        }
    }
}