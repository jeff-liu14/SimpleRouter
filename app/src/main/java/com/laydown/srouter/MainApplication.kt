package com.laydown.srouter

import android.app.Application
import androidx.multidex.MultiDex
import com.laydown.srouter.api.SimpleRouter

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        SimpleRouter.getInstance().apply {
            init(this@MainApplication)
            scanRoute(BuildConfig.SIMPLE_ROUTER_KEY, BuildConfig.OPEN_AES)
        }
    }
}