package com.tasyaamalia.cookland.screen.meal

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tasyaamalia.cookland.R
import com.tasyaamalia.cookland.databinding.ItemMealBinding
import com.tasyaamalia.cookland.model.Meal
import kotlin.collections.ArrayList

class MealAdapter(
    var data : ArrayList<Meal>,
    private val listener: MealListener
) : RecyclerView.Adapter<MealAdapter.MealViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        return MealViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_meal, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) =
        holder.bind(data[position], listener)

    @SuppressLint("NotifyDataSetChanged")
    fun swapData(data: ArrayList<Meal>) {
        this.data = data
        notifyDataSetChanged()
    }
    fun updateSelected(item: Meal) {
        data.map {
            if (it.idMeal == item.idMeal) {
                it.isFavorited = !it.isFavorited
            }
        }
        notifyDataSetChanged()
    }
    inner class MealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(item: Meal, listener: MealListener) = with(itemView) {
            val binding = ItemMealBinding.bind(itemView)
            Glide.with(context).load(item.strMealThumb).into(binding.imgMeal)
            binding.tvMealName.text = item.strMeal
            binding.btnFavorite.setImageDrawable(when(item.isFavorited){
                true-> ContextCompat.getDrawable(context,R.drawable.ic_favorite_true)
                false-> ContextCompat.getDrawable(context,R.drawable.ic_favorite_false)
            })
            binding.btnFavorite.setOnClickListener {
                listener.onClickFavorite(item)
            }
            setOnClickListener {
                listener.onClick(item)
            }
        }
    }

    interface MealListener {
        fun onClick(item: Meal)
        fun onClickFavorite(item: Meal)
    }
}