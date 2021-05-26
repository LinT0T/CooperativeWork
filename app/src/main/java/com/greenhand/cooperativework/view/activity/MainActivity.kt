package com.greenhand.cooperativework.view.activity

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.greenhand.cooperativework.R
import com.greenhand.cooperativework.base.BaseActivity
import com.greenhand.cooperativework.viewmodel.activity.MainViewModel

class MainActivity : BaseActivity() {

    private lateinit var mBottomNavigationView: BottomNavigationView
    private lateinit var mNavHostFragment: NavHostFragment

    private val mViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initNavigation()
        initStatusBar()
    }

    private fun initNavigation() {
        mBottomNavigationView = findViewById(R.id.nav_bottom_view)
        mNavHostFragment = supportFragmentManager.findFragmentById(R.id.nav_home_fragment) as NavHostFragment
        mBottomNavigationView.setupWithNavController(mNavHostFragment.navController)
    }

    private fun initStatusBar() { // 用一个单独的 View 来占状态栏的高度
        val statusBar = findViewById<View>(R.id.main_statusBar)
        if (STATUS_BAR_HEIGHT != 0) {
            val params = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, STATUS_BAR_HEIGHT)
            statusBar.layoutParams = params
        }
    }

    /**
     * 用来连点两下退出应用
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - mExitTime > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show()
                mExitTime = System.currentTimeMillis()
            }else {
                finish()
            }
            return false
        }
        return super.onKeyDown(keyCode, event)
    }
    private var mExitTime: Long = 0
}