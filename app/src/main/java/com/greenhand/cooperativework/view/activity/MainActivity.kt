package com.greenhand.cooperativework.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.greenhand.cooperativework.R
import com.greenhand.cooperativework.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var mBottomNavigationView: BottomNavigationView
    private lateinit var mNavHostFragment:NavHostFragment
    private val mViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }
    private fun init(){
        mBottomNavigationView = findViewById(R.id.nav_bottom_view)
        mNavHostFragment = supportFragmentManager.findFragmentById(R.id.nav_home_fragment) as NavHostFragment
        mBottomNavigationView.setupWithNavController(mNavHostFragment.navController) }
}