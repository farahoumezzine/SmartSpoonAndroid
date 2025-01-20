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
import com.google.firebase.auth.FirebaseAuth
import com.example.smartspoon.utils.UserPreferences

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvRegister: TextView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
        btnLogin = findViewById(R.id.btn_login) // Ensure this ID exists in your layout
        tvRegister = findViewById(R.id.tv_register) // TextView for navigation to RegisterActivity

        // Register button click listener
        btnLogin.setOnClickListener {
            loginUser()
        }

        // Set OnClickListener to navigate to RegisterActivity
        tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser() {
        // Get user input
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        // Validate input
        if (validateEmail(email) && validatePassword(password)) {
            // Sign in user with Firebase
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        val userPreferences = UserPreferences(this)
                        userPreferences.saveUserData(
                            email = user?.email ?: "",
                            displayName = user?.displayName
                        )

                        Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()

                        // Navigate to WelcomeActivity and clear the back stack
                        val intent = Intent(this, WelcomeActivity::class.java)
                        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                        finish()
                    } else {
                        // If sign in fails, display a message to the user
                        Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
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
}
