package com.greenhand.cooperativework.view.activity

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import androidx.core.animation.addListener
import androidx.lifecycle.Observer
import com.greenhand.cooperativework.R
import com.greenhand.cooperativework.base.BaseApplication
import com.greenhand.cooperativework.base.BaseBindingVMActivity
import com.greenhand.cooperativework.databinding.ActivityWelcomeBinding
import com.greenhand.cooperativework.viewmodel.activity.WelcomeViewModel

class WelcomeActivity : BaseBindingVMActivity<WelcomeViewModel, ActivityWelcomeBinding>(
    R.layout.activity_welcome,
    WelcomeViewModel::class.java
) {

    override fun init() {
        mBinding.welcomeViewModel = mViewModel
        val share = BaseApplication.appContext.getSharedPreferences("welcome", Context.MODE_PRIVATE)
        val isFirstOpen = share.getBoolean("is_first_open", true)
        if (isFirstOpen) {
            val edit = share.edit()
            edit.putBoolean("is_first_open", false)
            edit.apply()
        }
        initAnimator()
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
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        )
        animator.setEvaluator(ArgbEvaluator())
        animator.duration = duration
        animator.start()

        val startTime = System.currentTimeMillis()
        mViewModel.imgUrl.observe(this) {
            val diffTime = System.currentTimeMillis() - startTime
            val newDuration = duration - diffTime
            mBinding.imgWelcomeBackground.animate()
                .alpha(0.8F)
                .scaleX(1.1F)
                .scaleY(1.1F)
                .duration = if (newDuration < 0) 0 else newDuration
        }
    }
}