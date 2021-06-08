package com.ndhzs.slideshow

import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.cardview.widget.CardView
import androidx.core.animation.addListener
import androidx.core.view.NestedScrollingParent2
import androidx.core.view.NestedScrollingParentHelper
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.imageview.ShapeableImageView
import com.ndhzs.slideshow.myinterface.OnImgRefreshListener
import com.ndhzs.slideshow.myinterface.OnRefreshListener
import com.ndhzs.slideshow.utils.*
import com.ndhzs.slideshow.viewpager2.adapter.BaseFragmentStateAdapter
import com.ndhzs.slideshow.viewpager2.adapter.BaseImgAdapter
import com.ndhzs.slideshow.viewpager2.adapter.BaseRecyclerAdapter
import com.ndhzs.slideshow.viewpager2.pagecallback.BasePageChangeCallBack
import com.ndhzs.slideshow.viewpager2.transformer.BaseMultipleTransformer
import kotlin.math.abs

/**
 * **WARNING：** 目前还未实现自动滑动！
 *
 * 该控件参考了第三方库：Banner (https://github.com/youth5201314/banner)，在此表示感谢！
 *
 * 里面内置了 transformer（页面移动动画）的默认实现类，
 * 来自于 (https://github.com/youth5201314/banner) ，分别有：
 *
 * （因名字太长，只给出第一个单词）
 *
 * Alpha
 *
 * Depth
 *
 * MZScale
 *
 * Rotate
 *
 * Rotate
 *
 * Scale
 *
 * Zoom
 *
 * 还内置了许多动画，来自于 https://github.com/ToxicBakery/ViewPagerTransforms
 *
 * （因名字太长，只给出第一个单词）
 *
 * Accordion
 *
 * Background
 *
 * Cube
 *
 * Default
 *
 * Depth
 *
 * Drawer
 *
 * Flip
 *
 * Foreground
 *
 * Rotate
 *
 * Scale
 *
 * Stack
 *
 * Tablet
 *
 * Zoom
 *
 * @author 985892345
 * @email 2767465918@qq.com
 * @data 2021/5/26
 */
class SlideShow : CardView, NestedScrollingParent2 {

