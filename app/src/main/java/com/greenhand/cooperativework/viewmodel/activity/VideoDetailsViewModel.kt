package com.greenhand.cooperativework.viewmodel.activity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.greenhand.cooperativework.bean.RelevantVideoBean
import com.greenhand.cooperativework.bean.VideoReplyBean
import com.greenhand.cooperativework.repository.activity.VideoDetailsRepository
import com.greenhand.cooperativework.utils.toast
import retrofit2.Call
import retrofit2.Response

class VideoDetailsViewModel : ViewModel() {
    private var mRelevantVideoList = ArrayList<RelevantVideoBean.Data>()//储存所有图片的content对象
    private var mVideoReplyList = ArrayList<VideoReplyBean.Data>()//储存所有视频的content对象
    private var mVideoMessageList = ArrayList<Any>()//储存 轮播图 图片 视频 集合 依照顺序
    private val repository by lazy {
        VideoDetailsRepository()
    }

    val videoMessageList by lazy {
        MutableLiveData<MutableList<Any>>()
    }

    fun loadData(id: String?) {
        /**
         * 获取推荐视频
         * 注:因为接口问题 部分视频获取推荐视频可能为空
         */
        val relevantVideoResult = repository.getRelevantVideoById(id)//第一页nextPageUrl为空
        relevantVideoResult.enqueue(object : retrofit2.Callback<RelevantVideoBean> {
            override fun onResponse(
                call: Call<RelevantVideoBean>,
                response: Response<RelevantVideoBean>
            ) {
                val result = response.body()
                //总list
                var itemList = result?.itemList
                if (itemList != null) {
                    for (i in itemList.indices) {
                        //如果集合中数据类型是视频
                        if (itemList[i].data.dataType == "VideoBeanForClient") {
                            //最多获取10个推荐视频
                            if (mRelevantVideoList.size < 10) {
                                mRelevantVideoList.add(itemList[i].data)
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<RelevantVideoBean>, t: Throwable) {
                "网络错误!".toast()
            }
        })

        /**
         * 获取视频回复
         * 注:部分视频获取评论可能为空 即该视频没人评论
         */
        val videoReplyResult = repository.getVideoReplyById(id)//第一页nextPageUrl为空
        videoReplyResult.enqueue(object : retrofit2.Callback<VideoReplyBean> {
            override fun onResponse(
                call: Call<VideoReplyBean>,
                response: Response<VideoReplyBean>
            ) {
                val result = response.body()
                //总list
                var itemList = result?.itemList
                if (itemList != null) {
                    for (i in itemList.indices) {
                        //由于该接口数据处理太不方便 故仅获取部分最新评论
                        if (itemList[i].type == "leftAlignTextHeader") {
                            //取最新评论后的所有评论
                            for (j in i + 1 until itemList.size) {
                                mVideoReplyList.add(itemList[j].data)
                            }
                            break
                        }
                    }
                }
            }

            override fun onFailure(call: Call<VideoReplyBean>, t: Throwable) {
                "网络错误!".toast()
            }
        })

        //获取两个数据后 postValue
        mVideoMessageList.add(mRelevantVideoList)
        mVideoMessageList.add(mVideoReplyList)
        videoMessageList.postValue(mVideoMessageList)
    }
}