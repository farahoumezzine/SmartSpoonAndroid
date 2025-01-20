package com.example.smartspoon.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.smartspoon.R

class RecipeDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_details)

        // Get recipe details from intent
        val recipeName = intent.getStringExtra("recipe_name")
        val recipeImage = intent.getIntExtra("recipe_image", 0)
        val recipeTime = intent.getStringExtra("recipe_time")
        val isVegetarian = intent.getBooleanExtra("is_vegetarian", false)
        val isSpicy = intent.getBooleanExtra("is_spicy", false)
        val rating = intent.getFloatExtra("rating", 0f)
        val ingredients = intent.getStringExtra("ingredients") ?: ""
        val instructions = intent.getStringExtra("instructions") ?: ""

        // Set recipe details
        findViewById<TextView>(R.id.tv_recipe_name).text = recipeName
        findViewById<ImageView>(R.id.iv_recipe).setImageResource(recipeImage)
        findViewById<TextView>(R.id.tv_cooking_time).text = recipeTime
        findViewById<RatingBar>(R.id.rating_bar).rating = rating
        findViewById<TextView>(R.id.tv_ingredients).text = ingredients
        findViewById<TextView>(R.id.tv_instructions).text = instructions

        // Show/hide tags
        findViewById<TextView>(R.id.tag_vegetarian).visibility = 
            if (isVegetarian) View.VISIBLE else View.GONE
        findViewById<TextView>(R.id.tag_spicy).visibility = 
            if (isSpicy) View.VISIBLE else View.GONE

        findViewById<ImageButton>(R.id.btn_back).setOnClickListener {
            finish()
        }
    }

    companion object {
        fun createIntent(context: Context, recipeName: String) = Intent(context, RecipeDetailsActivity::class.java).apply {
            putExtra("recipe_name", recipeName)
        }
    }
}