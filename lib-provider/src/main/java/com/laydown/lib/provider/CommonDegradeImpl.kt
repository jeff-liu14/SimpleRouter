package com.laydown.lib.provider

import android.content.Context
import android.widget.Toast
import com.laydown.srouter.api.interceptor.DegradeService
import com.laydown.srouter.api.model.TargetMeta

class CommonDegradeImpl : DegradeService {
    override fun onLost(context: Context, targetMeta: TargetMeta) {
        Toast.makeText(context, "Path: " + targetMeta.path + " Lost.", Toast.LENGTH_SHORT).show()
    }
}