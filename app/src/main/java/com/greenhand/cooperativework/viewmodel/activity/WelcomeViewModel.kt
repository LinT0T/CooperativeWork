package com.greenhand.cooperativework.viewmodel.activity

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.greenhand.cooperativework.base.BaseApplication
import com.greenhand.cooperativework.repository.activity.WelcomeRepository
import kotlinx.coroutines.launch

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
    val imgUrl = MutableLiveData<String>()

    init {
        launch {
            imgUrl.value = WelcomeRepository.getImgUrl()
        }
    }

    private fun launch(block: suspend () -> Unit) = viewModelScope.launch {
        try {
            block()
        }catch (e: Throwable) {
            Toast.makeText(BaseApplication.appContext, e.message, Toast.LENGTH_SHORT).show()
        }
    }
}