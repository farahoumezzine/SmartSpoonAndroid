package com.example.smartspoon.model

data class PizzaRecipe(
    val name: String,
    val imageUrl: Int,  // Resource ID for the image
    val cookingTime: String,
    val isVegetarian: Boolean,
    val isSpicy: Boolean,
    val rating: Float,
    val ingredients: String,
    val instructions: String
) 