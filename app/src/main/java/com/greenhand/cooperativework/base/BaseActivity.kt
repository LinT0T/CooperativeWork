package com.greenhand.cooperativework.base

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding

/**
 *@author 985892345
 *@email 2767465918@qq.com
 *@data 2021/5/25
 *@description
 */
abstract class BaseActivity(

    /**
     * 是否锁定竖屏
     */
    private val isPortraitScreen: Boolean = true,

    /**
     * 是否沉浸式状态栏
     */
    private val isCancelStatusBar: Boolean = true

) : AppCompatActivity() {

    companion object {
        /**
         * 状态栏的高度
         *
         * 因为实现了沉浸式状态栏，所以状态栏会有高度空缺，需要自己使用 View 来补充，
         * 我（郭祥瑞）已经在 activity_main 中填充了一个 View 来占位
         */
        var STATUS_BAR_HEIGHT = 0
    }


    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (isPortraitScreen) { // 锁定竖屏
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        if (isCancelStatusBar) { // 沉浸式状态栏
            cancelStatusBar()
        }
    }

    private fun cancelStatusBar() {
        val window = this.window
        val decorView = window.decorView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) { // fitsSystemWindows过时替代方法--安卓11及以上才有windowInsetsController
            // 取消状态栏
            window.setDecorFitsSystemWindows(false)
        }else {
            // 取消状态栏，已经做了判断使用
            val option = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            decorView.systemUiVisibility = option
        }

        //让view空出状态栏高度
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            STATUS_BAR_HEIGHT = resources.getDimensionPixelSize(resourceId)
        }
        window.statusBarColor = Color.TRANSPARENT //把状态栏颜色设置成透明
    }
}