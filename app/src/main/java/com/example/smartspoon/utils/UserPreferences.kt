package com.example.smartspoon.utils

import android.content.Context
import android.content.SharedPreferences

class UserPreferences(context: Context) {
    private val preferences: SharedPreferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_DISPLAY_NAME = "display_name"
        private const val KEY_EMAIL = "email"
    }

    fun saveUserData(email: String, displayName: String?) {
        preferences.edit().apply {
            putString(KEY_EMAIL, email)
            putString(KEY_DISPLAY_NAME, displayName ?: "")
            apply()
        }
    }

    fun saveUserDisplayName(displayName: String?) {
        preferences.edit().putString(KEY_DISPLAY_NAME, displayName ?: "").apply()
    }

    fun getUserDisplayName(): String {
        return preferences.getString(KEY_DISPLAY_NAME, "") ?: ""
    }

    fun clearUserData() {
        preferences.edit().clear().apply()
    }
} 