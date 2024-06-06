package com.healthy.sehatscan.data.repository

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.healthy.sehatscan.data.remote.ApiService
import com.healthy.sehatscan.data.remote.user.response.HistoryAllergiesItem
import com.healthy.sehatscan.data.remote.user.response.HistoryDiseasesItem
import com.healthy.sehatscan.data.remote.user.response.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val apiService: ApiService
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

    suspend fun getUserProfileData(token: String) {
        withContext(Dispatchers.IO) {
            try {
                _isUserDataLoading.value = true
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
                                "Gagal mendapatkan data pengguna"
                        }
                    } else {
                        _userDataErrorMessage.value =
                            "Gagal mendapatkan data pengguna"
                    }
                    _isUserDataLoading.value = false
                }
            } catch (e: Exception) {
                _isUserDataLoading.value = false
                e.printStackTrace()
            }
        }
    }
}