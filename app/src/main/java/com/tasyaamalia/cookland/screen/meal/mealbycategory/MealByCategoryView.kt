package com.tasyaamalia.cookland.screen.meal.mealbycategory

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.tasyaamalia.cookland.R
import com.tasyaamalia.cookland.api.APIClient
import com.tasyaamalia.cookland.component.CustomToolbarView
import com.tasyaamalia.cookland.databinding.ActivityMealByCategoryBinding
import com.tasyaamalia.cookland.db.CooklandDatabase
import com.tasyaamalia.cookland.model.FavoriteMeal
import com.tasyaamalia.cookland.model.Meal
import com.tasyaamalia.cookland.model.MealList
import com.tasyaamalia.cookland.screen.about.AboutView
import com.tasyaamalia.cookland.screen.favorite.FavoriteView
import com.tasyaamalia.cookland.screen.meal.MealAdapter
import com.tasyaamalia.cookland.screen.meal.MealDetailView
import com.tasyaamalia.cookland.utils.Helpers.getStringRes
import com.tasyaamalia.cookland.utils.Helpers.setVisible
import com.tasyaamalia.cookland.utils.Helpers.toastShort
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealByCategoryView : AppCompatActivity() {
    val TAG: String = MealByCategoryView::class.java.simpleName

    companion object {
        private const val CATEGORY = "category"
        fun newIntent(context: Context, category:String): Intent {
            val intent = Intent(context, MealByCategoryView::class.java)
            intent.putExtra(CATEGORY,category)
            return intent
        }
    }

    private lateinit var binding: ActivityMealByCategoryBinding
    private var category: String = ""
    private lateinit var database: CooklandDatabase
    private lateinit var mealAdapter: MealAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealByCategoryBinding.inflate(layoutInflater)
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
        category = intent.getStringExtra(CATEGORY)?: ""
    }
    private fun initView(){
        initToolbar(binding.customToolbar.toolbar(), "", ContextCompat
            .getDrawable(this, R.drawable.ic_arrow_left) as Drawable
        )
        binding.customToolbar.apply {
            setTitle(String.format(getStringRes(R.string.meal_list_by),category))
            setListener(object: CustomToolbarView.ToolbarListener{
                override fun onClickAbout() {
                    startActivity(AboutView.newIntent(this@MealByCategoryView))
                }
                override fun onClickFavorite() {
                    startActivity(FavoriteView.newIntent(this@MealByCategoryView))
                }

            })
        }
        binding.rvListMeal.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
        getMealByCategory(category)
        binding.btnRefresh.setOnClickListener {
            binding.rvListMeal.setVisible(false)
            binding.shimmerLoading.root.setVisible(true)
            binding.constEmpty.setVisible(false)
            binding.constErrorConnection.setVisible(false)
            getMealByCategory(category)
        }
    }
    private fun getMealByCategory(category: String){
        APIClient.instance.getMealByCategory(category).enqueue(object : Callback<MealList> {
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
                        startActivity(MealDetailView.newIntent(this@MealByCategoryView,item.strMeal?:""))
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
                binding.constErrorConnection.setVisible(false)
                binding.shimmerLoading.root.setVisible(false)
                if(responseData?.size!! > 0){
                    binding.rvListMeal.setVisible(true)
                    binding.constEmpty.setVisible(false)
                }else{
                    binding.rvListMeal.setVisible(false)
                    binding.constEmpty.setVisible(true)
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                binding.rvListMeal.setVisible(false)
                binding.shimmerLoading.root.setVisible(false)
                binding.constEmpty.setVisible(false)
                binding.constErrorConnection.setVisible(true)
                toastShort("onFailure")
            }
        })
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
        binding.rvListMeal.setVisible(false)
        binding.shimmerLoading.root.setVisible(true)
        binding.constEmpty.setVisible(false)
        binding.constErrorConnection.setVisible(false)
        getMealByCategory(category)
    }
}