package com.tasyaamalia.cookland.screen.meal

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tasyaamalia.cookland.R
import com.tasyaamalia.cookland.databinding.ItemIngredientsBinding
import kotlin.collections.ArrayList

class IngredientsAdapter(
    var data : ArrayList<String>,
    private val listener: IngredientsListener
) : RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientsViewHolder {
        return IngredientsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_ingredients, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: IngredientsViewHolder, position: Int) =
        holder.bind(data[position], listener)

    @SuppressLint("NotifyDataSetChanged")
    fun swapData(data: ArrayList<String>) {
        this.data = data
        notifyDataSetChanged()
    }

    inner class IngredientsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(item: String, listener: IngredientsListener) = with(itemView) {
            val binding = ItemIngredientsBinding.bind(itemView)
            binding.lblIngredients.text = item
            setOnClickListener {
                listener.onClick(item)
            }
        }
    }

    interface IngredientsListener {
        fun onClick(item: String)
    }
}