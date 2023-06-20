package com.laydown.srouter.demo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.laydown.srouter.R
import com.laydown.srouter.annotation.Route

@Route(path = "/app/demo/product/fragment")
class ProductFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_product, container, false)
        initView(view)
        return view
    }

    private fun getArgs(): String {
        return if (arguments?.getString("name") != null) arguments?.getString("name")!! else ""
    }

    private fun initView(view: View) {
        view.findViewById<TextView>(R.id.tv_fragment_product).apply {
            setOnClickListener {
                text = getArgs()
            }
        }
        view.findViewById<Button>(R.id.btn_result).apply {
            setOnClickListener {
                val resultIntent = Intent()
                resultIntent.putExtra("uName", "legend2")
                resultIntent.putExtra("uAge", 22.toShort())
                this@ProductFragment.activity?.setResult(AppCompatActivity.RESULT_OK, resultIntent)
                this@ProductFragment.activity?.finish()
            }
        }
    }
}