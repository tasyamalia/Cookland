package com.tasyaamalia.cookland.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object APIClient {
    private val BASE_URL = "https://www.themealdb.com/api/json/v1/1/"

    val instance: Endpoint by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(Endpoint::class.java)
    }
}