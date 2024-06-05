package com.healthy.sehatscan.data.local.auth

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthDataStore @Inject constructor(@ApplicationContext private val context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_data_store")

    private val onTokenStateKey = stringPreferencesKey("on_token_state_key")
    private val onEmailStateKey = stringPreferencesKey("on_email_state_key")
    private val onNameStateKey = stringPreferencesKey("on_name_state_key")

    suspend fun setTokenPreferenceState(value: String) {
        context.dataStore.edit { pref ->
            pref[onTokenStateKey] = value
        }
    }

    fun getTokenPreferenceState(): Flow<String> = context.dataStore.data.map { pref ->
        pref[onTokenStateKey] ?: ""
    }

    suspend fun setUserData(email: String, name: String)  {
        context.dataStore.edit { pref ->
            pref[onEmailStateKey] = email
            pref[onNameStateKey] = name
        }
    }

    fun getEmailPreferenceState(): Flow<String> = context.dataStore.data.map { pref ->
        pref[onEmailStateKey] ?: ""
    }

    fun getNamePreferenceState(): Flow<String> = context.dataStore.data.map { pref ->
        pref[onNameStateKey] ?: ""
    }

    suspend fun destroySession() {
        context.dataStore.edit { pref ->
            pref.clear()
        }
    }
}