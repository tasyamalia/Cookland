package com.tasyaamalia.cookland.screen.meal

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tasyaamalia.cookland.api.APIClient
import com.tasyaamalia.cookland.databinding.ActivityMealDetailBinding
import com.tasyaamalia.cookland.utils.Helpers.toastShort
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.room.Room
import com.bumptech.glide.Glide
import com.tasyaamalia.cookland.R
import com.tasyaamalia.cookland.component.CustomToolbarView
import com.tasyaamalia.cookland.db.CooklandDatabase
import com.tasyaamalia.cookland.model.FavoriteMeal
import com.tasyaamalia.cookland.model.MealList
import com.tasyaamalia.cookland.screen.about.AboutView
import com.tasyaamalia.cookland.screen.favorite.FavoriteView
import com.tasyaamalia.cookland.utils.Helpers.setVisible
import kotlin.collections.ArrayList


class MealDetailView : AppCompatActivity() {
    val TAG: String = MealDetailView::class.java.simpleName

    companion object {
        private const val MEAL = "meal"
        fun newIntent(context: Context, meal:String): Intent {
            val intent = Intent(context, MealDetailView::class.java)
            intent.putExtra(MEAL,meal)
            return intent
        }
    }

    private lateinit var binding: ActivityMealDetailBinding
    private var meal: String = ""
    private val listIngredients = ArrayList<String>()
    private lateinit var database: CooklandDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBundle()
        //inisialisasi Database
        database = Room.databaseBuilder(applicationContext, CooklandDatabase::class.java, "cookland-db")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
        initView()
    }
    private fun initBundle(){
        meal = intent.getStringExtra(MEAL)?: ""
    }
    private fun initView(){
        initToolbar(binding.customToolbar.toolbar(), "", ContextCompat
            .getDrawable(this, R.drawable.ic_arrow_left) as Drawable
        )
        binding.customToolbar.apply {
            setListener(object: CustomToolbarView.ToolbarListener{
                override fun onClickAbout() {
                    startActivity(AboutView.newIntent(this@MealDetailView))
                }
                override fun onClickFavorite() {
                    startActivity(FavoriteView.newIntent(this@MealDetailView))
                }

            })
        }
        binding.rvIngredients.apply {
            layoutManager = GridLayoutManager(context, 2)
            setHasFixedSize(true)
        }
        getMeal(meal)
    }
    private fun getMeal(meal: String){
        APIClient.instance.getMeal(meal).enqueue(object : Callback<MealList> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<MealList>,
                response: Response<MealList>
            ) {
                binding.constBodyValue.setVisible(true)
                binding.shimmerLoading.root.setVisible(false)
                val responseData = response.body()?.meals?.get(0)
                Glide.with(this@MealDetailView)
                    .load(responseData?.strMealThumb)
                    .centerCrop()
                    .into(binding.imgMeal)
                binding.customToolbar.setTitle(responseData?.strMeal?:"")
                binding.lblTitleMeal.text = responseData?.strMeal
                binding.lblCategory.text = responseData?.strCategory
                binding.lblArea.text = responseData?.strArea
                binding.lblInstructionsValue.text = responseData?.strInstructions?:""
                listIngredients.clear()
                insertIngredients(responseData?.strMeasure1, responseData?.strIngredient1)
                insertIngredients(responseData?.strMeasure2, responseData?.strIngredient2)
                insertIngredients(responseData?.strMeasure3, responseData?.strIngredient3)
                insertIngredients(responseData?.strMeasure4, responseData?.strIngredient4)
                insertIngredients(responseData?.strMeasure5, responseData?.strIngredient5)
                insertIngredients(responseData?.strMeasure6, responseData?.strIngredient6)
                insertIngredients(responseData?.strMeasure7, responseData?.strIngredient7)
                insertIngredients(responseData?.strMeasure8, responseData?.strIngredient8)
                insertIngredients(responseData?.strMeasure9, responseData?.strIngredient9)
                insertIngredients(responseData?.strMeasure10, responseData?.strIngredient10)

                insertIngredients(responseData?.strMeasure11, responseData?.strIngredient11)
                insertIngredients(responseData?.strMeasure12, responseData?.strIngredient12)
                insertIngredients(responseData?.strMeasure13, responseData?.strIngredient13)
                insertIngredients(responseData?.strMeasure14, responseData?.strIngredient14)
                insertIngredients(responseData?.strMeasure15, responseData?.strIngredient15)
                insertIngredients(responseData?.strMeasure16, responseData?.strIngredient16)
                insertIngredients(responseData?.strMeasure17, responseData?.strIngredient17)
                insertIngredients(responseData?.strMeasure18, responseData?.strIngredient18)
                insertIngredients(responseData?.strMeasure19, responseData?.strIngredient19)
                insertIngredients(responseData?.strMeasure20, responseData?.strIngredient20)

                binding.rvIngredients.adapter = IngredientsAdapter(listIngredients, object : IngredientsAdapter.IngredientsListener{
                    override fun onClick(item: String) {
                    }
                })
                if(database.cooklandDao().getFavoriteMealById(responseData?.idMeal ?: "").isNotEmpty()){
                    responseData?.isFavorited = true
                }
                setSelectionFavorite((responseData?.isFavorited)!!)

                binding.btnMealFavorite.setOnClickListener {
                    val favMeal = FavoriteMeal(responseData?.idMeal,responseData?.strMeal,responseData?.strMealThumb,!(responseData?.isFavorited)!!)
                    if(responseData?.isFavorited){
                        responseData?.idMeal?.let { database.cooklandDao().deleteById(it) }
                    }else{
                        database.cooklandDao().insert(favMeal)
                    }
                    setSelectionFavorite(!(responseData?.isFavorited)!!)
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                toastShort("onFailure")
            }
        })
    }

    private fun setSelectionFavorite(isFavourite:Boolean){
        binding.btnMealFavorite.setImageDrawable(when(isFavourite){
            true-> ContextCompat.getDrawable(this@MealDetailView,R.drawable.ic_favorite_true)
            false-> ContextCompat.getDrawable(this@MealDetailView,R.drawable.ic_favorite_false)
        })
    }
    private fun insertIngredients (strMeasure: String?,strIngredient: String?){
        if ((strMeasure != null) && strIngredient != null) {
            if(strMeasure.isNotEmpty() && strIngredient.isNotEmpty()){
                listIngredients.add("$strMeasure $strIngredient")
            }
        }
    }
    private fun initToolbar(toolbar: Toolbar, title: String, iconBack: Drawable) {
        setSupportActionBar(toolbar)
        supportActionBar?.title = title
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(false)
        toolbar.navigationIcon = iconBack
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onResume() {
        super.onResume()
        binding.constBodyValue.setVisible(false)
        binding.shimmerLoading.root.setVisible(true)
        getMeal(meal)
    }
}