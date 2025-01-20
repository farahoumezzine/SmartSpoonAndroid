package com.example.smartspoon.model

data class Recipe(
    val name: String,
    val ingredients: String,
    val instructions: String,
    var isFavorite: Boolean = false
) 