package com.greenhand.cooperativework.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.greenhand.cooperativework.R
import com.greenhand.cooperativework.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private val mViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}