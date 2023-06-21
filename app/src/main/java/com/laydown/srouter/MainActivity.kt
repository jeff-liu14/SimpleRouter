package com.laydown.srouter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import com.laydown.srouter.annotation.Route
import com.laydown.srouter.api.SimpleRouter
import com.laydown.srouter.tool.Helper

@Route(path = "/app/home")
class MainActivity : AppCompatActivity() {
    var launcher: ActivityResultLauncher<Intent>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        launcher = Helper.startActivityForResult(this, intent) { result ->
            when (result?.resultCode) {
                RESULT_OK -> {
                    Toast.makeText(
                        this, result.data?.extras?.getString("uName"), Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        findViewById<Button>(R.id.btn_load_route).setOnClickListener {
            SimpleRouter.getInstance()
                .scanRoute(BuildConfig.SIMPLE_ROUTER_KEY, BuildConfig.OPEN_AES)
        }
        findViewById<Button>(R.id.btn_product).setOnClickListener {
            val intent = SimpleRouter.getInstance()
                .build("/app/demo/product")
                .withString("name", "app-product:透传参数")
                .navigateForResultX(this)
            launcher?.launch(intent)
        }
        findViewById<Button>(R.id.btn_profile).setOnClickListener {
            SimpleRouter.getInstance()
                .build("/app/demo/profile")
                .withString("name", "app-profile:透传参数")
                .navigate(this)
        }
        findViewById<Button>(R.id.btn_shop).setOnClickListener {
            SimpleRouter.getInstance()
                .build("/biz/shop/home")
                .withString("name", "biz-shop:透传参数")
                .navigate(this)
        }
        findViewById<Button>(R.id.btn_tax).setOnClickListener {
            SimpleRouter.getInstance()
                .build("/biz/tax/home")
                .withString("name", "biz-tax:透传参数;from->app-main")
                .navigate(this)
        }
    }
}