package com.healthy.sehatscan.data.repository

import android.content.Context
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.healthy.sehatscan.R
import com.healthy.sehatscan.data.local.auth.AuthDataStore
import com.healthy.sehatscan.data.remote.ApiService
import com.healthy.sehatscan.data.remote.drink.response.FavoriteDrink
import com.healthy.sehatscan.data.remote.drink.response.FavoriteDrink.FavoriteItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class FavoriteRepository @Inject constructor(
    private val context: Context,
    private val apiService: ApiService,
    private val authDataStore: AuthDataStore
) {
    // List
    private var _favoriteList = MutableStateFlow<List<FavoriteItem>>(emptyList())
    val favoriteList: StateFlow<List<FavoriteItem>> = _favoriteList

    private var _isFavoriteLoading = MutableStateFlow(false)
    val isFavoriteLoading: StateFlow<Boolean> = _isFavoriteLoading

    private var _favoriteErrorMessage = MutableStateFlow<String?>(null)
    val favoriteErrorMessage: StateFlow<String?> = _favoriteErrorMessage

    init {
        CoroutineScope(Dispatchers.IO).launch {
            getUserFavorite()
        }
    }

    // Get List
    suspend fun getUserFavorite() {
        withContext(Dispatchers.IO) {
            _favoriteErrorMessage.value = null
            try {
                _isFavoriteLoading.value = true
                authDataStore.getTokenPreferenceState().collect { token ->
                    val response = apiService.getFavoriteDrink("Bearer $token")
                    if (response.isSuccessful) {
                        _favoriteList.value = response.body()?.data ?: emptyList()
                        _isFavoriteLoading.value = false
                    } else {
                        val errMsg = response.errorBody()?.string()
                        if (errMsg != null) {
                            try {
                                val json = Gson().fromJson(errMsg, JsonObject::class.java)
                                _favoriteErrorMessage.value =
                                    json.getAsJsonObject("meta").get("message").asString
                            } catch (e: Exception) {
                                _favoriteErrorMessage.value =
                                    context.getString(R.string.failed_to_get_favorite_drink)
                            }
                        } else {
                            _favoriteErrorMessage.value =
                                context.getString(R.string.failed_to_get_favorite_drink)
                        }
                        _isFavoriteLoading.value = false
                    }
                }
            } catch (e: Exception) {
                _isFavoriteLoading.value = false
                _favoriteErrorMessage.value =
                    context.getString(R.string.failed_to_get_favorite_drink)
                e.printStackTrace()
            }
        }
    }

    // Add Remove Favorite
    suspend fun addFavorite(
        token: String,
        data: FavoriteDrink.AddRemoveFavoriteReqBody
    ): Response<FavoriteDrink.AddRemoveResponse> =
        apiService.addRemoveFavorite(token, "like", data)

    suspend fun removeFavorite(
        token: String,
        data: FavoriteDrink.AddRemoveFavoriteReqBody
    ): Response<FavoriteDrink.AddRemoveResponse> =
        apiService.addRemoveFavorite(token, "unlike", data)
}