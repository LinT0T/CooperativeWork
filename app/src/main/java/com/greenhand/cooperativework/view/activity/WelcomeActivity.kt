package com.greenhand.cooperativework.view.activity

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.animation.addListener
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.greenhand.cooperativework.R
import com.greenhand.cooperativework.base.BaseActivity
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
        mBinding.lifecycleOwner = this
        mBinding.viewModel = mViewModel
        init()
    }

    private fun init() {
        val animator = ValueAnimator.ofInt(0xFF1E1E1E.toInt(), 0x001E1E1E)
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
        animator.duration = 2000
        animator.start()
    }
}