package com.healthy.sehatscan.ui.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.healthy.sehatscan.data.remote.ApiService
import com.healthy.sehatscan.data.remote.auth.response.UserLogin
import com.healthy.sehatscan.data.remote.auth.response.UserRegister
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {

    // Result
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    var registerResult by mutableStateOf<UserRegister.RegisterResponse?>(null)
        private set
    var loginResult by mutableStateOf<UserLogin.LoginResponse?>(null)
        private set

    // User Input
    var nameError by mutableStateOf(false)
        private set

    var emailError by mutableStateOf(false)
        private set

    var passwordError by mutableStateOf(false)
        private set

    var confirmPasswordError by mutableStateOf(false)
        private set

    var name by mutableStateOf("")
        private set

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var confirmPassword by mutableStateOf("")
        private set

    fun onNameChange(value: String) {
        name = value
    }

    fun onEmailChange(value: String) {
        email = value
    }

    fun onPasswordChange(value: String) {
        password = value
    }

    fun onConfirmPasswordChange(value: String) {
        confirmPassword = value
    }

    // Validation
    fun onValidatingName(): Boolean {
        nameError = name.isEmpty()
        return !nameError
    }

    fun onValidatingEmail(): Boolean {
        emailError = !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        return !emailError
    }

    fun onValidatingPassword(): Boolean {
        passwordError = password.length < 8
        return !passwordError
    }

    fun onValidatingConfirmPassword(): Boolean {
        confirmPasswordError = confirmPassword != password
        return !confirmPasswordError
    }

    // Action
    fun register() {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            nameError = name.isEmpty()
            emailError = email.isEmpty()
            passwordError = password.isEmpty()
            confirmPasswordError = confirmPassword.isEmpty()
            return
        }
        val requestBody = UserRegister.RegisterRequestBody(
            name = name,
            email = email,
            password = password
        )
        viewModelScope.launch(Dispatchers.IO) {
            try {
                isLoading = true
                val response = apiService.register(requestBody)
                if (response.isSuccessful) {
                    registerResult = response.body()
                    isLoading = false
                } else {
                    val errMsg = response.errorBody()?.string()
                    if (errMsg != null) {
                        try {
                            val json = Gson().fromJson(errMsg, JsonObject::class.java)
                            errorMessage = json.getAsJsonObject("meta").get("message").asString
                        } catch (e: Exception) {
                            errorMessage = "Gagal"
                        }
                    } else {
                        errorMessage = "Gagal"
                    }
                    isLoading = false
                }
            } catch (e: Exception) {
                isLoading = false
                e.printStackTrace()
            }
        }
    }

    fun onForgetPassword() {
        if (email.isEmpty()) {
            emailError = email.isEmpty()
            return
        }
        // TODO: Connect to API
    }

    fun login() {
        if (email.isEmpty() || password.isEmpty()) {
            emailError = email.isEmpty()
            passwordError = password.isEmpty()
            return
        }
        val requestBody = UserLogin.LoginRequestBody(
            email = email,
            password = password
        )
        viewModelScope.launch(Dispatchers.IO) {
            try {
                isLoading = true
                val response = apiService.login(requestBody)
                if (response.isSuccessful) {
                    loginResult = response.body()
                    isLoading = false
                } else {
                    val errMsg = response.errorBody()?.string()
                    if (errMsg != null) {
                        try {
                            val json = Gson().fromJson(errMsg, JsonObject::class.java)
                            errorMessage = json.getAsJsonObject("meta").get("message").asString
                        } catch (e: Exception) {
                            errorMessage = "Gagal"
                        }
                    } else {
                        errorMessage = "Gagal"
                    }
                    isLoading = false
                }
            } catch (e: Exception) {
                isLoading = false
                e.printStackTrace()
            }
        }
    }

    // Clear
    fun clearValidation() {
        nameError = false
        emailError = false
        passwordError = false
        confirmPasswordError = false
    }

    fun clearData() {
        name = ""
        email = ""
        password = ""
        confirmPassword = ""
    }

    fun clearResult() {
        isLoading = false
        loginResult = null
        registerResult = null
        errorMessage = null
    }
}