package com.tasyaamalia.cookland.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.tasyaamalia.cookland.R
import com.tasyaamalia.cookland.databinding.LayoutCustomToolbarBinding


class CustomToolbarView: ConstraintLayout {

    private val TAG = CustomToolbarView::class.java.simpleName
    /**
     * Variables
     */
    //State
    private lateinit var binding: LayoutCustomToolbarBinding

    private var listener: ToolbarListener? = null

    /**
     * Constructors
     */
    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    /**
     * Initialization
     */
    private fun init(context: Context, attributeSet: AttributeSet?) {
        //Inflate view
        binding = LayoutCustomToolbarBinding.bind(LayoutInflater.from(context).inflate(R.layout.layout_custom_toolbar, this, true))
        binding.btnUser.setOnClickListener {
           listener?.onClickAbout()
        }
        binding.btnFavorite.setOnClickListener {
            listener?.onClickFavorite()
        }
    }

    private fun TextView.setMarginLR(margin: Int) {
        val params : LayoutParams = this.layoutParams as LayoutParams
        params.setMargins(margin, params.topMargin, margin, params.bottomMargin)
        this.layoutParams = params
    }

    fun toolbar(): Toolbar {
        return binding.toolbar
    }

    fun setTitle(title: String) {
        binding.lblTitle.text = title
    }

    fun setListener(listener: ToolbarListener) {
        this.listener = listener
    }
    fun hideNavUser() {
        binding.btnUser.isVisible = false
    }
    fun hideNavFavorite() {
        binding.btnFavorite.isVisible = false
    }

    interface ToolbarListener {
        fun onClickAbout()
        fun onClickFavorite()
    }

}