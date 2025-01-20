package com.example.smartspoon.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.smartspoon.R
import com.example.smartspoon.utils.UserPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import android.util.Log
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseUser

class ProfileActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var userPreferences: UserPreferences
    private lateinit var etDisplayName: EditText
    private lateinit var etEmail: EditText
    private lateinit var btnUpdateProfile: Button
    private lateinit var btnChangePassword: Button
    private lateinit var btnLogout: Button
    private lateinit var btnDeleteAccount: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Initialize Firebase Auth and UserPreferences
        auth = FirebaseAuth.getInstance()
        userPreferences = UserPreferences(this)

        // Initialize views
        initializeViews()
        
        // Load user data
        loadUserData()
        
        // Set up click listeners
        setupClickListeners()
    }

    private fun initializeViews() {
        etDisplayName = findViewById(R.id.et_display_name)
        etEmail = findViewById(R.id.et_email)
        btnUpdateProfile = findViewById(R.id.btn_update_profile)
        btnChangePassword = findViewById(R.id.btn_change_password)
        btnLogout = findViewById(R.id.btn_logout)
        btnDeleteAccount = findViewById(R.id.btn_delete_account)
    }

    private fun loadUserData() {
        // Force refresh the current user
        auth.currentUser?.reload()?.addOnCompleteListener { reloadTask ->
            if (reloadTask.isSuccessful) {
                val user = auth.currentUser
                
                // Set email
                etEmail.setText(user?.email)
                
                // Get fresh display name from Firebase
                val firebaseDisplayName = user?.displayName
                
                // Get display name from SharedPreferences
                val savedDisplayName = userPreferences.getUserDisplayName()
                
                // Set display name (prioritize Firebase, then SharedPreferences)
                when {
                    !firebaseDisplayName.isNullOrEmpty() -> etDisplayName.setText(firebaseDisplayName)
                    !savedDisplayName.isNullOrEmpty() -> etDisplayName.setText(savedDisplayName)
                    else -> etDisplayName.setText("")
                }
                
                // Log for debugging
                Log.d("ProfileActivity", "Firebase DisplayName: $firebaseDisplayName")
                Log.d("ProfileActivity", "Saved DisplayName: $savedDisplayName")
                Log.d("ProfileActivity", "Current EditText value: ${etDisplayName.text}")
            }
        }
    }

    private fun setupClickListeners() {
        btnUpdateProfile.setOnClickListener { updateProfile() }
        btnChangePassword.setOnClickListener { changePassword() }
        btnLogout.setOnClickListener { logout() }
        btnDeleteAccount.setOnClickListener { deleteAccount() }
    }

    private fun updateProfile() {
        val newDisplayName = etDisplayName.text.toString().trim()
        val newEmail = etEmail.text.toString().trim()
        val user = auth.currentUser

        if (user != null) {
            // Check if email is being changed
            if (newEmail != user.email) {
                // Show re-authentication dialog for email change
                val dialogView = layoutInflater.inflate(R.layout.dialog_change_password, null)
                val etCurrentPassword = dialogView.findViewById<EditText>(R.id.et_current_password)

                AlertDialog.Builder(this)
                    .setTitle("Verify Password")
                    .setMessage("Please enter your current password to update profile")
                    .setView(dialogView)
                    .setPositiveButton("Update") { dialog, _ ->
                        val password = etCurrentPassword.text.toString()
                        
                        // Create credentials and re-authenticate
                        val credential = EmailAuthProvider.getCredential(user.email!!, password)
                        
                        user.reauthenticate(credential)
                            .addOnCompleteListener { reAuthTask ->
                                if (reAuthTask.isSuccessful) {
                                    // First update email
                                    user.updateEmail(newEmail)
                                        .addOnCompleteListener { emailTask ->
                                            if (emailTask.isSuccessful) {
                                                // Then update display name
                                                updateDisplayName(user, newDisplayName)
                                                // Update SharedPreferences
                                                userPreferences.saveUserData(newEmail, newDisplayName)
                                                Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                                                
                                                // Refresh the user session
                                                user.reload().addOnCompleteListener { reloadTask ->
                                                    if (reloadTask.isSuccessful) {
                                                        loadUserData() // Reload the UI with new data
                                                    }
                                                }
                                            } else {
                                                Toast.makeText(this, 
                                                    "Failed to update email: ${emailTask.exception?.message}", 
                                                    Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                } else {
                                    Toast.makeText(this, "Authentication failed: Incorrect password", 
                                        Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            } else {
                // If only display name is being updated
                updateDisplayName(user, newDisplayName)
            }
        }
    }

    private fun updateDisplayName(user: FirebaseUser, newDisplayName: String) {
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(newDisplayName)
            .build()

        user.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Update SharedPreferences
                    userPreferences.saveUserDisplayName(newDisplayName)
                    
                    // Refresh the user session
                    user.reload().addOnCompleteListener { reloadTask ->
                        if (reloadTask.isSuccessful) {
                            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                            loadUserData() // Reload the UI with new data
                        }
                    }
                } else {
                    Toast.makeText(this, "Failed to update display name", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun changePassword() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_change_password, null)
        val etCurrentPassword = dialogView.findViewById<EditText>(R.id.et_current_password)
        val etNewPassword = dialogView.findViewById<EditText>(R.id.et_new_password)

        AlertDialog.Builder(this)
            .setTitle("Change Password")
            .setView(dialogView)
            .setPositiveButton("Change") { dialog, _ ->
                val currentPassword = etCurrentPassword.text.toString()
                val newPassword = etNewPassword.text.toString()
                val user = auth.currentUser
                val email = user?.email

                if (email != null) {
                    // Reauthenticate user
                    auth.signInWithEmailAndPassword(email, currentPassword)
                        .addOnSuccessListener {
                            // Update password
                            user.updatePassword(newPassword)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(this, "Failed to update password", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Current password is incorrect", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun logout() {
        auth.signOut()
        userPreferences.clearUserData()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun deleteAccount() {
        AlertDialog.Builder(this)
            .setTitle("Delete Account")
            .setMessage("Are you sure you want to delete your account? This action cannot be undone.")
            .setPositiveButton("Delete") { _, _ ->
                val user = auth.currentUser
                user?.delete()
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            userPreferences.clearUserData()
                            Toast.makeText(this, "Account deleted successfully", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, "Failed to delete account", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
} 