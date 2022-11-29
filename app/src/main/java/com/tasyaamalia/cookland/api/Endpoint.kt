package com.tasyaamalia.cookland.api

import com.tasyaamalia.cookland.model.CategoryList
import com.tasyaamalia.cookland.model.MealList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Endpoint {
    @GET("list.php?c=list")
    fun getCategories(): Call<CategoryList>

    @GET("filter.php?")
    fun getMealByCategory(@Query("c") category:String): Call<MealList>

    @GET("search.php?")
    fun getMeal(@Query("s") meal:String): Call<MealList>
}