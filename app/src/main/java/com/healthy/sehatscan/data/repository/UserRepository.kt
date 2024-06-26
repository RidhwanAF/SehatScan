package com.healthy.sehatscan.data.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.healthy.sehatscan.R
import com.healthy.sehatscan.data.local.auth.AuthDataStore
import com.healthy.sehatscan.data.remote.ApiService
import com.healthy.sehatscan.data.remote.user.response.HistoryAllergiesItem
import com.healthy.sehatscan.data.remote.user.response.HistoryDiseasesItem
import com.healthy.sehatscan.data.remote.user.response.UserData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val context: Context,
    private val apiService: ApiService,
    private val authDataStore: AuthDataStore,
) {
    private var _isUserDataLoading = MutableStateFlow(false)
    val isUserDataLoading: StateFlow<Boolean> = _isUserDataLoading

    private var _userDataResult = MutableStateFlow<UserData?>(null)
    val userDataResult: StateFlow<UserData?> = _userDataResult

    private var _userDataErrorMessage = MutableStateFlow<String?>(null)
    val userDataErrorMessage: StateFlow<String?> = _userDataErrorMessage

    // Allergies
    private var _userAllergies = MutableStateFlow<List<HistoryAllergiesItem>>(emptyList())
    val userAllergies: StateFlow<List<HistoryAllergiesItem>> = _userAllergies

    // Medical History
    private var _userMedicalList = MutableStateFlow<List<HistoryDiseasesItem>>(emptyList())
    val userMedicalList: StateFlow<List<HistoryDiseasesItem>> = _userMedicalList

    init {
        CoroutineScope(Dispatchers.IO).launch {
            getUserProfileData()
        }
    }

    suspend fun getUserProfileData() {
        _userDataErrorMessage.value = null
        withContext(Dispatchers.IO) {
            try {
                _isUserDataLoading.value = true
                authDataStore.getTokenPreferenceState().collect { token ->
                    if (token.isNotEmpty()) {
                        val response = apiService.getUser("Bearer $token")
                        if (response.isSuccessful) {
                            _userDataResult.value = response.body()?.data
                            _userAllergies.value =
                                response.body()?.data?.allergies ?: emptyList()
                            _userMedicalList.value =
                                response.body()?.data?.historyDiseases ?: emptyList()
                            _isUserDataLoading.value = false
                        } else {
                            val errMsg = response.errorBody()?.string()
                            if (errMsg != null) {
                                try {
                                    val json = Gson().fromJson(errMsg, JsonObject::class.java)
                                    _userDataErrorMessage.value =
                                        json.getAsJsonObject("meta").get("message").asString
                                } catch (e: Exception) {
                                    _userDataErrorMessage.value =
                                        context.getString(R.string.failed_to_get_user_data)
                                }
                            } else {
                                _userDataErrorMessage.value =
                                    context.getString(R.string.failed_to_get_user_data)
                            }
                            _isUserDataLoading.value = false
                        }
                    }
                }
            } catch (e: Exception) {
                _isUserDataLoading.value = false
                _userDataErrorMessage.value = context.getString(R.string.failed_to_get_user_data)
                e.printStackTrace()
            }
        }
    }

    fun userLogout() {
        _userDataResult.value = null
        _userAllergies.value = emptyList()
        _userMedicalList.value = emptyList()
        _userDataErrorMessage.value = null
    }
}