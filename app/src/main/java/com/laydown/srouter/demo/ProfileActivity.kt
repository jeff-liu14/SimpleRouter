package com.laydown.srouter.demo

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.laydown.srouter.R
import com.laydown.srouter.annotation.Route

@Route(path = "/app/demo/profile")
class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        findViewById<TextView>(R.id.tv_profile).apply {
            setOnClickListener {
                val bundle = intent.extras
                if (bundle != null) {
                    val name = bundle.getString("name")
                    text = name
                }
            }
        }
    }
}