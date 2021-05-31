package com.greenhand.cooperativework.viewmodel.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.greenhand.cooperativework.bean.CommunityFirstRecommendBean
import com.greenhand.cooperativework.bean.CommunityRecommendBean
import com.greenhand.cooperativework.repository.fragment.CommunityRecommendRepository
import com.greenhand.cooperativework.utils.toast
import com.greenhand.cooperativework.view.fragment.community.CommunityRecommendFragment
import retrofit2.Call
import retrofit2.Response

class CommunityRecommendViewModel : ViewModel() {
    private lateinit var communityRecommendFragment: CommunityRecommendFragment
    private var mImageList = ArrayList<CommunityRecommendBean.Content>()//储存所有图片的content对象
    private var mVideoList = ArrayList<CommunityRecommendBean.Content>()//储存所有视频的content对象
    private var mBannerList = ArrayList<CommunityFirstRecommendBean.ItemX>()//储存所有轮播图的content对象
    private var mRecommendList = ArrayList<Any>()//储存 轮播图 图片 视频 集合 依照顺序
    private var loadDataTime = 1//记录获取了多少组数据 12个image 1个video 为一组 若一次网络请求获取到的数据不足 则递归继续获取 直到获取了一组数据
    private var mNextPageUrl: String = ""//记录处理后的下一页的Url

    private val repository by lazy {
        CommunityRecommendRepository()
    }

    val recommendList by lazy {
        MutableLiveData<MutableList<Any>>()
    }

    /**
     * 第一次获取推荐的Bean类与之后获取的推荐Bean类不一样
     */
    fun loadData(nextPageUrl: String) {
        if (nextPageUrl == "") {//首次获取推荐
            //第一次加载初始化相关数据 主要针对下拉刷新
            mImageList.clear()
            mVideoList.clear()
            mBannerList.clear()
            mRecommendList.clear()
            loadDataTime = 1
            val result = repository.getCommunityFirstRecommendByPage(nextPageUrl)//第一页nextPageUrl为空
            result.enqueue(object : retrofit2.Callback<CommunityFirstRecommendBean> {
                override fun onResponse(
                    call: Call<CommunityFirstRecommendBean>,
                    response: Response<CommunityFirstRecommendBean>
                ) {
                    handleFirstData(response)
                }

                override fun onFailure(call: Call<CommunityFirstRecommendBean>, t: Throwable) {
                    "网络错误!".toast()
                }
            })
        } else {//不是首次获取推荐
            val result = repository.getCommunityRecommendByPage(mNextPageUrl, "2")//第一页nextPageUrl为空
            result.enqueue(object : retrofit2.Callback<CommunityRecommendBean> {
                override fun onResponse(
                    call: Call<CommunityRecommendBean>,
                    response: Response<CommunityRecommendBean>
                ) {
                    handleData(response)
                }

                override fun onFailure(call: Call<CommunityRecommendBean>, t: Throwable) {
                    "网络错误!".toast()
                }
            })
        }

    }

    /**
     * 仅获取轮播图
     */
    private fun handleFirstData(response: Response<CommunityFirstRecommendBean>) {

        val result = response.body()
        //总list
        var allList = result?.itemList

        if (allList != null) {
            for (i in allList.indices) {
                if (allList[i].data.dataType == "HorizontalScrollCard") {//此时可获取轮播图数据
                    mBannerList.clear()
                    mBannerList.addAll(allList[i].data.itemList)
                }
            }
            /**
             * 获取并处理下一页Url
             */
            if (result != null) {
                var nextPageUrl = result.nextPageUrl
                //去掉baseUrl
                nextPageUrl =
                    nextPageUrl.replace(
                        "http://baobab.kaiyanapp.com/api/v7/community/tab/rec?startScore=",
                        ""
                    )
                nextPageUrl = nextPageUrl.replace("&pageCount=2", "")
                loadData(nextPageUrl)
            }
        }
    }

    /**
     * 之后的处理 获取图片/视频
     */
    private fun handleData(response: Response<CommunityRecommendBean>) {
        val result = response.body()
        //总list
        var allList =
            result?.itemList
        /**
         * 获取并处理下一页Url
         */
        if (result != null) {
            mNextPageUrl = result.nextPageUrl
            //去掉baseUrl
            mNextPageUrl =
                mNextPageUrl.replace(
                    "http://baobab.kaiyanapp.com/api/v7/community/tab/rec?startScore=",
                    ""
                )
            mNextPageUrl = mNextPageUrl.replace("&pageCount=2", "")
        }
        if (allList != null) {
            for (i in allList.indices) {
                if (allList[i].type == "communityColumnsCard") {//此时可获取图片/视频数据
                    //通过判断类型 取到相对应的数据
                    when (allList[i].data.content.type) {
                        "ugcPicture" -> {
                            if (mImageList.size < 12 * loadDataTime) {
                                mImageList.add(allList[i].data.content)

                            }
                        }
                        "video" -> {
                            if (mVideoList.size < 2 * loadDataTime) {
                                mVideoList.add(allList[i].data.content)

                            }
                        }
                    }
                }
            }
        }
        if (mRecommendList.size == 0) {
            mRecommendList.run {
                add(mBannerList)
                add(mImageList)
                add(mVideoList)
            }
        }
        //判断是否满足一组数据 若不满足则递归继续添加
        if (mImageList.size == 12 * loadDataTime && mVideoList.size == 2 * loadDataTime) {
            recommendList.postValue(mRecommendList)
            loadDataTime++
            //回调更新关系/加载状态
            communityRecommendFragment.finishLoad()
            communityRecommendFragment.finishRefresh()
        } else {
            loadData(mNextPageUrl)
        }

    }

    fun setFragment(communityRecommendFragment: CommunityRecommendFragment) {
        this.communityRecommendFragment = communityRecommendFragment
    }


}