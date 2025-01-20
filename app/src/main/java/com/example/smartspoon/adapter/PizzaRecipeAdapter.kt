package com.example.smartspoon.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartspoon.R
import com.example.smartspoon.model.PizzaRecipe
import com.example.smartspoon.view.RecipeDetailsActivity

class PizzaRecipeAdapter(private var recipes: List<PizzaRecipe>) :
    RecyclerView.Adapter<PizzaRecipeAdapter.PizzaViewHolder>() {

    class PizzaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.iv_recipe_image)
        val nameText: TextView = view.findViewById(R.id.tv_recipe_name)
        val timeText: TextView = view.findViewById(R.id.tv_recipe_time)
        val ratingBar: RatingBar = view.findViewById(R.id.rating_bar)
        val tagVegetarian: CardView = view.findViewById(R.id.tag_vegetarian)
        val tagSpicy: CardView = view.findViewById(R.id.tag_spicy)
        val viewRecipeButton: Button = view.findViewById(R.id.btn_view_recipe)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PizzaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pizza_recipe, parent, false)
        return PizzaViewHolder(view)
    }

    override fun onBindViewHolder(holder: PizzaViewHolder, position: Int) {
        val recipe = recipes[position]
        
        holder.imageView.setImageResource(recipe.imageUrl)
        holder.nameText.text = recipe.name
        holder.timeText.text = recipe.cookingTime
        holder.ratingBar.rating = recipe.rating

        // Show/hide vegetarian tag
        holder.tagVegetarian.visibility = 
            if (recipe.isVegetarian) View.VISIBLE else View.GONE

        // Show/hide spicy tag
        holder.tagSpicy.visibility = 
            if (recipe.isSpicy) View.VISIBLE else View.GONE

        holder.viewRecipeButton.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, RecipeDetailsActivity::class.java).apply {
                putExtra("recipe_name", recipe.name)
                putExtra("recipe_image", recipe.imageUrl)
                putExtra("recipe_time", recipe.cookingTime)
                putExtra("is_vegetarian", recipe.isVegetarian)
                putExtra("is_spicy", recipe.isSpicy)
                putExtra("rating", recipe.rating)
                putExtra("ingredients", recipe.ingredients)
                putExtra("instructions", recipe.instructions)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = recipes.size

    fun updateRecipes(newRecipes: List<PizzaRecipe>) {
        recipes = newRecipes
        notifyDataSetChanged()
    }
}