package com.tasyaamalia.cookland.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tasyaamalia.cookland.model.FavoriteMeal

@Database(entities = [FavoriteMeal::class], version = 2)
abstract class CooklandDatabase: RoomDatabase() {
    abstract fun cooklandDao(): CooklandDao
}