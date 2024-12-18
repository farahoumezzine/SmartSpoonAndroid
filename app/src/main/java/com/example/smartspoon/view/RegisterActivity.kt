package com.example.smartspoon.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartspoon.R
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var tvLoginPrompt: TextView
    private lateinit var tvPasswordStrength: TextView // For displaying password strength
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Initialize views
        etName = findViewById(R.id.et_name)
        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
        etConfirmPassword = findViewById(R.id.et_confirm_password)
        btnRegister = findViewById(R.id.btn_register)
        tvLoginPrompt = findViewById(R.id.tv_login_prompt)
        tvPasswordStrength = findViewById(R.id.tv_password_strength)

        // Adding TextWatcher to check password strength
        etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val password = s.toString()
                val strength = getPasswordStrength(password)

                // Update password strength display
                tvPasswordStrength.text = strength.message
                tvPasswordStrength.setTextColor(Color.parseColor(strength.color))
            }
        })

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
        if (validateName(name) && validateEmail(email) && validatePassword(password, confirmPassword)) {
            // Register user with Firebase Authentication
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Registration success
                        Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                        navigateToLogin() // Navigate to login after successful registration
                    } else {
                        // If registration fails, display a message to the user
                        Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        Log.w(TAG, "Registration:failure", task.exception)
                    }
                }
        }
    }

    private fun validateName(name: String): Boolean {
        return when {
            name.isEmpty() -> {
                etName.error = "Name is required"
                false
            }
            name.length < 3 -> {
                etName.error = "Name must be at least 3 characters long"
                false
            }
            else -> true
        }
    }

    private fun validateEmail(email: String): Boolean {
        return when {
            email.isBlank() -> {
                etEmail.error = "Email is required"
                false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                etEmail.error = "Invalid email format"
                false
            }
            else -> true
        }
    }

    private fun validatePassword(password: String, confirmPassword: String): Boolean {
        return when {
            password.isEmpty() -> {
                etPassword.error = "Password is required"
                false
            }
            password.length < 6 -> {
                etPassword.error = "Password must be at least 6 characters long"
                false
            }
            password != confirmPassword -> {
                etConfirmPassword.error = "Passwords do not match"
                false
            }
            else -> true
        }
    }

    private fun getPasswordStrength(password: String): PasswordStrength {
        return when {
            password.length >= 8 &&
                    password.any { it.isLowerCase() } &&
                    password.any { it.isUpperCase() } &&
                    password.any { it.isDigit() } &&
                    password.any { !it.isLetterOrDigit() } -> {
                PasswordStrength("Strong", "#4CAF50") // Green
            }
            password.length >= 6 &&
                    password.any { it.isLetter() } &&
                    password.any { it.isDigit() } -> {
                PasswordStrength("Medium", "#FFC107") // Yellow
            }
            password.isNotEmpty() -> {
                PasswordStrength("Weak", "#F44336") // Red
            }
            else -> {
                PasswordStrength("", "#000000") // No color
            }
        }
    }

    private fun navigateToLogin() {
        // Navigate to the login activity
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // Call finish() to remove the registration activity from the back stack
    }

    companion object {
        private const val TAG = "RegisterActivity"
    }

    data class PasswordStrength(val message: String, val color: String)
}
