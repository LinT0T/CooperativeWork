package com.greenhand.cooperativework.viewmodel.activity

import android.util.Log
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
         * 注:因为接口问题 部分视频获取推荐视频可能为空 故若为空则获取一个固定的相关视频List id为186856
         */
        val relevantVideoResult = repository.getRelevantVideoById(id)
        relevantVideoResult.enqueue(object : retrofit2.Callback<RelevantVideoBean> {
            override fun onResponse(
                call: Call<RelevantVideoBean>,
                response: Response<RelevantVideoBean>
            ) {
                Log.d("zz", id.toString() + " knj")
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
                    getReply(id)
                } else {
                    loadData("186856")
                }
            }

            override fun onFailure(call: Call<RelevantVideoBean>, t: Throwable) {
                "网络错误!".toast()
            }
        })


    }

    fun getReply(id: String?) {
        /**
         * 获取视频回复
         * 注:部分社区推荐中的视频获取评论可能为空 但评论数却不是0 这是接口问题
         *    获取的评论可能为空 即该视频没人评论
         */
        if (id != "186856") {
            //不获取评论 没办法接口有点问题
            val videoReplyResult = repository.getVideoReplyById(id)
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
                            if (itemList[i].data.text == "最新评论") {
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
        }
        postValue()
    }

    fun postValue() {
        //获取两个数据后 postValue
        if (mVideoMessageList.size < 2) {
            mVideoMessageList.add(mRelevantVideoList)
            mVideoMessageList.add(mVideoReplyList)
        }
        videoMessageList.postValue(mVideoMessageList)
    }
}