package com.example.smartspoon.view

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartspoon.R
import com.example.smartspoon.adapter.PizzaRecipeAdapter
import com.example.smartspoon.model.PizzaRecipe

class PizzaRecipesActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PizzaRecipeAdapter
    private var allRecipes = listOf<PizzaRecipe>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pizza_recipes)

        // Initialize back button
        findViewById<ImageButton>(R.id.btn_back).setOnClickListener {
            onBackPressed()
        }

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.rv_pizza_recipes)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Create sample recipes
        createSampleRecipes()

        // Initialize adapter with all recipes
        adapter = PizzaRecipeAdapter(allRecipes)
        recyclerView.adapter = adapter

        // Set up filter buttons
        setupFilters()

        findViewById<CardView>(R.id.card_ai_generator).setOnClickListener {
            startActivity(Intent(this, AIRecipeGeneratorActivity::class.java))
        }
    }

    private fun createSampleRecipes() {
        allRecipes = listOf(
            PizzaRecipe(
                "Classic Margherita",
                R.drawable.pizza_margherita,
                "25 mins",
                isVegetarian = true,
                isSpicy = false,
                rating = 4.8f,
                ingredients = "Pizza dough, Fresh mozzarella, Fresh basil leaves, Tomato sauce, Olive oil, Salt",
                instructions = "1. Preheat oven to 450°F\n2. Roll out pizza dough\n3. Spread tomato sauce\n4. Add mozzarella slices\n5. Bake for 12-15 minutes\n6. Add fresh basil leaves\n7. Drizzle with olive oil"
            ),
            PizzaRecipe(
                "Spicy Pepperoni Supreme",
                R.drawable.pizza_pepperoni,
                "30 mins",
                isVegetarian = false,
                isSpicy = true,
                rating = 4.7f,
                ingredients = "Pizza dough, Pepperoni slices, Mozzarella cheese, Spicy tomato sauce, Bell peppers, Red pepper flakes",
                instructions = "1. Preheat oven to 450°F\n2. Roll out dough\n3. Spread spicy sauce\n4. Add cheese and toppings\n5. Sprinkle red pepper flakes\n6. Bake for 15-18 minutes"
            ),
            PizzaRecipe(
                "Mediterranean Veggie",
                R.drawable.pizza_veggie,
                "28 mins",
                isVegetarian = true,
                isSpicy = false,
                rating = 4.5f,
                ingredients = "Pizza dough, Feta cheese, Black olives, Cherry tomatoes, Red onions, Spinach, Olive oil, Oregano",
                instructions = "1. Preheat oven to 450°F\n2. Roll out pizza dough\n3. Brush with olive oil\n4. Add spinach and vegetables\n5. Sprinkle feta cheese\n6. Add oregano\n7. Bake for 12-15 minutes"
            ),
            PizzaRecipe(
                "Buffalo Chicken",
                R.drawable.pizza_chicken,
                "35 mins",
                isVegetarian = false,
                isSpicy = true,
                rating = 4.6f,
                ingredients = "Pizza dough, Grilled chicken, Buffalo sauce, Blue cheese, Mozzarella cheese, Red onions, Ranch dressing",
                instructions = "1. Preheat oven to 450°F\n2. Roll out dough\n3. Mix chicken with buffalo sauce\n4. Spread ranch base\n5. Add cheeses and chicken\n6. Top with onions\n7. Bake for 15-18 minutes"
            ),
            PizzaRecipe(
                "Four Cheese Delight",
                R.drawable.pizza_cheese,
                "25 mins",
                isVegetarian = true,
                isSpicy = false,
                rating = 4.4f,
                ingredients = "Pizza dough, Mozzarella cheese, Gorgonzola cheese, Parmesan cheese, Ricotta cheese, Olive oil, Fresh basil",
                instructions = "1. Preheat oven to 450°F\n2. Roll out dough\n3. Brush with olive oil\n4. Spread ricotta base\n5. Add other cheeses\n6. Bake for 12-15 minutes\n7. Garnish with fresh basil"
            ),
            PizzaRecipe(
                "Spicy Veggie Supreme",
                R.drawable.pizza_spicy_veggie,
                "32 mins",
                isVegetarian = true,
                isSpicy = true,
                rating = 4.3f,
                ingredients = "Pizza dough, Spicy marinara sauce, Bell peppers, Jalapeños, Red onions, Mushrooms, Mozzarella cheese, Red pepper flakes",
                instructions = "1. Preheat oven to 450°F\n2. Roll out dough\n3. Spread spicy marinara\n4. Add vegetables and cheese\n5. Sprinkle red pepper flakes\n6. Bake for 15-18 minutes"
            ),
            PizzaRecipe(
                "BBQ Chicken Deluxe",
                R.drawable.pizza_bbq,
                "40 mins",
                isVegetarian = false,
                isSpicy = false,
                rating = 4.7f,
                ingredients = "Pizza dough, BBQ sauce, Grilled chicken, Red onions, Mozzarella cheese, Cilantro, Bacon bits",
                instructions = "1. Preheat oven to 450°F\n2. Roll out dough\n3. Spread BBQ sauce\n4. Add chicken and bacon\n5. Top with cheese and onions\n6. Bake for 15-20 minutes\n7. Garnish with cilantro"
            ),
            PizzaRecipe(
                "Garden Fresh Veggie",
                R.drawable.pizza_garden,
                "30 mins",
                isVegetarian = true,
                isSpicy = false,
                rating = 4.2f,
                ingredients = "Pizza dough, Tomato sauce, Mozzarella cheese, Zucchini, Cherry tomatoes, Bell peppers, Fresh basil, Garlic oil",
                instructions = "1. Preheat oven to 450°F\n2. Roll out dough\n3. Brush with garlic oil\n4. Spread sauce\n5. Add vegetables and cheese\n6. Bake for 12-15 minutes\n7. Top with fresh basil"
            )
        )
    }

    private fun setupFilters() {
        val cardAll = findViewById<CardView>(R.id.card_all)
        val cardVegetarian = findViewById<CardView>(R.id.card_vegetarian)
        val cardSpicy = findViewById<CardView>(R.id.card_spicy)

        cardAll.setOnClickListener {
            adapter.updateRecipes(allRecipes)
            updateFilterSelection(cardAll, cardVegetarian, cardSpicy)
        }

        cardVegetarian.setOnClickListener {
            val vegetarianRecipes = allRecipes.filter { it.isVegetarian }
            adapter.updateRecipes(vegetarianRecipes)
            updateFilterSelection(cardVegetarian, cardAll, cardSpicy)
        }

        cardSpicy.setOnClickListener {
            val spicyRecipes = allRecipes.filter { it.isSpicy }
            adapter.updateRecipes(spicyRecipes)
            updateFilterSelection(cardSpicy, cardAll, cardVegetarian)
        }
    }

    private fun updateFilterSelection(
        selectedCard: CardView,
        vararg unselectedCards: CardView
    ) {
        selectedCard.cardElevation = 8f
        unselectedCards.forEach { it.cardElevation = 4f }
    }
} 