    private val mAttrs: SlideShowAttrs
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        mAttrs = SlideShowAttrs.Builder().build()
        mAttrs.initialize(context, attrs)
        setPageInterval()
        init()
    }
    constructor(context: Context, attrs: SlideShowAttrs) : super(context) {
        mAttrs = attrs
        attrs.setAttrs()
        init()
    }

    /**
     * 该方法只能设置一个 transformer（页面移动动画）
     *
     * 已内置了几个 transformer，请点击 [SlideShow] 查看注释
     *
     * @see [addTransformer]
     */
    fun setTransformer(transformer: ViewPager2.PageTransformer): SlideShow {
        clearTransformer()
        mPageTransformers.addTransformer(transformer)
        return this
    }

    /**
     * 该方法可以设置多个 transformer（页面移动动画）
     * @see [setTransformer]
     */
    fun addTransformer(transformer: ViewPager2.PageTransformer): SlideShow {
        mPageTransformers.addTransformer(transformer)
        return this
    }

    /**
     * 该方法用于删除 transformer（页面移动动画）
     * @see [clearTransformer]
     */
    fun removeTransformer(transformer: ViewPager2.PageTransformer) {
        mPageTransformers.removeTransformer(transformer)
    }

    /**
     * 该方法用于删除全部 transformer（页面移动动画）
     * @see [removeTransformer]
     */
    fun clearTransformer() {
        mPageTransformers.mTransformers.clear()
    }

    /**
     * 虽然可以得到 ViewPager2，但建议最好不要在 ViewPager2 中设置一些东西
     *
     * **WARNING：** 传入 [setAutoSlideEnabled] 为 true 或调用 [openCirculateEnabled] 后，
     * ViewPager2 的内部 item 位置会发生改变。如果此时你与 Toolbar 等进行联合将会出现位置问题。
     *
     * **NOTICE：** 可以使用 [setPageChangeCallback] 来进行联合，这个回调会返回你想要的 item 位置
     *
     * @see [setPageChangeCallback]
     */
    fun getViewPager2(): ViewPager2 {
        return mViewPager2
    }

    fun setAllViewPager2Setting(vp: (viewPager2: ViewPager2) -> Unit): SlideShow {
        vp.invoke(mViewPager2)
        return this
    }

    /**
     * 得到是否设置了 Adapter
     */
    fun hasBeenSetAdapter(): Boolean {
        return mViewPager2.adapter != null
    }

    fun setOffscreenPageLimit(@ViewPager2.OffscreenPageLimit limit: Int): SlideShow {
        mViewPager2.offscreenPageLimit = limit
        return this
    }

    fun getIsLeft(): Boolean {
        return !getChildAt(0).canScrollHorizontally(-1)
    }

    fun getIsRight(): Boolean {
        return !getChildAt(0).canScrollHorizontally(1)
    }

    fun getIsTop(): Boolean {
        return !getChildAt(0).canScrollVertically(-1)
    }

    fun getIsBottom(): Boolean {
        return !getChildAt(0).canScrollVertically(-1)
    }

    /**
     * 用于设置图片加载的 Adapter
     *
     * **NOTICE：** 如果你想使一个页面能看到相邻的图片边缘，请设置 app:slide_adjacentPageInterval
     *
     * **NOTICE：** 使用该方法可能意为着你需要自动滑动，请使用 [setAutoSlideEnabled]
     */
    fun <T> setAdapter(datas: List<T>, imgAdapter: BaseImgAdapter<T>): SlideShow {
        imgAdapter.setData(datas, mAttrs)
        mViewPager2.adapter = imgAdapter
        mImgRealItemCount = datas.size
        if (mIsCirculateEnabled && mImgRealItemCount > 1) {
            imgAdapter.openCirculateEnabled()
            mPageChangeCallback.openCirculateEnabled(mImgRealItemCount)
            if (mIsAutoSlideEnabled) { // 在延迟加载 adapter 后，ViewPager2 仍然会显示，但 onAttachedToWindow 已经被调用，所以要在这里调用开始
                start()
            }
        }
        return this
    }

    /**
     * 用于设置图片加载的 Adapter（使用 Lambda 填写）
     *
     * **NOTICE：** 如果你想使一个页面能看到相邻的图片边缘，请设置 app:slide_adjacentPageInterval
     *
     * **NOTICE：** 使用该方法可能意为着你需要自动滑动，请使用 [setAutoSlideEnabled]
     */
    fun <T> setAdapter(
            datas: List<T>,
            onBindImageView:
            (data: T,
             imageView: ShapeableImageView,
             holder: BaseImgAdapter.BaseImgViewHolder,
             position: Int
            ) -> Unit
    ): SlideShow {
        val adapter = object : BaseImgAdapter<T>() {
            override fun onBindImageView(
                    data: T,
                    imageView: ShapeableImageView,
                    holder: BaseImgViewHolder,
                    position: Int
            ) {
                onBindImageView.invoke(data, imageView, holder, position)
            }
        }
        return setAdapter(datas, adapter)
    }

    /**
     * 用于设置 FragmentStateAdapter
     *
     * 该方法简写了创建 FragmentStateAdapter 的过程，传入数据后会自动帮你设置 FragmentStateAdapter
     */
    fun setAdapter(fragments: List<Fragment>, fragmentActivity: FragmentActivity): SlideShow {
        if (mIsAutoSlideEnabled) {
            throw IllegalAccessException(
                    "Your ${SlideShowAttrs.Library_name}#setAdapter()、setAutoSlideEnabled(): " +
                            "The adapter does not support automatic sliding!")
        }
        if (mIsCirculateEnabled) {
            throw IllegalAccessException(
                    "Your ${SlideShowAttrs.Library_name}#setAdapter()、 openCirculateEnabled(): " +
                            "The adapter does not support circular presentation!")
        }
        val adapter = object : BaseFragmentStateAdapter(fragmentActivity, fragments) {}
        mViewPager2.adapter = adapter
        return this
    }

    /**
     * 用于设置 FragmentStateAdapter
     */
    fun setAdapter(fragmentAdapter: FragmentStateAdapter): SlideShow {
        if (mIsAutoSlideEnabled) {
            throw IllegalAccessException(
                    "Your ${SlideShowAttrs.Library_name}#setAdapter()、setAutoSlideEnabled(): " +
                            "The adapter does not support automatic sliding!")
        }
        if (mIsCirculateEnabled) {
            throw IllegalAccessException(
                    "Your ${SlideShowAttrs.Library_name}#setAdapter()、 openCirculateEnabled(): " +
                            "The adapter does not support circular presentation!")
        }
        mViewPager2.adapter = fragmentAdapter
        return this
    }

    /**
     * 用于设置通用 RecyclerView.Adapter
     *
     * 不推荐使用自己的 adapter，建议使用 BaseRecyclerAdapter，用法一样，但可以调用 [notifyItemRefresh] 方法进行特殊刷新
     */
    @Deprecated("不建议使用自己的 adapter", ReplaceWith("使用 BaseRecyclerAdapter 代替"))
    fun setAdapter(adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>): SlideShow {
        if (mIsAutoSlideEnabled) {
            throw IllegalAccessException(
                    "Your ${SlideShowAttrs.Library_name}#setAdapter()、setAutoSlideEnabled(): " +
                            "The adapter does not support automatic sliding!")
        }
        if (mIsCirculateEnabled) {
            throw IllegalAccessException(
                    "Your ${SlideShowAttrs.Library_name}#setAdapter()、 openCirculateEnabled(): " +
                            "The adapter does not support circular presentation!")
        }
        mViewPager2.adapter = adapter
        return this
    }

    /**
     * 用于设置 BaseRecyclerAdapter
     *
     * 使用该 adapter 后可以调用 [notifyItemRefresh] 方法进行特殊刷新
     */
    fun setAdapter(adapter: BaseRecyclerAdapter<out RecyclerView.ViewHolder>): SlideShow {
        if (mIsAutoSlideEnabled) {
            throw IllegalAccessException(
                    "Your ${SlideShowAttrs.Library_name}#setAdapter()、setAutoSlideEnabled(): " +
                            "The adapter does not support automatic sliding!")
        }
        if (mIsCirculateEnabled) {
            throw IllegalAccessException(
                    "Your ${SlideShowAttrs.Library_name}#setAdapter()、 openCirculateEnabled(): " +
                            "The adapter does not support circular presentation!")
        }
        mViewPager2.adapter = adapter
        return this
    }

    /**
     * 通知位置为 position 的 imageView 刷新
     *
     * **NOTICE：** 该方法支持永久的更新，但实现原理是打上标记，在滑动回来时会重新调用，所以会出现重复调用，请注意该点
     *
     * **WARNING：** 不建议进行延时操作
     *
     * **WARNING：** 使用该方法的前提是 [setAdapter] 是 [BaseImgAdapter] 的实现类，否则将报错
     */
    fun notifyImageViewRefresh(position: Int, @Refresh.Condition condition: Int, l: OnImgRefreshListener) {
        val adapter = mViewPager2.adapter
        if (adapter is BaseImgAdapter<*>) {
            adapter.setImgRefreshListener(position, condition, l)
        }else {
            throw IllegalAccessException(
                    "Your ${SlideShowAttrs.Library_name}#notifyImageViewRefresh(): " +
                            "The adapter is not BaseImgAdapter, so you can't use function of notifyImageViewRefresh!")
        }
    }

    /**
     * 通知位置为 position 的 holder 刷新
     *
     * **NOTICE：** 该方法支持永久的更新，但实现原理是打上标记，在滑动回来时会重新调用，所以会出现重复调用，请注意该点
     *
     * **WARNING：** 不建议进行延时操作
     *
     * **WARNING：** 使用该方法的前提是 [setAdapter] 是 [BaseRecyclerAdapter] 的实现类，否则将报错
     */
    fun notifyItemRefresh(position: Int, @Refresh.Condition condition: Int, l: OnRefreshListener) {
        val adapter = mViewPager2.adapter
        if (adapter is BaseRecyclerAdapter) {
            adapter.setRefreshListener(position, condition, l)
        }else {
            throw IllegalAccessException(
                    "Your ${SlideShowAttrs.Library_name}#notifyRefresh(): " +
                            "The adapter is not BaseRecyclerAdapter, so you can't use function of notifyRefresh!")
        }
    }

    /**
     * 用于刷新全部
     *
     * **NOTE：** 如果使用的 [BaseImgAdapter]，可以在修改了外部数据的情况下调用该方法进行刷新
     */
    fun notifyDataSetChanged() {
        val adapter = mViewPager2.adapter
        if (adapter is BaseImgAdapter<*>) {
            adapter.refreshData()
            return
        }
        adapter!!.notifyDataSetChanged()
    }

    /**
     * 用于给设置了 [BaseImgAdapter] 的情况下传入新数据刷新
     *
     * **WARNING：** 使用该方法的前提是 [setAdapter] 是 [BaseImgAdapter] 的实现类，否则将报错
     */
    fun <T> notifyImgDataChange(data: List<T>) {
        val adapter = mViewPager2.adapter
        if (adapter is BaseImgAdapter<*>) {
            adapter.refreshData(data as List<Nothing>)
        }else {
            throw IllegalAccessException(
                "Your ${SlideShowAttrs.Library_name}#notifyImgDataChange(): " +
                        "The adapter is not BaseImgAdapter, so you can't use function of notifyImgDataChange!")
        }
    }

    /**
     * 用于清除之前设置的刷新
     *
     * 只会在 [Refresh.Condition.COEXIST]、[Refresh.Condition.COVERED] 时调用才有用
     */
    fun removeImgRefreshListener(position: Int) {
        val adapter = mViewPager2.adapter
        if (adapter is BaseImgAdapter<*>) {
            adapter.removeImgRefreshListener(position)
        }else if (adapter is BaseRecyclerAdapter) {
            adapter.removeRefreshListener(position)
        }
    }

    /**
     * 用于清除之前设置的刷新
     *
     * 只会在 [Refresh.Condition.COEXIST]、[Refresh.Condition.COVERED] 时调用才有用
     */
    fun clearImgRefreshListener() {
        val adapter = mViewPager2.adapter
        if (adapter is BaseImgAdapter<*>) {
            adapter.clearImgRefreshListener()
        }else if (adapter is BaseRecyclerAdapter) {
            adapter.clearRefreshListener()
        }
    }

    /**
     * 设置是否自动滑动
     *
     * **WARNING：** 传入 true 后，为了能够循环，将会使 ViewPager2 的 item 位置发生变化，该变化即使在之后传入 false 也不能取消
     */
    fun setAutoSlideEnabled(enabled: Boolean): SlideShow {
        if (enabled) {
            val adapter = mViewPager2.adapter
            if (adapter != null) {
                if (adapter !is BaseImgAdapter<*>) {
                    throw IllegalAccessException(
                            "Your ${SlideShowAttrs.Library_name}#setAutoSlideEnabled(): " +
                                    "The adapter does not support automatic sliding!")
                }
            }
            openCirculateEnabled()
        }else {
            stop()
        }
        mIsAutoSlideEnabled = enabled
        start() // 里面会判断
        return this
    }

    fun getAutoSlideEnabled(): Boolean {
        return mIsAutoSlideEnabled
    }

    /**
     * 开启自动滑动后该方法自动会被调用
     *
     * 调用该方法将会使 ViewPager2 的 item 位置发生变化，该变化不能取消
     *
     * **WARNING：** 只有在未加载视图时设置才有效
     *
     * @see [setAutoSlideEnabled]
     */
    fun openCirculateEnabled(): SlideShow {
        if (mIsCirculateEnabled) {
            return this
        }

        val adapter = mViewPager2.adapter
        if (adapter != null) { // 等于 null 时交给 adapter 设置
            if (adapter is BaseImgAdapter<*>) {
                if (mImgRealItemCount > 1) {
                    adapter.openCirculateEnabled()
                    mPageChangeCallback.openCirculateEnabled(mImgRealItemCount)
                }else {
                    mIsCirculateEnabled = false
                }
            }else {
                throw IllegalAccessException(
                        "Your ${SlideShowAttrs.Library_name}#setAdapter()、 openCirculateEnabled(): " +
                                "The adapter does not support circular presentation!")
            }
        }
        mIsCirculateEnabled = true
        finalItem = 0
        return this
    }

    /**
     * 设置自动滑动的延迟时间，默认时间是 4 秒，最低只能设置成 2 秒
     */
    fun setDelayTime(delayTime: Long): SlideShow {
        mDelayTime = delayTime
        if (delayTime < 2000) {
            mDelayTime = 2000
        }
        return this
    }

    /**
     * 设置自动滑动动画的时间，默认时间是 1 秒
     */
    fun setAutoSlideTime(slideTime: Long): SlideShow {
        mAutoSlideTime = slideTime
        return this
    }

    /**
     * 设置自动滑动动画的插值器
     */
    fun setTimeInterpolator(interpolator: TimeInterpolator): SlideShow {
        mInterpolator = interpolator
        return this
    }

    /**
     * 设置指示器的横幅背景颜色
     */
    fun setIndicatorsBannerColor(color: Int): SlideShow {
        mAttrs.indicatorBannerColor = color
        return this
    }

    /**
     * 设置指示器的圆点颜色
     */
    fun setIndicatorsColor(color: Int): SlideShow {
        mAttrs.indicatorColor = color
        return this
    }

    /**
     * 设置指示器的圆点半径
     */
    fun setIndicatorsRadius(radius: Float): SlideShow {
        mAttrs.indicatorRadius = radius
        return this
    }

    /**
     * 设置指示器的位置
     *
     * @param gravity 数据来自 [Indicators]
     */
    fun setIndicatorsGravity(@Indicators.Gravity gravity: Int): SlideShow {
        mAttrs.indicatorGravity = gravity
        return this
    }

    /**
     * 得到指示器的位置
     */
    @Indicators.Gravity
    fun getIndicatorsGravity(): Int {
        return mAttrs.indicatorGravity
    }

    /**
     * 设置指示器的样式
     *
     * @see style 数据来自 [Indicators]
     */
    fun setIndicatorsStyle(@Indicators.Style style: Int): SlideShow {
        mAttrs.indicatorStyle = style
        return this
    }

    /**
     * 得到指示器的样式
     */
    @Indicators.Style
    fun getIndicatorsStyle(): Int {
        return mAttrs.indicatorStyle
    }

    /**
     * 设置是否显示指示器
     */
    fun setShowIndicators(boolean: Boolean): SlideShow {
        mAttrs.isShowIndicators = boolean
        return this
    }

    /**
     * 得到内部 ViewPager2 的 orientation
     */
    @ViewPager2.Orientation
    fun getOrientation(): Int {
        return mAttrs.orientation
    }

    /**
     * 设置 [SlideShow] 的圆角，其实 [SlideShow] 继承于 [CardView]，可以使用 [CardView] 的方法
     */
    fun setOutRadius(radius: Float): SlideShow {
        super.setRadius(radius)
        return this
    }

    /**
     * 监听 ViewPager2 的滑动
     *
     * **NOTICE：** 在传入 [setAutoSlideEnabled] 为 true 或调用了 [openCirculateEnabled] 的情况下，
     * ViewPager2 的内部 item 位置会发生改变。
     *
     * **WARNING：** 请使用该方法与 Toolbar 等进行联合
     */
    fun setPageChangeCallback(callback: ViewPager2.OnPageChangeCallback): SlideShow {
        mPageChangeCallback.setPageChangeCallBack(callback)
        return this
    }

    fun removePageChangeCallback() {
        mPageChangeCallback.removePageChangeCallback()
    }

    /**
     * 设置 [SlideShow] 刚被加载时的起始页面
     * @see [setCurrentItem]
     */
    fun setStartItem(item: Int): SlideShow {
        finalItem = item
        return this
    }
    private var finalItem = 0

    /**
     * 与 ViewPager2#setCurrentItem 相同，默认有动画
     *
     * **WARNING：** 只有在设置 adapter 后才有效
     *
     * @see [setStartItem]
     */
    fun setCurrentItem(item: Int, smoothScroll: Boolean = true): SlideShow {
         if (mViewPager2.isFakeDragging) {
             mViewPager2.endFakeDrag()
         }
        mViewPager2.setCurrentItem(
                if (mIsCirculateEnabled) item + 2 else item,
                smoothScroll)
        return this
    }

    /**
     * 进行可控制时间的滑动
     */
    fun slowlySlide(startPosition: Int, endPosition: Int, duration: Long, interpolator: TimeInterpolator = LinearInterpolator()) {
        if (mViewPager2.isAttachedToWindow) {
            checkIsInItemCount(startPosition, "startPosition")
            checkIsInItemCount(endPosition, "endPosition")
            fakeDrag(endPosition - startPosition, duration, interpolator)
        }else {
            mRunnableManger.post{
                checkIsInItemCount(startPosition, "startPosition")
                checkIsInItemCount(endPosition, "endPosition")
                fakeDrag(endPosition - startPosition, duration, interpolator)
            }
        }
    }

    /**
     * 设置是否允许用户滑动
     *
     * 设置了 false 后你将会在 SlideShow 的父 View 的 onTouchEvent 收到事件
     */
    fun setUserInputEnabled(userInputEnabled: Boolean): SlideShow {
        mViewPager2.isUserInputEnabled = userInputEnabled
        mIsUserInputEnabled = userInputEnabled
        return this
    }

    fun getUserInputEnabled(): Boolean {
        return mIsUserInputEnabled
    }

    /**
     * 设置是否开启同方向的嵌套滑动处理
     */
    fun setOpenNestedScroll(isOpenNestedScroll: Boolean): SlideShow {
        mIsOpenNestedScroll = isOpenNestedScroll
        return this
    }

    /**
     * 用于开启自动滑动
     */
    fun start() {
        if (mIsAutoSlideEnabled) {
            if (!mIsSliding && mImgRealItemCount != 1) {
                mIsSliding = true
                mRunnableManger.postDelay(mDelayTime, mAutoSlideRunnable)
                if (!this::mInterpolator.isInitialized) {
                    mInterpolator = LinearInterpolator()
                }
            }
        }
    }

    /**
     * 用于关闭自动滑动
     */
    fun stop() {
        if (mIsAutoSlideEnabled) {
            mIsSliding = false
            mRunnableManger.remove(mAutoSlideRunnable)
            if (this::mAnimator.isInitialized) {
                mAnimator.cancel()
            }
        }
    }

    private val mParentHelper by lazy {
        NestedScrollingParentHelper(this)
    }
    private val mRunnableManger = RunnableManger(this)
    private val mViewPager2 = ViewPager2(context)
    private var mIsUserInputEnabled = true
    private val mPageChangeCallback = BasePageChangeCallBack(mViewPager2)
    private val mPageTransformers by lazy {
        val transformer = BaseMultipleTransformer()
        mViewPager2.setPageTransformer(transformer)
        transformer
    }
    private var mImgRealItemCount = 1
    private var mIsAutoSlideEnabled = false
    private var mIsSliding = false
    private var mDelayTime = 4000L
    private var mAutoSlideTime = 1000L
    private lateinit var mInterpolator: TimeInterpolator
    private var i = 0
    private val mAutoSlideRunnable by lazy {
        object : Runnable {
            override fun run() {
                fakeDrag(1, mAutoSlideTime, mInterpolator)
                postDelayed(this, mDelayTime)
            }
        }
    }


    private fun init() {
        cardElevation = 0F
        initViewPager2()
        mRunnableManger.post {
            setCurrentItem(finalItem, false)
        }
    }

    private fun initViewPager2() {
        mViewPager2.layoutParams = LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        )
        mViewPager2.registerOnPageChangeCallback(mPageChangeCallback)
        mViewPager2.orientation = mAttrs.orientation
        mViewPager2.setBackgroundColor(0x00000000)
        mViewPager2.getChildAt(0).setBackgroundColor(0x00000000)
        attachViewToParent(mViewPager2, 0, mViewPager2.layoutParams)
    }

    /**
     * 设置 ViewPager2 内部页面的边距，Orientation 为水平时设置左右的间隔，垂直时设置上下的间隔
     *
     * **NOTICE：**
     *
     * 1、slide_imgWight 为 match_parent 时，设置 pageInterval，图片宽度 **会** 改变，
     *   两图片的间距为 slide_imgMarginHorizontal 的值的两倍，显示的图片到外部页面的间距为
     *   slide_imgMarginHorizontal + pageInterval
     *
     * 2、slide_imgWight 为 具体值 时，设置 pageInterval，图片宽度不会改变，
     *   且设置的 slide_imgMarginHorizontal 无效，此时两图片的间距为 (初始的ImageView.left - pageInterval) * 2
     *
     * **NOTICE：** 感觉麻烦可以使用另一个同名方法
     *
     * **WARNING：** 只有在未加载视图时设置才有效
     */
    private fun setPageInterval() {
        mRunnableManger.post {
            if (mViewPager2.adapter is BaseImgAdapter<*>) {
                val distance = if (mAttrs.imgWidth == ViewGroup.LayoutParams.MATCH_PARENT) {
                    mAttrs.pageInterval
                }else {
                    /*
                    * 两图片的间距为 (初始的ImageView.left - pageInterval) * 2，我使用了 distance 做中间值来转换
                    * */
                    (width - mAttrs.imgWidth) / 2 - mAttrs.pageInterval / 2
                }
                val childView = mViewPager2.getChildAt(0) as RecyclerView
                when (getOrientation()) {
                    ViewPager2.ORIENTATION_HORIZONTAL -> {
                        childView.setPadding(distance, 0, distance, 0)
                    }
                    ViewPager2.ORIENTATION_VERTICAL -> {
                        childView.setPadding(0, distance, 0, distance)
                    }
                }
                childView.clipToPadding = false
            }
        }
    }

    private fun checkIsInItemCount(position: Int, positionMessage: String = "position") {
        var isError = false
        val itemCount = mViewPager2.adapter!!.itemCount
        if (mIsCirculateEnabled) {
            if (position !in 0 until itemCount - 4) {
                isError = true
            }
        }else {
            if (position !in 0 until itemCount) {
                isError = true
            }
        }
        if (isError) {
            throw IndexOutOfBoundsException(
                    "Your ${SlideShowAttrs.Library_name}#slowlySlide(): " +
                            "The $positionMessage is < 0 or >= itemCount")
        }
    }

    private var mPreFakeDrag = 0F
    private lateinit var mAnimator:  ValueAnimator
    private fun fakeDrag(diffPosition: Int, duration: Long, interpolator: TimeInterpolator = LinearInterpolator()) {
        mPreFakeDrag = 0F
        val childView = mViewPager2.getChildAt(0)
        val pixelDistance = if (getOrientation() == ViewPager2.ORIENTATION_HORIZONTAL) {
            mViewPager2.width - childView.paddingLeft - childView.paddingRight
        }else {
            mViewPager2.height - childView.paddingTop - childView.paddingBottom
        }
        mAnimator = ValueAnimator.ofFloat(0F, -diffPosition.toFloat())
        mAnimator.addUpdateListener {
            val nowFakeDrag = it.animatedValue as Float
            val differentOffsetPixel = (nowFakeDrag - mPreFakeDrag) * pixelDistance
            mViewPager2.fakeDragBy(differentOffsetPixel)
            mPreFakeDrag = nowFakeDrag
        }
        mAnimator.addListener(
                onStart = {
                    mViewPager2.beginFakeDrag()
                    mViewPager2.isUserInputEnabled = false
                },
                onEnd = {
                    mViewPager2.endFakeDrag()
                    mViewPager2.isUserInputEnabled = true
                },
                onCancel = {
                    mViewPager2.endFakeDrag()
                    mViewPager2.isUserInputEnabled = true
                }
        )
        mAnimator.interpolator = interpolator
        mAnimator.duration = duration
        mAnimator.start()
    }

    private var mInitialX =0
    private var mInitialY = 0
    private var mIsCirculateEnabled = false
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (mIsUserInputEnabled) {
            val x = ev.x.toInt()
            val y = ev.y.toInt()
            if (mViewPager2.adapter is BaseImgAdapter<*>) {
                when (ev.action) {
                    MotionEvent.ACTION_DOWN -> {
                        mInitialX = x
                        mInitialY = y
                        stop()
                    }
                    MotionEvent.ACTION_MOVE -> {
                        if (abs(y - mInitialY) < abs(x - mInitialX)) {
                            requestDisallowInterceptTouchEvent(true)
                        }
                    }
                    MotionEvent.ACTION_UP -> {
                        start()
                    }
                    MotionEvent.ACTION_CANCEL -> {
                        start()
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stop()
        mRunnableManger.destroy()
    }

    private var mIsOpenNestedScroll = false
    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        if (!mIsOpenNestedScroll)
            return false
        if (target == mViewPager2.getChildAt(0)) {
            return false
        }
        if (type == ViewCompat.TYPE_TOUCH) {
            if (getOrientation() == ViewPager2.ORIENTATION_HORIZONTAL && axes == ViewCompat.SCROLL_AXIS_HORIZONTAL
                || getOrientation() == ViewPager2.ORIENTATION_VERTICAL && axes == ViewCompat.SCROLL_AXIS_VERTICAL
            ) {
                return true
            }
        }
        return false
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int) {
        mParentHelper.onNestedScrollAccepted(child, target, axes, type)
        mViewPager2.isUserInputEnabled = false
        mViewPager2.beginFakeDrag()
    }

    override fun onStopNestedScroll(target: View, type: Int) {
        mParentHelper.onStopNestedScroll(target, type)
        mViewPager2.endFakeDrag()
        mViewPager2.isUserInputEnabled = true
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {
        if (type == ViewCompat.TYPE_TOUCH) {
            if (getOrientation() == ViewPager2.ORIENTATION_HORIZONTAL) {
                mViewPager2.fakeDragBy(-dxUnconsumed.toFloat())
            }else {
                mViewPager2.fakeDragBy(-dyUnconsumed.toFloat())
            }
        }
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
    }
}