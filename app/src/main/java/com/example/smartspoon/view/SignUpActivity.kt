package com.example.smartspoon.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartspoon.R
import com.example.smartspoon.controller.LoginController
import com.example.smartspoon.model.UserData

class SignUpActivity: AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnSignup: Button
    private lateinit var tvPasswordStrength: TextView
    private lateinit var tv_register: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)


        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnSignup = findViewById(R.id.btn_signup);
        tvPasswordStrength = findViewById(R.id.tv_password_strength)

        val userData = UserData(this) // Pass the context
        val loginController = LoginController(userData)

        btnSignup.setOnClickListener {
            // Get input values
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()



            etPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    val password = s.toString()
                    val strength = getPasswordStrength(password)
                    tvPasswordStrength.text = strength.message
                    tvPasswordStrength.setTextColor(Color.parseColor(strength.color))
                }
            })

            // Validate fields
            if (validateName(name) && validateEmail(email) && validatePassword(password)) {
                // All validations passed
                Toast.makeText(this, "Sign up successful!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, WelcomeActivty::class.java)
                startActivity(intent)
            }else{
                Toast.makeText(this, "Sign up failed!", Toast.LENGTH_SHORT).show()
            }
        }

        // Reference to the TextView for registration
        val registerBtn = findViewById<TextView>(R.id.tv_register)

        // Set OnClickListener to navigate to the RegisterActivity
        registerBtn.setOnClickListener {
            // Navigate to RegisterActivity
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
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
            email.isBlank() -> { // Use isBlank to cover both empty and whitespace-only strings
                etEmail.error = "Email is required"
                false
            }

            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                etEmail.error = "Invalid email format"
                false
            }

            else -> true // The email is valid
        }
    }


    private fun validatePassword(password: String): Boolean {
        return when {
            password.isEmpty() -> {
                etPassword.error = "Password is required"
                false
            }

            password.length < 6 -> {
                etPassword.error = "Password must be at least 6 characters long"
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

    data class PasswordStrength(val message: String, val color: String)





}

