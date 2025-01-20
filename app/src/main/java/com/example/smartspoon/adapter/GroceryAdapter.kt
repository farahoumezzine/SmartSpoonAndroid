package com.example.smartspoon.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartspoon.R
import com.example.smartspoon.model.GroceryItem

class GroceryAdapter(
    private val onItemChecked: (GroceryItem) -> Unit,
    private val onItemFavorited: (GroceryItem) -> Unit,
    private val onItemDeleted: (GroceryItem) -> Unit
) : RecyclerView.Adapter<GroceryAdapter.GroceryViewHolder>() {

    private var items = listOf<GroceryItem>()

    class GroceryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val checkBox: CheckBox = view.findViewById(R.id.checkbox_item)
        val favoriteButton: ImageButton = view.findViewById(R.id.btn_favorite)
        val deleteButton: ImageButton = view.findViewById(R.id.btn_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_grocery, parent, false)
        return GroceryViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroceryViewHolder, position: Int) {
        val item = items[position]
        
        holder.checkBox.apply {
            text = item.name
            isChecked = item.isChecked
            setOnCheckedChangeListener { _, _ -> onItemChecked(item) }
        }

        holder.favoriteButton.apply {
            setImageResource(
                if (item.isFavorite) R.drawable.baseline_favorite_24
                else R.drawable.baseline_favorite_border_24
            )
            setOnClickListener { onItemFavorited(item) }
        }

        holder.deleteButton.setOnClickListener { onItemDeleted(item) }
    }

    override fun getItemCount() = items.size

    fun submitList(newItems: List<GroceryItem>) {
        items = newItems
        notifyDataSetChanged()
    }
} 