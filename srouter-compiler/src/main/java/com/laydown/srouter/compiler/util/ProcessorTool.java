package com.laydown.srouter.compiler.util;


import com.alibaba.fastjson.JSONObject;
import com.laydown.srouter.annotation.Route;

import java.util.HashMap;
import java.util.Set;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

import static com.laydown.srouter.compiler.util.Const.ACTIVITY;
import static com.laydown.srouter.compiler.util.Const.FRAGMENT;
import static com.laydown.srouter.compiler.util.Const.FRAGMENT_V4;
import static com.laydown.srouter.compiler.util.Const.IPROVIDER;
import static com.laydown.srouter.compiler.util.Const.PAGE_TYPE_ACTIVITY;
import static com.laydown.srouter.compiler.util.Const.PAGE_TYPE_FRAGMENT;
import static com.laydown.srouter.compiler.util.Const.PAGE_TYPE_PROVIDER;

public class ProcessorTool {

    public static void handleDestination(Elements elementUtils, Types types, Messager messager, Set<? extends Element> elements, Class<Route> destinationClass, HashMap<String, JSONObject> destMap) {
        messager.printMessage(Diagnostic.Kind.NOTE, ">>>>>> handleDestination start <<<<<<");
        try {
            TypeMirror type_Activity = elementUtils.getTypeElement(ACTIVITY).asType();
            TypeMirror fragmentTm = elementUtils.getTypeElement(FRAGMENT).asType();
            TypeMirror fragmentTmV4 = elementUtils.getTypeElement(FRAGMENT_V4).asType();
            TypeMirror iProvider = elementUtils.getTypeElement(IPROVIDER).asType();

            messager.printMessage(Diagnostic.Kind.NOTE, ">>>>>> handleDestination end <<<<<<");
            for (Element element : elements) {
                TypeMirror tm = element.asType();
                TypeElement typeElement = (TypeElement) element;
                String clazName = typeElement.getQualifiedName().toString();

                Route annotation = typeElement.getAnnotation(destinationClass);
                String pageUrl = annotation.path();
                if (!pageUrl.startsWith("/")) {
                    messager.printMessage(Diagnostic.Kind.ERROR, "\npageUrl must start with \"/\"，current page url is ：" + pageUrl + "\ncurrent class ：" + clazName);
                }
                int id = Math.abs(clazName.hashCode());
                messager.printMessage(Diagnostic.Kind.NOTE, clazName);
                String destType = "";
                if (types.isSubtype(tm, type_Activity) || types.isSubtype(tm, fragmentTm) || types.isSubtype(tm, fragmentTmV4)) {
                    if (types.isSubtype(tm, type_Activity)) {
                        destType = PAGE_TYPE_ACTIVITY.toLowerCase();
                    } else {
                        destType = PAGE_TYPE_FRAGMENT.toLowerCase();
                    }
                } else if (types.isSubtype(tm, iProvider)) {
                    destType = PAGE_TYPE_PROVIDER.toLowerCase();
                } else {
                    throw new RuntimeException("The @Route is marked on unsupported class, look at [" + tm.toString() + "].");
                }

                if (destMap.containsKey(pageUrl)) {
                    messager.printMessage(Diagnostic.Kind.ERROR, "不同的页面不允许使用相同的pageUrl" + pageUrl);
                } else {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("clazzName", clazName);
                    jsonObject.put("path", pageUrl);
                    jsonObject.put("id", id);
                    jsonObject.put("destType", destType);

                    destMap.put(pageUrl, jsonObject);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            messager.printMessage(Diagnostic.Kind.NOTE, ">>>>>> handleDestination start err <<<<<< \n" + ex.getLocalizedMessage());
        }
    }
}
