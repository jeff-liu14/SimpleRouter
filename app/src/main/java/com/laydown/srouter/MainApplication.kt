package com.laydown.srouter

import android.app.Application
import androidx.multidex.MultiDex
import com.laydown.lib.provider.CommonDegradeImpl
import com.laydown.lib.provider.CommonInterceptorImpl
import com.laydown.srouter.api.SimpleRouter

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        SimpleRouter.init(this)
        SimpleRouter.scanRoute(BuildConfig.SIMPLE_ROUTER_KEY, BuildConfig.OPEN_AES)
        SimpleRouter.setInterceptorCallBack(CommonInterceptorImpl())
        SimpleRouter.setDegradeService(CommonDegradeImpl())
    }
}