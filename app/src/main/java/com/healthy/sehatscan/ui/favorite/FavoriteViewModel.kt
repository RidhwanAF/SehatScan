package com.healthy.sehatscan.ui.favorite

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.healthy.sehatscan.R
import com.healthy.sehatscan.data.local.auth.AuthDataStore
import com.healthy.sehatscan.data.remote.drink.response.FavoriteDrink
import com.healthy.sehatscan.data.repository.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val authDataStore: AuthDataStore,
    private val repository: FavoriteRepository
) : ViewModel() {

    // List
    val favoriteDrink = repository.favoriteList
    val isFavoriteLoading = repository.isFavoriteLoading
    val favoriteErrorMessage = repository.favoriteErrorMessage

    // Add Remove Favorite
    private val _isAddRemoveLoading = MutableStateFlow(false)
    val isAddRemoveLoading: StateFlow<Boolean> = _isAddRemoveLoading

    private val _addRemoveErrorMessage = MutableStateFlow<String?>(null)
    val addRemoveErrorMessage: StateFlow<String?> = _addRemoveErrorMessage

    fun getFavoriteDrink() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getUserFavorite()
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
                    val response = repository.addFavorite("Bearer $token", data)
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
                    val response = repository.removeFavorite("Bearer $token", data)
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