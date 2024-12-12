package com.example.smartspoon.controller

import com.example.smartspoon.model.UserData

class LoginController(private val userData: UserData) {
    fun login(username: String, password: String): Boolean {
        val user = userData.getUser(username)
        return user != null && user.password == password
    }
}