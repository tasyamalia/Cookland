package com.tasyaamalia.cookland.screen.category

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.tasyaamalia.cookland.R
import com.tasyaamalia.cookland.api.APIClient
import com.tasyaamalia.cookland.component.CustomToolbarView
import com.tasyaamalia.cookland.databinding.ActivityCategoryBinding
import com.tasyaamalia.cookland.model.Category
import com.tasyaamalia.cookland.model.CategoryList
import com.tasyaamalia.cookland.screen.about.AboutView
import com.tasyaamalia.cookland.screen.favorite.FavoriteView
import com.tasyaamalia.cookland.screen.meal.mealbycategory.MealByCategoryView
import com.tasyaamalia.cookland.utils.Helpers.getStringRes
import com.tasyaamalia.cookland.utils.Helpers.setVisible
import com.tasyaamalia.cookland.utils.Helpers.toastShort
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryView : AppCompatActivity() {
    val TAG: String = CategoryView::class.java.simpleName

    companion object {
        fun newIntent(context: Context): Intent {
            val intent = Intent(context, CategoryView::class.java)
            return intent
        }
    }

    private lateinit var binding: ActivityCategoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        getCategories()
    }

    private fun initView() {
        initToolbar(binding.customToolbar.toolbar(), "", ContextCompat
            .getDrawable(this, R.drawable.ic_arrow_left) as Drawable
        )
        binding.customToolbar.apply {
            setTitle(getStringRes(R.string.main_category))
            setListener(object: CustomToolbarView.ToolbarListener{
                override fun onClickAbout() {
                    startActivity(AboutView.newIntent(this@CategoryView))
                }
                override fun onClickFavorite() {
                    startActivity(FavoriteView.newIntent(this@CategoryView))
                }

            })
        }
        binding.rvCategory.apply {
            layoutManager = GridLayoutManager(context, 4)
            setHasFixedSize(true)
        }
        binding.btnRefresh.setOnClickListener {
            binding.rvCategory.setVisible(false)
            binding.shimmerLoading.root.setVisible(true)
            binding.constEmpty.setVisible(false)
            binding.constErrorConnection.setVisible(false)
            getCategories()
        }
    }
    private fun getCategories(){
        APIClient.instance.getCategories().enqueue(object : Callback<CategoryList> {
            override fun onResponse(
                call: Call<CategoryList>,
                response: Response<CategoryList>
            ) {
                val responseData = response.body()
                binding.rvCategory.adapter = CategoryAdapter(responseData?.meals ?:ArrayList(),object :
                    CategoryAdapter.CategoryListener{
                    override fun onClick(item: Category) {
                        startActivity(MealByCategoryView.newIntent(this@CategoryView,item.strCategory?:""))
                    }
                })
                binding.shimmerLoading.root.setVisible(false)
                binding.constErrorConnection.setVisible(false)
                if (responseData?.meals?.size!! > 0){
                    binding.rvCategory.setVisible(true)
                    binding.constEmpty.setVisible(false)
                }else{
                    binding.rvCategory.setVisible(false)
                    binding.constEmpty.setVisible(true)
                }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                binding.rvCategory.setVisible(false)
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
}