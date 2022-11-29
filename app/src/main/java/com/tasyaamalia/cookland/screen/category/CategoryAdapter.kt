package com.tasyaamalia.cookland.screen.category

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tasyaamalia.cookland.R
import com.tasyaamalia.cookland.databinding.ItemCategoryBinding
import com.tasyaamalia.cookland.model.Category
import kotlin.collections.ArrayList

class CategoryAdapter(
    var data : ArrayList<Category>,
    private val listener: CategoryListener
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_category, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) =
        holder.bind(data[position], listener)

    @SuppressLint("NotifyDataSetChanged")
    fun swapData(data: ArrayList<Category>) {
        this.data = data
        notifyDataSetChanged()
    }

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(item: Category, listener: CategoryListener) = with(itemView) {
            val binding = ItemCategoryBinding.bind(itemView)
            binding.tvCategory.text = item.strCategory
            setOnClickListener {
                listener.onClick(item)
            }
            binding.constItemCategory.setBackgroundColor(ContextCompat.getColor(context,
            when(item.strCategory){
                "Beef","Pork" ->R.color.colorYellow
                "Chicken", "Seafood"->R.color.colorPink
                "Dessert", "Side", "Goat"->R.color.colorBlue
                "Lamb","Starter"->R.color.colorOrange
                "Miscellaneous", "Vegan"->R.color.colorPurpleMenu
                "Pasta", "Vegetarian", "Breakfast"->R.color.colorGreen
                else->R.color.colorYellow
            }))
            binding.imgCategory.setImageDrawable(ContextCompat.getDrawable(context,
            when(item.strCategory){
                "Beef"->R.drawable.beef
                "Breakfast"->R.drawable.breakfast
                "Chicken"->R.drawable.chicken
                "Dessert"->R.drawable.dessert
                "Goat"->R.drawable.goat
                "Lamb"->R.drawable.lamb
                "Miscellaneous"->R.drawable.miscellaneous
                "Pasta"->R.drawable.pasta
                "Pork"->R.drawable.pork
                "Seafood"->R.drawable.seafood
                "Side"->R.drawable.side
                "Starter"->R.drawable.starter
                "Vegan"->R.drawable.vegan
                "Vegetarian"->R.drawable.vegetarian
                else->R.drawable.seeall
            }))

        }

    }

    interface CategoryListener {
        fun onClick(item: Category)
    }
}