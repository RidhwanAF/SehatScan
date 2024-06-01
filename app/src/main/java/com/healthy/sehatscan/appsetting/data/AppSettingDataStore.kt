package com.healthy.sehatscan.appsetting.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.healthy.sehatscan.appsetting.domain.AppTheme
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppSettingDataStore @Inject constructor(@ApplicationContext private val context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_settings_data_store")

    // Theme
    private val onThemeStateKey = stringPreferencesKey("theme_state_key")

    suspend fun setThemePreferenceState(value: String) {
        context.dataStore.edit { pref ->
            pref[onThemeStateKey] = value
        }
    }

    fun getThemePreferenceState(): Flow<String> = context.dataStore.data.map { pref ->
        pref[onThemeStateKey] ?: AppTheme.LIGHT.name
    }
}