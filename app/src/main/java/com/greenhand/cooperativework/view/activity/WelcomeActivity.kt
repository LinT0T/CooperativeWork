package com.greenhand.cooperativework.view.activity

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.core.animation.addListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.greenhand.cooperativework.R
import com.greenhand.cooperativework.base.BaseActivity
import com.greenhand.cooperativework.base.BaseApplication
import com.greenhand.cooperativework.databinding.ActivityWelcomeBinding
import com.greenhand.cooperativework.viewmodel.activity.WelcomeViewModel
import java.lang.Thread.sleep
import kotlin.concurrent.thread

class WelcomeActivity : BaseActivity() {

    private val mBinding by lazy {
        DataBindingUtil.setContentView<ActivityWelcomeBinding>(this, R.layout.activity_welcome)
    }

    private val mViewModel by lazy {
        ViewModelProvider(this).get(WelcomeViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init() // 初始化变量
        initAnimator() // 加载界面动画
    }

    private fun init() {
        mBinding.lifecycleOwner = this
        mBinding.viewModel = mViewModel
        val share = BaseApplication.appContext.getSharedPreferences("welcome", Context.MODE_PRIVATE)
        val isFirstOpen = share.getBoolean("is_first_open", true)
        if (isFirstOpen) {
            val edit = share.edit()
            edit.putBoolean("is_first_open", false)
            edit.apply()
        }
    }

    private fun initAnimator() {
        val duration = 3000L
        val animator = ValueAnimator.ofInt(0x331E1E1E, 0xFF1E1E1E.toInt(), 0x001E1E1E)
        animator.addUpdateListener {
            val color = it.animatedValue as Int
            mViewModel.textColor.value = color
        }
        animator.addListener(
            onEnd = {
                thread {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        )
        animator.setEvaluator(ArgbEvaluator())
        animator.duration = duration
        animator.start()

        mViewModel.imgUrl.observe(this, Observer {
            mBinding.imgWelcomeBackground.animate()
                .alpha(0.8F)
                .scaleX(1.1F)
                .scaleY(1.1F)
                .duration = duration
        })
    }
}