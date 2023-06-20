package com.laydown.biz.shop

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.laydown.lib.provider.ITaxProvider
import com.laydown.srouter.annotation.Route
import com.laydown.srouter.api.SimpleRouter

@Route(path = "/biz/shop/home")
class ShopActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)
        findViewById<TextView>(R.id.tv_shop).apply {
            setOnClickListener {
                val bundle = intent.extras
                if (bundle != null) {
                    val name = bundle.getString("name")
                    text = name
                }
            }
        }

        findViewById<Button>(R.id.btn_tax).apply {
            setOnClickListener {
                SimpleRouter.getInstance()
                    .build("/biz/tax/home")
                    .withString("name", "biz-tax:透传参数;from->biz-shop")
                    .navigate(this@ShopActivity)
            }
        }

        findViewById<Button>(R.id.btn_tax_provider).apply {
            setOnClickListener {
                val provider: ITaxProvider = SimpleRouter.getInstance()
                    .build("/tax/provider")
                    .navigate(this@ShopActivity) as ITaxProvider
                provider.sayHello("ShopActivity call Tax Provider")
            }
        }
    }
}