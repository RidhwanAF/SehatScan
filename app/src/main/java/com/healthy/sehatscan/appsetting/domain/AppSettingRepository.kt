package com.healthy.sehatscan.appsetting.domain

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.healthy.sehatscan.appsetting.data.AppSettingDataStore
import javax.inject.Inject

class AppSettingRepository @Inject constructor(
    private val appSettingDataStore: AppSettingDataStore
) {

    private var _appTheme = mutableStateOf(AppTheme.LIGHT)
    val appTheme: State<AppTheme> = _appTheme

    suspend fun setThemePreferenceState(value: AppTheme) {
        appSettingDataStore.setThemePreferenceState(value.value)
        _appTheme.value = value
    }

    suspend fun getThemePreferenceState() {
        appSettingDataStore.getThemePreferenceState().collect {
            _appTheme.value = AppTheme.fromString(it)
        }
    }
}