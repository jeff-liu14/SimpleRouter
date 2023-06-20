package com.laydown.srouter.api;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import androidx.core.app.ActivityCompat;

import com.alibaba.fastjson.JSONObject;
import com.laydown.srouter.api.model.RouterMeta;
import com.laydown.srouter.api.model.TargetMeta;
import com.laydown.srouter.api.provider.IProvider;
import com.laydown.srouter.api.util.AesHelper;
import com.laydown.srouter.api.util.FileUtil;
import com.laydown.srouter.api.util.JsonTool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import static com.laydown.srouter.api.util.Const.END_FIX;

public class _SimpleRouter {
    private Context mContext;

    private HashMap<String, RouterMeta> routerMetaMap;

    private static _SimpleRouter router;

    private static Handler mHandler;

    private String SIMPLE_ROUTER_KEY = "";

    public static synchronized _SimpleRouter getInstance() {
        if (router == null) {
            router = new _SimpleRouter();
        }
        return router;
    }

    public boolean init(Application application) {
        mContext = application;
        routerMetaMap = new HashMap<>();
        mHandler = new Handler(Looper.getMainLooper());
        return true;
    }

    public void setSimpleRouterKey(String simpleRouterKey) {
        this.SIMPLE_ROUTER_KEY = simpleRouterKey;
    }

    public void scanDestinationFromAsset(Boolean isOpenAes) {
        String[] nameList = FileUtil.getAllAssetsList(mContext, "");
        ArrayList<String> stringArrayList = FileUtil.filterArray(nameList, END_FIX);
        if (stringArrayList.size() > 0) {
            routerMetaMap.clear();
            for (String item : stringArrayList) {
                String decryptContent = "";
                String jsonString = FileUtil.getAssetFile(mContext.getAssets(), item);
                if (!TextUtils.isEmpty(SIMPLE_ROUTER_KEY) && isOpenAes) {
                    decryptContent = AesHelper.decrypt(jsonString, SIMPLE_ROUTER_KEY);
                } else {
                    decryptContent = jsonString;
                }
                loadModuleFile(decryptContent);
            }
        } else {
            throw new RuntimeException("路由加载异常，请检查asset文件夹中是否存在*.json.simple文件");
        }
    }

    private void loadModuleFile(String jsonString) {
        HashMap<String, JSONObject> json2Map = JsonTool.json2Map(jsonString);
        Iterator<HashMap.Entry<String, JSONObject>> iterator = json2Map.entrySet().iterator();
        while (iterator.hasNext()) {
            HashMap.Entry<String, JSONObject> entry = iterator.next();
            String key = entry.getKey();
            JSONObject value = entry.getValue();
            System.out.println(key);
            System.out.println(value.toString());
            if (!routerMetaMap.containsKey(key)) {
                RouterMeta routerMeta = JsonTool.json2Bean(value.toJSONString(), RouterMeta.class);
                routerMetaMap.put(key, routerMeta);
            } else {
                RouterMeta oldRouterMeta = routerMetaMap.get(key);
                RouterMeta routerMeta = JsonTool.json2Bean(value.toJSONString(), RouterMeta.class);
                throw new RuntimeException("\npageUrl -> \"" + key + "\" has exist; \n old route class is -> "
                        + oldRouterMeta.getClazzName()
                        + "\n new route class is -> "
                        + routerMeta.getClazzName());
            }
        }
    }

    public HashMap<String, RouterMeta> getRouterMetaMap() {
        return routerMetaMap;
    }

    public Object navigate(TargetMeta targetMeta) {
        return realNavigate(mContext, targetMeta, -1);
    }

    public Object navigate(Context context, TargetMeta targetMeta) {
        return realNavigate(context, targetMeta, -1);
    }

    public void navigateForResult(TargetMeta targetMeta, int requestCode) {
        realNavigate(mContext, targetMeta, requestCode);
    }

    public void navigateForResult(Context context, TargetMeta targetMeta, int requestCode) {
        realNavigate(context, targetMeta, requestCode);
    }

