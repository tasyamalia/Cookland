package com.tasyaamalia.cookland.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteMeal(
    @ColumnInfo(name = "idMeal")
    var idMeal: String?,
    @ColumnInfo(name = "strMeal")
    var strMeal: String?,
    @ColumnInfo(name = "strMealThumb")
    var strMealThumb: String?,
    @ColumnInfo(name = "isFavorited")
    var isFavorited: Boolean = false,
){
    @PrimaryKey(autoGenerate = true)
    var idFav: Int? = null
}