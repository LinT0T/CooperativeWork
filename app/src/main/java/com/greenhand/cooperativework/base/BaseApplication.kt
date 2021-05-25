package com.greenhand.cooperativework.base

import android.app.Application
import android.content.Context

/**
 *@author 985892345
 *@email 2767465918@qq.com
 *@data 2021/5/25
 *@description
 */
class BaseApplication : Application() {

    companion object {
        lateinit var appContext : Context
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
    }
}