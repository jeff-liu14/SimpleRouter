package com.laydown.biz.tax;

import android.content.Context;
import android.widget.Toast;

import com.laydown.lib.provider.ITaxProvider;
import com.laydown.srouter.annotation.Route;

/**
 * 在biz-tax模块中实现ITaxProvider接口
 * 并且使用@Route标签注册服务
 */
@Route(path = "/tax/provider")
public class TaxProviderImpl implements ITaxProvider {
    private Context mContext;

    @Override
    public void sayHello(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void init(Context context) {
        this.mContext = context;
    }
}
