package com.laydown.biz.tax

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.laydown.srouter.annotation.Route

@Route(path = "/biz/tax/home")
class TaxActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tax)
        findViewById<TextView>(R.id.tv_tax).apply {
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