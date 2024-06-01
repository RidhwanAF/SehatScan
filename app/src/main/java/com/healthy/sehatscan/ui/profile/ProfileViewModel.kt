package com.healthy.sehatscan.ui.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthy.sehatscan.appsetting.domain.AppSettingRepository
import com.healthy.sehatscan.appsetting.domain.AppTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val appSettingRepository: AppSettingRepository
) : ViewModel() {

    // App Theme
    val appTheme = appSettingRepository.appTheme

    // User Data
    var userName by mutableStateOf("")
        private set

    var medicalHistory by mutableStateOf("")
        private set

    var allergy by mutableStateOf("")
        private set

    init {
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

    fun onMedicalHistoryChange(value: String) {
        medicalHistory = value
    }

    fun onAllergyChange(value: String) {
        allergy = value
    }

}