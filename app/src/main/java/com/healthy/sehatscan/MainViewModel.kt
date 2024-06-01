package com.healthy.sehatscan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.healthy.sehatscan.appsetting.domain.AppSettingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val appSettingRepository: AppSettingRepository
) : ViewModel() {

    // App Theme
    val appTheme = appSettingRepository.appTheme

    init {
        viewModelScope.launch {
            appSettingRepository.getThemePreferenceState()
        }
    }
}