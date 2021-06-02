package com.greenhand.cooperativework.utils

import androidx.lifecycle.*

/**
 * 用协程请求网络数据
 * @author 985892345
 * @email 2767465918@qq.com
 * @data 2021/6/1
 */
fun <X, Y> switchMap(source: LiveData<X>, switchMapFunction: () -> Y) = Transformations.switchMap(source) {
    MutableLiveData(switchMapFunction())
}

fun <X, Y> ViewModel.switchMapLaunch(source: LiveData<X>, block: suspend () -> Y) = Transformations.switchMap(source) {
    liveData(viewModelScope.coroutineContext) {
        val result = kotlin.runCatching { block() }.onFailure { it.message?.toast() }
        emit(result.getOrNull())
    }
}

fun <T> ViewModel.liveDataLaunch(block: suspend () -> T) = liveData(viewModelScope.coroutineContext) {
    val result = runCatching { block() }.onFailure { it.message?.toast() }
    emit(result.getOrNull())
}
