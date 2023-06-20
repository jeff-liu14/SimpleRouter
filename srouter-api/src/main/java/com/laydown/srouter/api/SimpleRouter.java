package com.laydown.srouter.api;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Keep;

import com.laydown.srouter.api.model.TargetMeta;

@Keep
public class SimpleRouter {
    private static SimpleRouter router;

    private boolean isInit = false;

    public static synchronized SimpleRouter getInstance() {
        if (router == null) {
            router = new SimpleRouter();
        }
        return router;
    }

    public void init(Application application) {
        if (!isInit) {
            isInit = _SimpleRouter.getInstance().init(application);
        }
    }

    /**
     * 加载路由文件
     *
     * @param simpleRouterKey 为路由文件加密/解密提供key，长度16
     */
    public void scanRoute(String simpleRouterKey, Boolean isOpenAes) {
        if (simpleRouterKey.length() < 16) {
            throw new RuntimeException("simpleRouterKey长度必须为16");
        }
        _SimpleRouter simpleRouter = _SimpleRouter.getInstance();
        simpleRouter.setSimpleRouterKey(simpleRouterKey);
        initSimpleRouter(simpleRouter, isOpenAes);
    }

    private void initSimpleRouter(_SimpleRouter simpleRouter, Boolean isOpenAes) {
        simpleRouter.scanDestinationFromAsset(isOpenAes);
    }

    public TargetMeta build(String pageUrl) {
        return _SimpleRouter.getInstance().build(pageUrl);
    }

    public Object navigate(TargetMeta targetMeta) {
        return _SimpleRouter.getInstance().navigate(targetMeta);
    }

    public Object navigate(Context context, TargetMeta targetMeta) {
        return _SimpleRouter.getInstance().navigate(context, targetMeta);
    }

    public void navigateForResult(TargetMeta targetMeta, int requestCode) {
        _SimpleRouter.getInstance().navigateForResult(targetMeta, requestCode);
    }

    public void navigateForResult(Context context, TargetMeta targetMeta, int requestCode) {
        _SimpleRouter.getInstance().navigateForResult(context, targetMeta, requestCode);
    }

    public Intent navigateForResultX(TargetMeta targetMeta) {
        return _SimpleRouter.getInstance().navigateForResultX(targetMeta);
    }

    public Intent navigateForResultX(Context context, TargetMeta targetMeta) {
        return _SimpleRouter.getInstance().navigateForResultX(context, targetMeta);
    }
}
