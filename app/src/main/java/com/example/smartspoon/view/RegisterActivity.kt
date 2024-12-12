package com.example.smartspoon.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartspoon.R

class RegisterActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var tvLoginPrompt: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register) // Replace with your actual layout name

        // Initialize views
        etName = findViewById(R.id.et_name)
        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
        etConfirmPassword = findViewById(R.id.et_confirm_password)
        btnRegister = findViewById(R.id.btn_register)
        tvLoginPrompt = findViewById(R.id.tv_login_prompt)

        // Register button click listener
        btnRegister.setOnClickListener {
            registerUser()
        }

        // Login prompt click listener
        tvLoginPrompt.setOnClickListener {
            navigateToLogin()
        }
    }

    private fun registerUser() {
        // Get user input
        val name = etName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val confirmPassword = etConfirmPassword.text.toString().trim()

        // Validate input
        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
            return
        }

        if (email.isEmpty()) {
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show()
            return
        }

        if (confirmPassword.isEmpty() || password != confirmPassword) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
        navigateToLogin() // Navigate to login after successful registration
    }

    private fun navigateToLogin() {
        // Navigate back to LoginActivity
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }
}
