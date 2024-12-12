package com.example.smartspoon.model

import android.content.Context

class UserData(private val context: Context) { // Inject context for data persistence
    private val sharedPreferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)

    fun saveUser(user: User) {
        with(sharedPreferences.edit()) {
            putString("username", user.username)
            putString("password", user.password)
            apply()
        }
    }
    fun getUser(username: String): User? {
        val storedUsername = sharedPreferences.getString("username", null)
        val storedPassword = sharedPreferences.getString("password", null)
        return if (storedUsername == username && storedPassword != null) {
            User(storedUsername, "", storedPassword)
        } else {
            null
        }
    }

}