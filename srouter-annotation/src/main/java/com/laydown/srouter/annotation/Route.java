package com.laydown.srouter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
public @interface Route {
    /**
     * 页面在路由中的名称
     */
    String path();
}
