package com.healthy.sehatscan.ui.profile

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.healthy.sehatscan.R
import com.healthy.sehatscan.appsetting.domain.AppSettingRepository
import com.healthy.sehatscan.appsetting.domain.AppTheme
import com.healthy.sehatscan.data.local.auth.AuthDataStore
import com.healthy.sehatscan.data.remote.ApiService
import com.healthy.sehatscan.data.remote.auth.response.UserForgetPassword
import com.healthy.sehatscan.data.remote.disease.response.DiseaseDataItem
import com.healthy.sehatscan.data.remote.fruit.response.FruitItem
import com.healthy.sehatscan.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val apiService: ApiService,
    private val appSettingRepository: AppSettingRepository,
    private val authDataStore: AuthDataStore,
    private val userRepo: UserRepository
) : ViewModel() {
    // App Theme
    val appTheme = appSettingRepository.appTheme

    // Api Result
    val isUserDataLoading = userRepo.isUserDataLoading
    val userDataResult = userRepo.userDataResult
    val userDataErrorMessage = userRepo.userDataErrorMessage

    val userAllergies = userRepo.userAllergies
    val userMedicalHistory = userRepo.userMedicalList

    // User Data Holder
    var isDiseaseLoading by mutableStateOf(false)
        private set
    var diseaseListResult by mutableStateOf<List<DiseaseDataItem?>>(emptyList())
        private set
    var diseaseListErrorMessage by mutableStateOf<String?>(null)
        private set

    var isFruitLoading by mutableStateOf(false)
        private set
    var fruitListResult by mutableStateOf<List<FruitItem?>>(emptyList())
        private set
    var fruitListErrorMessage by mutableStateOf<String?>(null)
        private set

    var isForgetPassLoading by mutableStateOf(false)
        private set
    var forgetPassErrorMessage by mutableStateOf<String?>(null)
        private set
    var forgetPasswordResult by mutableStateOf<UserForgetPassword.ForgetPasswordResponse?>(null)
        private set

    // User Data
    var email by mutableStateOf("")
        private set

    var userName by mutableStateOf("")
        private set

    var medicalListId by mutableStateOf(emptyList<Int>())
        private set

    var allergyListId by mutableStateOf(emptyList<Int>())
        private set


    init {
        if (userDataResult.value == null) {
            getUserProfileData()
        }
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

    fun onMedicalHistoryChange(value: List<Int>) {
        medicalListId = value
    }

    fun onAllergyChange(value: Int) {
        allergyListId = if (value in allergyListId) {
            allergyListId - value
        } else {
            allergyListId + value
        }
    }

    fun onAllergyChange(value: List<Int>) {
        allergyListId = value
    }

    // Api Request
    private fun getUserProfileData() {
        viewModelScope.launch(Dispatchers.IO) {
            authDataStore.getTokenPreferenceState().collect { token ->
                userRepo.getUserProfileData(token)
            }
        }
    }

    fun getDiseaseList(
        context: Context
    ) {
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
                                    context.getString(R.string.failed_to_get_disease_data)
                            }
                        } else {
                            diseaseListErrorMessage =
                                context.getString(R.string.failed_to_get_disease_data)
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

    fun getFruitList(
        context: Context
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                authDataStore.getTokenPreferenceState().collect { token ->
                    isFruitLoading = true
                    val response = apiService.getFruit("Bearer $token")
                    if (response.isSuccessful) {
                        fruitListResult = response.body()?.data ?: emptyList()
                        isFruitLoading = false
                    } else {
                        val errMsg = response.errorBody()?.string()
                        if (errMsg != null) {
                            try {
                                val json = Gson().fromJson(errMsg, JsonObject::class.java)
                                fruitListErrorMessage =
                                    json.getAsJsonObject("meta").get("message").asString
                            } catch (e: Exception) {
                                fruitListErrorMessage =
                                    context.getString(R.string.failed_to_get_fruit_data)
                            }
                        } else {
                            fruitListErrorMessage =
                                context.getString(R.string.failed_to_get_fruit_data)
                        }
                        isFruitLoading = false
                    }
                }
            } catch (e: Exception) {
                isFruitLoading = false
                e.printStackTrace()
            }
        }
    }

    fun onForgetPassword(
        context: Context
    ) {
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
                                context.getString(R.string.failed_to_send_reset_password_link)
                        }
                    } else {
                        forgetPassErrorMessage =
                            context.getString(R.string.failed_to_send_reset_password_link)
                    }
                    isForgetPassLoading = false
                }
            } catch (e: Exception) {
                isForgetPassLoading = false
                e.printStackTrace()
            }
        }
    }

    // Update Allergies
    var isUpdateAllergiesLoading by mutableStateOf(false)
        private set
    var updateAllergiesErrorMessage by mutableStateOf<String?>(null)
        private set

    fun updateAllergies(
        context: Context
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                isUpdateAllergiesLoading = true
                authDataStore.getTokenPreferenceState().collect { token ->
                    val response = apiService.updateAllergies(
                        "Bearer $token",
                        allergyListId
                    )
                    if (response.isSuccessful) {
                        getUserProfileData()
                        isUpdateAllergiesLoading = false
                    } else {
                        val errMsg = response.errorBody()?.string()
                        if (errMsg != null) {
                            try {
                                val json = Gson().fromJson(errMsg, JsonObject::class.java)
                                updateAllergiesErrorMessage =
                                    json.getAsJsonObject("meta").get("message").asString
                            } catch (e: Exception) {
                                updateAllergiesErrorMessage =
                                    context.getString(R.string.failed_to_update_allergies)
                            }
                        } else {
                            updateAllergiesErrorMessage =
                                context.getString(R.string.failed_to_update_allergies)
                        }
                        isUpdateAllergiesLoading = false
                    }
                }
            } catch (e: Exception) {
                isUpdateAllergiesLoading = false
                e.printStackTrace()
            } finally {
                updateAllergiesErrorMessage = null
            }
        }
    }

    // Update Disease
    var isUpdateDiseaseLoading by mutableStateOf(false)
        private set
    var updateDiseaseErrorMessage by mutableStateOf<String?>(null)
        private set

    fun updateDisease(
        context: Context
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                isUpdateDiseaseLoading = true
                authDataStore.getTokenPreferenceState().collect { token ->
                    val response = apiService.updateDisease(
                        "Bearer $token",
                        medicalListId
                    )
                    if (response.isSuccessful) {
                        getUserProfileData()
                        isUpdateDiseaseLoading = false
                    } else {
                        val errMsg = response.errorBody()?.string()
                        if (errMsg != null) {
                            try {
                                val json = Gson().fromJson(errMsg, JsonObject::class.java)
                                updateDiseaseErrorMessage =
                                    json.getAsJsonObject("meta").get("message").asString
                            } catch (e: Exception) {
                                updateDiseaseErrorMessage =
                                    context.getString(R.string.failed_to_update_medical_history)
                            }
                        } else {
                            updateDiseaseErrorMessage =
                                context.getString(R.string.failed_to_update_medical_history)
                        }
                        isUpdateDiseaseLoading = false
                    }
                }
            } catch (e: Exception) {
                isUpdateDiseaseLoading = false
                e.printStackTrace()
            } finally {
                updateDiseaseErrorMessage = null
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
        isDiseaseLoading = false
        diseaseListErrorMessage = null
        updateDiseaseErrorMessage = null
        updateAllergiesErrorMessage = null
        isFruitLoading = false
    }
}