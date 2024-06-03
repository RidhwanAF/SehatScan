package com.healthy.sehatscan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthy.sehatscan.appsetting.domain.AppSettingRepository
import com.healthy.sehatscan.data.local.AuthDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val appSettingRepository: AppSettingRepository,
    private val authDataStore: AuthDataStore
) : ViewModel() {

    // Auth
    var userToken by mutableStateOf("")
        private set

    // User Data
    var userName by mutableStateOf("")
        private set


    // App Theme
    val appTheme = appSettingRepository.appTheme

    init {
        viewModelScope.launch(Dispatchers.IO) {
            authDataStore.getTokenPreferenceState().collect {
                userToken = it
            }
        }
        viewModelScope.launch {
            authDataStore.getNamePreferenceState().collect {
                userName = it
            }
        }
        viewModelScope.launch {
            appSettingRepository.getThemePreferenceState()
        }
    }
}