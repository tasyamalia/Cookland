package com.tasyaamalia.cookland.screen.favorite

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
import com.tasyaamalia.cookland.component.CustomToolbarView
import com.tasyaamalia.cookland.databinding.ActivityFavoriteBinding
import com.tasyaamalia.cookland.db.CooklandDatabase
import com.tasyaamalia.cookland.model.FavoriteMeal
import com.tasyaamalia.cookland.screen.about.AboutView
import com.tasyaamalia.cookland.screen.meal.MealDetailView
import com.tasyaamalia.cookland.utils.Helpers.getStringRes
import com.tasyaamalia.cookland.utils.Helpers.setVisible

class FavoriteView : AppCompatActivity() {
    val TAG: String = FavoriteView::class.java.simpleName

    companion object {
        fun newIntent(context: Context): Intent {
            val intent = Intent(context, FavoriteView::class.java)
            return intent
        }
    }

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var database: CooklandDatabase
    private lateinit var mealAdapter: MealFavAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //inisialisasi Database
        database = Room.databaseBuilder(applicationContext, CooklandDatabase::class.java, "cookland-db")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
        initView()
    }
    private fun initView(){
        initToolbar(binding.customToolbar.toolbar(), "", ContextCompat
            .getDrawable(this, R.drawable.ic_arrow_left) as Drawable
        )
        binding.customToolbar.apply {
            setTitle(getStringRes(R.string.favorite))
            hideNavFavorite()
            setListener(object: CustomToolbarView.ToolbarListener{
                override fun onClickAbout() {
                    startActivity(AboutView.newIntent(this@FavoriteView))
                }
                override fun onClickFavorite() {}

            })
        }
        binding.rvFavorite.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
        val dataFav = database.cooklandDao().getAllFavoriteMeal()
        if(dataFav.isNotEmpty()){
            binding.rvFavorite.setVisible(true)
            binding.constEmpty.setVisible(false)
        }else{
            binding.rvFavorite.setVisible(false)
            binding.constEmpty.setVisible(true)
        }
        mealAdapter = MealFavAdapter(dataFav,object :
            MealFavAdapter.MealListener{
            override fun onClick(item: FavoriteMeal) {
                startActivity(MealDetailView.newIntent(this@FavoriteView,item.strMeal?:""))
            }

            override fun onClickFavorite(item: FavoriteMeal) {
                item.idMeal?.let { database.cooklandDao().deleteById(it) }
                mealAdapter.updateSelected(item)
                mealAdapter.swapData(database.cooklandDao().getAllFavoriteMeal())
            }
        })
        binding.rvFavorite.adapter = mealAdapter

    }
    private fun initToolbar(toolbar: Toolbar, title: String, iconBack: Drawable) {
        setSupportActionBar(toolbar)
        supportActionBar?.title = title
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(false)
        toolbar.navigationIcon = iconBack
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }
}