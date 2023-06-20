package com.laydown.srouter.compiler.util;

public class Const {
    public static final String ACTIVITY = "android.app.Activity";
    public static final String FRAGMENT = "android.app.Fragment";

    public static final String FRAGMENT_V4 = "android.support.v4.app.Fragment";

    public static final String IPROVIDER = "com.laydown.srouter.api.provider.IProvider";
    public static final String PAGE_TYPE_ACTIVITY = "Activity";
    public static final String PAGE_TYPE_FRAGMENT = "Fragment";
    public static final String PAGE_TYPE_PROVIDER = "Provider";
    public static final String OUTPUT_FILE_NAME_FIX = ".sr";
    public static final String OUTPUT_FILE_NAME = "_" + "router_path";
    public static final String SIMPLE_ROUTER_KEY = "SIMPLE_ROUTER_KEY";
    public static final String OPEN_AES = "OPEN_AES";

    public static final String NO_MODULE_NAME_TIPS = "These no SIMPLE_ROUTER_KEY name, at 'build.gradle', like :\n" +
            "android {\n" +
            "    defaultConfig {\n" +
            "        ...\n" +
            "        javaCompileOptions {\n" +
            "            annotationProcessorOptions {\n" +
            "                arguments = [SIMPLE_ROUTER_KEY: \"1234567890987654\", OPEN_AES: \"true\"]\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "}\n";
}
