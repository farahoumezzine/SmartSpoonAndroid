package com.example.smartspoon.model

data class GroceryItem(
    val id: Long = 0,
    var name: String,
    var isFavorite: Boolean = false,
    var isChecked: Boolean = false
) 