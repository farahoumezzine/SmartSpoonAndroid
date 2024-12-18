package com.example.smartspoon.view

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.smartspoon.R

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome) // Replace with your actual layout resource

        // Optional: Retrieve the user name or other data (if provided via Intent)
        val userName = intent.getStringExtra("EXTRA_USER_NAME") // get user name if passed

        // Initialize views (like a TextView) to greet the user
        val welcomeMessageTextView = findViewById<TextView>(R.id.tv_welcome_message) // Adjust based on your layout
        welcomeMessageTextView.text = "Welcome, $userName!" // Greeting the user
    }
}