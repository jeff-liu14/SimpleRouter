package com.laydown.srouter

import android.app.Application
import androidx.multidex.MultiDex
import com.laydown.srouter.api.SimpleRouter

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        SimpleRouter.getInstance().apply {
            // 路由初始化
            init(this@MainApplication)
            /**
             *  设置路由文件加密key及是否开启加密算法；
             *  SIMPLE_ROUTER_KEY和OPEN_AES需要与build.gradle中配置的参数保持一致
             */
            scanRoute(BuildConfig.SIMPLE_ROUTER_KEY, BuildConfig.OPEN_AES)
        }
    }
}