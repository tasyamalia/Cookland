package com.tasyaamalia.cookland.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.tasyaamalia.cookland.model.FavoriteMeal

@Dao
interface CooklandDao {
    @Query("SELECT * FROM FavoriteMeal")
    fun getAllFavoriteMeal():List<FavoriteMeal>

    @Insert
    fun insert(vararg favMeal: FavoriteMeal)

    @Query("DELETE FROM FavoriteMeal WHERE idMeal = :idMeal")
    fun deleteById(idMeal: String)

    @Query("DELETE FROM FavoriteMeal")
    fun deleteAll()

    @Query("SELECT * FROM FavoriteMeal WHERE idMeal = :idMeal")
    fun getFavoriteMealById(idMeal: String):List<FavoriteMeal>
}