package com.tasyaamalia.cookland.screen.home

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.tasyaamalia.cookland.R
import com.tasyaamalia.cookland.api.APIClient
import com.tasyaamalia.cookland.component.CustomToolbarView
import com.tasyaamalia.cookland.screen.category.CategoryAdapter
import com.tasyaamalia.cookland.databinding.ActivityMainBinding
import com.tasyaamalia.cookland.db.CooklandDatabase
import com.tasyaamalia.cookland.model.*
import com.tasyaamalia.cookland.screen.about.AboutView
import com.tasyaamalia.cookland.screen.category.CategoryView
import com.tasyaamalia.cookland.screen.favorite.FavoriteView
import com.tasyaamalia.cookland.screen.meal.MealAdapter
import com.tasyaamalia.cookland.screen.meal.MealDetailView
import com.tasyaamalia.cookland.screen.meal.mealbycategory.MealByCategoryView
import com.tasyaamalia.cookland.utils.Helpers.getStringRes
import com.tasyaamalia.cookland.utils.Helpers.setVisible
import com.tasyaamalia.cookland.utils.Helpers.toastShort
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeView : AppCompatActivity() {
    val TAG: String = HomeView::class.java.simpleName

    companion object {
        fun newIntent(context: Context): Intent {
            val intent = Intent(context, HomeView::class.java)
            return intent
        }
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: CooklandDatabase
    private lateinit var mealAdapter: MealAdapter
    private var mealNeedToRefresh: Boolean = true
    private var categoryNeedToRefresh: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        getCategories()
        getMealByCategory()
        //inisialisasi Database
        database = Room.databaseBuilder(applicationContext, CooklandDatabase::class.java, "cookland-db")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()

    }

    private fun initView() {
        binding.customToolbar.apply {
            setTitle(getStringRes(R.string.app_name))
            setListener(object:CustomToolbarView.ToolbarListener{
                override fun onClickAbout() {
                    startActivity(AboutView.newIntent(this@HomeView))
                }
                override fun onClickFavorite() {
                    startActivity(FavoriteView.newIntent(this@HomeView))
                }

            })
        }
        binding.rvCategory.apply {
            layoutManager = GridLayoutManager(context, 4)
            setHasFixedSize(true)
        }
        binding.rvListMeal.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
        binding.btnRefresh.setOnClickListener {
            mealNeedToRefresh = true
            categoryNeedToRefresh = true
            binding.shimmerLoading.root.setVisible(true)
            binding.svMain.setVisible(false)
            binding.constErrorConnection.setVisible(false)
            getCategories()
            getMealByCategory()
        }
    }

    private fun getCategories(){
        APIClient.instance.getCategories().enqueue(object : Callback<CategoryList> {
            override fun onResponse(
                call: Call<CategoryList>,
                response: Response<CategoryList>
            ) {
                val responseData = response.body()
                val listQuickCategory = ArrayList<Category>()
                for (index in 0..6){
                    responseData?.meals?.get(index)?.let { listQuickCategory.add(it) }
                }
                listQuickCategory.add(Category("SeeAll","See All","",""))
                binding.rvCategory.adapter = CategoryAdapter(listQuickCategory,object :
                    CategoryAdapter.CategoryListener{
                    override fun onClick(item: Category) {
                        when(item.idCategory){
                            "SeeAll"->startActivity(CategoryView.newIntent(this@HomeView))
                            else->{
                                startActivity(MealByCategoryView.newIntent(this@HomeView,item.strCategory?:""))
                            }
                        }
                    }
                })
                if (mealNeedToRefresh && categoryNeedToRefresh){
                    binding.shimmerLoading.root.setVisible(false)
                    binding.svMain.setVisible(true)
                    mealNeedToRefresh = false
                    categoryNeedToRefresh = false
                }
                binding.constErrorConnection.setVisible(false)
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                binding.shimmerLoading.root.setVisible(false)
                binding.svMain.setVisible(false)
                binding.constErrorConnection.setVisible(true)
                toastShort("onFailure")
            }
        })
    }

    private fun getMealByCategory(){
        APIClient.instance.getMealByCategory("Chicken").enqueue(object : Callback<MealList> {
            override fun onResponse(
                call: Call<MealList>,
                response: Response<MealList>
            ) {
                val responseData = response.body()?.meals
                responseData?.map {
                    if(database.cooklandDao().getFavoriteMealById(it.idMeal ?: "").isNotEmpty()){
                        it.isFavorited = true
                    }
                }
                mealAdapter = MealAdapter(responseData?: ArrayList(),object :
                    MealAdapter.MealListener{
                    override fun onClick(item: Meal) {
                        startActivity(MealDetailView.newIntent(this@HomeView,item.strMeal?:""))
                    }

                    override fun onClickFavorite(item: Meal) {
                        val favMeal = FavoriteMeal(item.idMeal,item.strMeal,item.strMealThumb,!item.isFavorited)
                        if(item.isFavorited){
                            item.idMeal?.let { database.cooklandDao().deleteById(it) }
                        }else{
                            database.cooklandDao().insert(favMeal)
                        }
                        mealAdapter.updateSelected(item)
                    }
                })
                binding.rvListMeal.adapter = mealAdapter
                if (mealNeedToRefresh && categoryNeedToRefresh){
                    binding.shimmerLoading.root.setVisible(false)
                    binding.svMain.setVisible(true)
                    mealNeedToRefresh = false
                    categoryNeedToRefresh = false
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                binding.shimmerLoading.root.setVisible(false)
                binding.svMain.setVisible(false)
                binding.constErrorConnection.setVisible(true)
                toastShort("onFailure")
            }
        })
    }
    override fun onResume() {
        super.onResume()
        mealNeedToRefresh = true
        categoryNeedToRefresh = true
        binding.shimmerLoading.root.setVisible(true)
        binding.svMain.setVisible(false)
        getMealByCategory()
    }
}