    public Intent navigateForResultX(TargetMeta targetMeta) {
        return realNavigateX(mContext, targetMeta);
    }

    public Intent navigateForResultX(Context context, TargetMeta targetMeta) {
        return realNavigateX(context, targetMeta);
    }

    private Intent realNavigateX(Context context, TargetMeta targetMeta) {
        Intent intent = new Intent(context, targetMeta.getaClass());
        if (context instanceof Application) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.putExtras(targetMeta.getmBundle());
        if (context instanceof Activity) {
            return intent;
        } else {
            throw new RuntimeException("Must use [navigation(activity, ...)] to support [startActivityForResult]");
        }
    }

    private Object realNavigate(Context context, TargetMeta targetMeta, int requestCode) {
        if (!targetMeta.getmType().isEmpty()) {
            switch (targetMeta.getmType()) {
                case "activity":
                    Intent intent = new Intent(context, targetMeta.getaClass());
                    if (context instanceof Application) {
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    }
                    intent.putExtras(targetMeta.getmBundle());

                    runInMainThread(() -> {
                        if (requestCode > 0) {
                            if (context instanceof Activity) {
                                ActivityCompat.startActivityForResult((Activity) context, intent, requestCode, targetMeta.getmBundle());
                            } else {
                                throw new RuntimeException("Must use [navigation(activity, ...)] to support [startActivityForResult]");
                            }
                        } else {
                            ActivityCompat.startActivity(context, intent, targetMeta.getmBundle());
                        }

                    });
                    break;
                case "fragment":
                    Class<?> fragmentMeta = targetMeta.getaClass();
                    try {
                        Object instance = fragmentMeta.getConstructor().newInstance();
                        if (instance instanceof Fragment) {
                            ((Fragment) instance).setArguments(targetMeta.getmBundle());
                        } else if (instance instanceof androidx.fragment.app.Fragment) {
                            ((androidx.fragment.app.Fragment) instance).setArguments(targetMeta.getmBundle());
                        } else {
                            // noting
                            String nothing = "";
                        }
                        return instance;
                    } catch (Exception e) {
                        throw new RuntimeException("Fetch fragment instance error, " + e.getLocalizedMessage());
                    }
                case "provider":
                    Class<? extends IProvider> providerClazz = (Class<? extends IProvider>) targetMeta.getaClass();
                    IProvider provider;
                    try {
                        provider = providerClazz.getConstructor().newInstance();
                        provider.init(context);
                        return provider;
                    } catch (Exception e) {
                        throw new RuntimeException("Fetch provider instance error, " + e.getLocalizedMessage());
                    }
                default:
                    break;
            }
            return null;
        }
        return null;
    }

    public TargetMeta build(String pageUrl) {
        HashMap<String, RouterMeta> routerMetaHashMap = _SimpleRouter.getInstance().getRouterMetaMap();
        if (routerMetaHashMap.containsKey(pageUrl)) {
            RouterMeta routerMeta = routerMetaHashMap.get(pageUrl);
            return parseRouterMeta(routerMeta);
        }
        throw new RuntimeException("not find pageUrl:" + pageUrl + "，please check again!!" + "\n already register：" + routerMetaHashMap.keySet().toString());
    }

    private void runInMainThread(Runnable runnable) {
        if (Looper.getMainLooper().getThread() != Thread.currentThread()) {
            mHandler.post(runnable);
        } else {
            runnable.run();
        }
    }

    private TargetMeta parseRouterMeta(RouterMeta routerMeta) {
        TargetMeta targetMeta = new TargetMeta();
        targetMeta.setId(routerMeta.getId());
        targetMeta.setPageUrl(routerMeta.getPageUrl());
        targetMeta.setmType(routerMeta.getDestType());
        try {
            targetMeta.setaClass(Class.forName(routerMeta.getClazzName()));
        } catch (ClassNotFoundException exception) {
            exception.printStackTrace();
            throw new RuntimeException("路由：" + routerMeta.getPageUrl() + "，对应的类：" + routerMeta.getClazzName() + "不存在，请检查后重试!!");
        }
        return targetMeta;
    }

}
