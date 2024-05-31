package com.healthy.sehatscan.ui.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(

) : ViewModel() {

    var emailError by mutableStateOf(false)
        private set

    var passwordError by mutableStateOf(false)
        private set

    var confirmPasswordError by mutableStateOf(false)
        private set

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var confirmPassword by mutableStateOf("")
        private set

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
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            emailError = email.isEmpty()
            passwordError = password.isEmpty()
            confirmPasswordError = confirmPassword.isEmpty()
            return
        }
        // TODO: Connect to API
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
        // TODO: Connect to API
    }

    fun clearValidation() {
        emailError = false
        passwordError = false
        confirmPasswordError = false
    }

    fun clearData() {
        email = ""
        password = ""
        confirmPassword = ""
    }
}