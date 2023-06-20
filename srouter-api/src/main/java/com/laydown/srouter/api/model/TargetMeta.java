package com.laydown.srouter.api.model;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Keep;

import com.laydown.srouter.api.SimpleRouter;

@Keep
public class TargetMeta {

    private String pageUrl;

    private Integer id;

    private Class aClass;

    public String getmType() {
        return mType;
    }

    public void setmType(String mType) {
        this.mType = mType;
    }

    private String mType;

    public Bundle getmBundle() {
        return mBundle;
    }

    public void setmBundle(Bundle mBundle) {
        this.mBundle = mBundle;
    }

    private Bundle mBundle;

    public TargetMeta() {
        this(null);
    }

    public TargetMeta(Bundle bundle) {
        this.mBundle = (null == bundle ? new Bundle() : bundle);
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Class getaClass() {
        return aClass;
    }

    public void setaClass(Class aClass) {
        this.aClass = aClass;
    }

    public Object navigate() {
        return SimpleRouter.getInstance().navigate(this);
    }

    public Object navigate(Context context) {
        return SimpleRouter.getInstance().navigate(context, this);
    }

    public void navigateForResult(int requestCode) {
        SimpleRouter.getInstance().navigateForResult(this, requestCode);
    }

    public void navigateForResult(Context context, int requestCode) {
        SimpleRouter.getInstance().navigateForResult(context, this, requestCode);
    }

    public Intent navigateForResultX() {
        return SimpleRouter.getInstance().navigateForResultX(this);
    }

    public Intent navigateForResultX(Context context) {
        return SimpleRouter.getInstance().navigateForResultX(context, this);
    }


    public TargetMeta withString(String key, String value) {
        mBundle.putString(key, value);
        return this;
    }

    public TargetMeta withBoolean(String key, boolean value) {
        mBundle.putBoolean(key, value);
        return this;
    }

    public TargetMeta withInt(String key, int value) {
        mBundle.putInt(key, value);
        return this;
    }

    public TargetMeta withLong(String key, long value) {
        mBundle.putLong(key, value);
        return this;
    }

    public TargetMeta withFloat(String key, float value) {
        mBundle.putFloat(key, value);
        return this;
    }

    public TargetMeta WithBundle(Bundle bundle) {
        this.mBundle = bundle;
        return this;
    }
}
