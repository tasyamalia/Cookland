package com.tasyaamalia.cookland.screen.about

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.tasyaamalia.cookland.R
import com.tasyaamalia.cookland.databinding.ActivityAboutBinding
import com.tasyaamalia.cookland.utils.Helpers.getStringRes

class AboutView : AppCompatActivity() {
    val TAG: String = AboutView::class.java.simpleName

    companion object {
        fun newIntent(context: Context): Intent {
            val intent = Intent(context, AboutView::class.java)
            return intent
        }
    }

    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }
    private fun initView(){
        initToolbar(binding.customToolbar.toolbar(), "", ContextCompat
            .getDrawable(this, R.drawable.ic_arrow_left) as Drawable
        )
        binding.customToolbar.apply {
            setTitle(getStringRes(R.string.about))
            hideNavUser()
        }
        Glide.with(this)
            .load(R.drawable.img_profile)
            .centerCrop()
            .dontAnimate()
            .apply(RequestOptions.circleCropTransform())
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(binding.imgProfile)
        binding.lblName.text = getStringRes(R.string.name)
        binding.lblEmail.text =  getStringRes(R.string.email)
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