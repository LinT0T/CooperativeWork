package com.greenhand.cooperativework.view.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.greenhand.cooperativework.R
import com.greenhand.cooperativework.adapter.ImageFragmentStateAdapter
import com.greenhand.cooperativework.bean.CommunityRecommendImageBean
import com.greenhand.cooperativework.databinding.ActivityImageDetailsBinding
import com.greenhand.cooperativework.utils.toast
import com.greenhand.cooperativework.view.fragment.ImageFragment

class ImageDetailsActivity : AppCompatActivity(), View.OnTouchListener, ImageFragment.ViewState {
    private lateinit var mImagePage: ViewPager2
    private lateinit var mImageOrderNum: TextView
    private lateinit var mImageDetailsLayout: ConstraintLayout
    private lateinit var mCommunityFragmentStateAdapter: ImageFragmentStateAdapter
    private lateinit var mItemBinding: ActivityImageDetailsBinding
    private var mImageFragmentList = ArrayList<Fragment>()
    private lateinit var mImageUrlList: List<String>
    private var hide: Boolean = false//除图片外其他view是否已隐藏
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //创建Binding 并绑定数据
        createBinding()
        //初始化相关view
        initView()
    }

    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    private fun initView() {
        mImageDetailsLayout = findViewById(R.id.layout_image_detailed)
        mImagePage = findViewById(R.id.vp_image)
        mImageOrderNum = findViewById(R.id.tv_image_order_num)

        //设置图片详细layout的onTouch方法 全部return true
        mImageDetailsLayout.setOnTouchListener(this)

        //先将图片序号设置为1/mImageUrlList.size
        mImageOrderNum.text = "1/${mImageUrlList.size}"

        //监听viewPage2滑动
        mImagePage.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                //滑动时动态设置图片序号
                mImageOrderNum.text = "${position + 1}/${mImageUrlList.size}"
            }
        })

        //创建和Url等数量的Fragment 并将相应的Url传入Fragment
        for (element in mImageUrlList) {
            val bundle = Bundle()
            bundle.putString("url", element)
            val imageFragment = ImageFragment()
            imageFragment.arguments = bundle
            mImageFragmentList.add(imageFragment)
        }
        mCommunityFragmentStateAdapter = ImageFragmentStateAdapter(this, mImageFragmentList)
        mImagePage.adapter = mCommunityFragmentStateAdapter
    }

    private fun createBinding() {
        mItemBinding = DataBindingUtil.setContentView(this, R.layout.activity_image_details)
        mItemBinding.imageBean = getImageBean()
        mItemBinding.eventHandle = EventHandle()
    }

    private fun getImageBean(): CommunityRecommendImageBean? {
        val imageBean: CommunityRecommendImageBean? = intent.getParcelableExtra("imageBean")
        if (imageBean != null) {
            mImageUrlList = imageBean.imageUrlList
        }
        return imageBean
    }

    /**
     * 设置图片详细layout的onTouch方法 全部return true
     * 使在该layout中滑动不会影响到viewPage的滑动
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return true
    }

    /**
     * 事件内部类
     * 通过binding绑定到xml中
     */
    inner class EventHandle() {
        //处理单击事件
        fun onItemSingleClick(view: View) {
            "缺少API".toast()
        }
    }

    override fun hideOrShow() {
        //点击时隐藏其他view
        hide = if (!hide) {
            val fade1 = ObjectAnimator.ofFloat(mImageOrderNum, "alpha", 1f, 0f)
            val fade2 = ObjectAnimator.ofFloat(mImageDetailsLayout, "alpha", 1f, 0f)
            val animSet = AnimatorSet()
            animSet.play(fade1).with(fade2)
            animSet.start()
            true
        } else {
            val fade1 = ObjectAnimator.ofFloat(mImageOrderNum, "alpha", 0f, 1f)
            val fade2 = ObjectAnimator.ofFloat(mImageDetailsLayout, "alpha", 0f, 1f)
            val animSet = AnimatorSet()
            animSet.play(fade1).with(fade2)
            animSet.start()
            false
        }
    }

}