package com.healthy.sehatscan.ui.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.healthy.sehatscan.appsetting.domain.AppSettingRepository
import com.healthy.sehatscan.appsetting.domain.AppTheme
import com.healthy.sehatscan.data.local.AuthDataStore
import com.healthy.sehatscan.data.remote.ApiService
import com.healthy.sehatscan.data.remote.auth.response.UserForgetPassword
import com.healthy.sehatscan.data.remote.user.response.DiseaseDataItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val apiService: ApiService,
    private val appSettingRepository: AppSettingRepository,
    private val authDataStore: AuthDataStore
) : ViewModel() {
    // App Theme
    val appTheme = appSettingRepository.appTheme

    // User Data
    var email by mutableStateOf("")
        private set

    var userName by mutableStateOf("")
        private set

    var medicalListId by mutableStateOf<List<Int>>(emptyList())
        private set

    var allergy by mutableStateOf("")
        private set

    // Api Result
    var isDiseaseLoading by mutableStateOf(false)
        private set
    var diseaseListResult by mutableStateOf<List<DiseaseDataItem?>>(emptyList())
        private set
    var diseaseListErrorMessage by mutableStateOf<String?>(null)
        private set

    var isForgetPassLoading by mutableStateOf(false)
        private set
    var forgetPassErrorMessage by mutableStateOf<String?>(null)
        private set
    var forgetPasswordResult by mutableStateOf<UserForgetPassword.ForgetPasswordResponse?>(null)
        private set


    init {
        viewModelScope.launch {
            authDataStore.getNamePreferenceState().collect {
                userName = it
            }
        }
        viewModelScope.launch {
            authDataStore.getEmailPreferenceState().collect {
                email = it
            }
        }
        viewModelScope.launch {
            appSettingRepository.getThemePreferenceState()
        }
    }

    // App Theme Changes
    fun onAppThemeChange(value: AppTheme) {
        viewModelScope.launch {
            appSettingRepository.setThemePreferenceState(value)
        }
    }

    // User Data Changes
    fun onUserNameChange(value: String) {
        userName = value
    }

    fun onMedicalHistoryChange(value: Int) {
        medicalListId = if (value in medicalListId) {
            medicalListId - value
        } else {
            medicalListId + value
        }
    }

    fun onAllergyChange(value: String) {
        allergy = value
    }

    // Api Request
    fun getDiseaseList() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                authDataStore.getTokenPreferenceState().collect { token ->
                    isDiseaseLoading = true
                    val response = apiService.getDisease("Bearer $token")
                    if (response.isSuccessful) {
                        diseaseListResult = response.body()?.data ?: emptyList()
                        isDiseaseLoading = false
                    } else {
                        val errMsg = response.errorBody()?.string()
                        if (errMsg != null) {
                            try {
                                val json = Gson().fromJson(errMsg, JsonObject::class.java)
                                diseaseListErrorMessage =
                                    json.getAsJsonObject("meta").get("message").asString
                            } catch (e: Exception) {
                                diseaseListErrorMessage =
                                    "Gagal mendapatkan data penyakit, coba lagi"
                            }
                        } else {
                            diseaseListErrorMessage =
                                "Gagal mendapatkan data penyakit, coba lagi"
                        }
                        isDiseaseLoading = false
                    }
                }
            } catch (e: Exception) {
                isDiseaseLoading = false
                e.printStackTrace()
            }
        }
    }

    fun onForgetPassword() {
        viewModelScope.launch(Dispatchers.IO) {
            val requestBody = UserForgetPassword.ForgetPasswordReqBody(email = email)
            try {
                isForgetPassLoading = true
                val response = apiService.forgetPassword(requestBody)
                if (response.isSuccessful) {
                    forgetPasswordResult = response.body()
                    isForgetPassLoading = false
                } else {
                    val errMsg = response.errorBody()?.string()
                    if (errMsg != null) {
                        try {
                            val json = Gson().fromJson(errMsg, JsonObject::class.java)
                            forgetPassErrorMessage =
                                json.getAsJsonObject("meta").get("message").asString
                        } catch (e: Exception) {
                            forgetPassErrorMessage =
                                "Gagal mengirimkan tautan untuk mengganti password, coba lagi"
                        }
                    } else {
                        forgetPassErrorMessage =
                            "Gagal mengirimkan tautan untuk mengganti password, coba lagi"
                    }
                    isForgetPassLoading = false
                }
            } catch (e: Exception) {
                isForgetPassLoading = false
                e.printStackTrace()
            }
        }
    }

    // OnLogout
    fun logout() {
        viewModelScope.launch {
            authDataStore.destroySession()
        }
    }

    // Cleanup Data
    fun clearResult() {
        isForgetPassLoading = false
        forgetPassErrorMessage = null
        forgetPasswordResult = null
    }
}