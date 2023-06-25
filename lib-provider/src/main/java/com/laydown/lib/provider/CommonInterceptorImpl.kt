package com.laydown.lib.provider

import android.content.Context
import android.widget.Toast
import com.laydown.srouter.api.interceptor.InterceptorCallBack
import com.laydown.srouter.api.model.TargetMeta

class CommonInterceptorImpl : InterceptorCallBack {
    override fun onContinue(context: Context, targetMeta: TargetMeta): Boolean {
        when (targetMeta.path) {
            "/app/demo/product1" -> {
                Toast.makeText(context, "Product页面被拦截", Toast.LENGTH_SHORT).show()
                return false
            }
            "/app/demo/profile" -> {
                Toast.makeText(context, "Profile页面被拦截", Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return true
    }
}