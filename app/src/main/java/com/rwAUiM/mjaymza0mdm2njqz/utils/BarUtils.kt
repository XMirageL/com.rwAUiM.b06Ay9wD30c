package com.rwAUiM.mjaymza0mdm2njqz.utils

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.content.res.Resources.NotFoundException
import android.graphics.Color
import android.util.TypedValue
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat

object BarUtils {
    private fun getInternalDimensionSize(context: Context, key: String): Int {
        val result = 0
        try {
            val resourceId = Resources.getSystem().getIdentifier(key, "dimen", "android")
            if (resourceId > 0) {
                val sizeOne = context.resources.getDimensionPixelSize(resourceId)
                val sizeTwo = Resources.getSystem().getDimensionPixelSize(resourceId)
                return if (sizeTwo >= sizeOne) {
                    sizeTwo
                } else {
                    sizeOne
                }
            }
        } catch (ignored: NotFoundException) {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                result.toFloat(), Resources.getSystem().displayMetrics
            ).toInt()
        }
        return result
    }

    fun getStatusBarHeight(context: Context): Int {
        return getInternalDimensionSize(
            context,
            "status_bar_height"
        )
    }

    fun setStatusBarLightMode(
        activity: AppCompatActivity,
        isLightMode: Boolean
    ) {
        val window = activity.window
        val decorView: View = window.decorView
        val wic = WindowInsetsControllerCompat(window, decorView)
        wic.isAppearanceLightStatusBars = isLightMode
    }

    fun setNavigationBarLightMode(
        activity: AppCompatActivity,
        isLightMode: Boolean
    ) {
        val window = activity.window
        val decorView: View = window.decorView
        val wic = WindowInsetsControllerCompat(window, decorView)
        wic.isAppearanceLightNavigationBars = isLightMode
    }


    // 沉浸式状态栏
    fun immerseStatusBar(activity: AppCompatActivity) {
        val window = activity.window
        window.statusBarColor = Color.TRANSPARENT
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        val option = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        val vis = window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.decorView.systemUiVisibility = option or vis
        window.statusBarColor = Color.TRANSPARENT
    }


    fun setPaddingSmart(context: Context, view: View) {
        val lp = view.layoutParams
        if (lp != null && lp.height > 0) {
            lp.height += getStatusBarHeight(context) //增高
        }
        view.setPadding(
            view.paddingLeft,
            view.paddingTop + getStatusBarHeight(context),
            view.paddingRight,
            view.paddingBottom
        )
    }


    fun setNavBarColor(activity: Activity, @ColorInt color: Int) {
        setNavBarColor(activity.window, color)
    }

    fun setNavBarColor(window: Window, @ColorInt color: Int) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.navigationBarColor = color
    }

}