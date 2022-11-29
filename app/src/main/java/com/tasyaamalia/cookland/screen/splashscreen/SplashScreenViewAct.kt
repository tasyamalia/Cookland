package com.tasyaamalia.cookland.screen.splashscreen

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.tasyaamalia.cookland.databinding.ActivitySplashScreenBinding
import com.tasyaamalia.cookland.screen.home.HomeView

class SplashScreenViewAct : AppCompatActivity() {
    val TAG: String = SplashScreenViewAct::class.java.simpleName

    companion object {
        fun newIntent(context: Context): Intent {
            val intent = Intent(context, SplashScreenViewAct::class.java)
            return intent
        }
    }

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }
    private fun initView(){
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(HomeView.newIntent(this@SplashScreenViewAct))
        }, 3000)
    }
}