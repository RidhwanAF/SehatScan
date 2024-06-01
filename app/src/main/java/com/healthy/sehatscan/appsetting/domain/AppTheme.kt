package com.healthy.sehatscan.appsetting.domain

enum class AppTheme(val value: String) {
    LIGHT("light"),
    DARK("dark"),
    SYSTEM("system");

    companion object {
        fun fromString(value: String): AppTheme {
            return entries.find { it.value == value } ?: LIGHT
        }
    }
}
