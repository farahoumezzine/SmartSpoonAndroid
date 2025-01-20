package com.example.smartspoon.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartspoon.R
import com.google.firebase.auth.FirebaseAuth
import com.example.smartspoon.utils.UserPreferences
import com.example.smartspoon.model.GroceryItem
import com.example.smartspoon.database.GroceryDatabaseHelper
import com.example.smartspoon.adapter.GroceryAdapter

class WelcomeActivity : AppCompatActivity() {

    private lateinit var tvWelcomeMessage: TextView
    private lateinit var btnProfile: ImageButton
    private lateinit var auth: FirebaseAuth
    private lateinit var userPreferences: UserPreferences
    private lateinit var dbHelper: GroceryDatabaseHelper
    private lateinit var groceryAdapter: GroceryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        auth = FirebaseAuth.getInstance()
        userPreferences = UserPreferences(this)
        tvWelcomeMessage = findViewById(R.id.tv_welcome_message)
        btnProfile = findViewById(R.id.btn_profile)

        dbHelper = GroceryDatabaseHelper(this)
        
        val btnAddItem = findViewById<Button>(R.id.btn_add_item)
        val etGroceryItem = findViewById<EditText>(R.id.et_grocery_item)
        
        btnAddItem.setOnClickListener {
            val itemName = etGroceryItem.text.toString().trim()
            if (itemName.isNotEmpty()) {
                val item = GroceryItem(name = itemName)
                dbHelper.addItem(item)
                etGroceryItem.text.clear()
                refreshGroceryList()
            }
        }

        // Initialize RecyclerView
        val rvGroceryList = findViewById<RecyclerView>(R.id.rv_grocery_list)
        rvGroceryList.layoutManager = LinearLayoutManager(this)
        groceryAdapter = GroceryAdapter(
            onItemChecked = { item ->
                item.isChecked = !item.isChecked
                dbHelper.updateItem(item)
            },
            onItemFavorited = { item ->
                item.isFavorite = !item.isFavorite
                dbHelper.updateItem(item)
            },
            onItemDeleted = { item ->
                dbHelper.deleteItem(item.id)
                refreshGroceryList()
            }
        )
        rvGroceryList.adapter = groceryAdapter
        
        refreshGroceryList()

        // Find the Pizza CardView and set click listener
        findViewById<CardView>(R.id.card_pizza).setOnClickListener {
            startActivity(Intent(this, PizzaRecipesActivity::class.java))
        }

        findViewById<ImageButton>(R.id.btn_scan).setOnClickListener {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                startActivity(Intent(this, ScannerActivity::class.java))
            } else {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), 100)
            }
        }

        findViewById<ImageButton>(R.id.btn_voice_welcome).setOnClickListener {
            startActivity(Intent(this, AIRecipeGeneratorActivity::class.java))
        }

        val user = auth.currentUser
        if (user != null) {
            tvWelcomeMessage.text = "Welcome, ${user.displayName ?: user.email}!"
            
            // Set up profile button click listener
            btnProfile.setOnClickListener {
                navigateToProfile()
            }
        } else {
            tvWelcomeMessage.text = "Welcome!"
            Toast.makeText(this, "Please log in first.", Toast.LENGTH_SHORT).show()
            navigateToLogin()
        }
    }

    private fun navigateToProfile() {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun refreshGroceryList() {
        val items = dbHelper.getAllItems()
        groceryAdapter.submitList(items)
    }
}
