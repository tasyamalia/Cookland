package com.tasyaamalia.cookland.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.Toast

object Helpers {
    fun Activity.toastShort(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    fun Context.getStringRes(int: Int) : String {
        return resources.getString(int)
    }

    fun View.setVisible(isVisible: Boolean) {
        visibility = if (isVisible) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}