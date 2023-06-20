package com.laydown.srouter.demo

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.laydown.srouter.R
import com.laydown.srouter.annotation.Route
import com.laydown.srouter.api.SimpleRouter

@Route(path = "/app/demo/product")
class ProductActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        setFragment()
    }


    private fun setFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        val fragment =
            SimpleRouter.getInstance()
                .build("/app/demo/product/fragment")
                .withString("name", intent.extras?.getString("name"))
                .navigate() as Fragment
        fragment.arguments = intent.extras

        transaction.add(R.id.fl_content, fragment, fragment.javaClass.simpleName)
        transaction.commitNowAllowingStateLoss()
    }
}