package com.greenhand.cooperativework.viewmodel.activity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 *@author 985892345
 *@email 2767465918@qq.com
 *@data 2021/5/25
 *@description
 */
class WelcomeViewModel : ViewModel() {

    companion object {
        const val bigText = "开眼\n看更好\n的世界"
        const val smallText = "FEED YOUR EYES,\nFEED YOUR SOUL."
    }

    val textColor = MutableLiveData<Int>()
}