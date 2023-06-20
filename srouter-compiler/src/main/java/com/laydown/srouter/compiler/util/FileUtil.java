package com.laydown.srouter.compiler.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

import static com.laydown.srouter.compiler.util.Const.NO_MODULE_NAME_TIPS;
import static com.laydown.srouter.compiler.util.Const.OUTPUT_FILE_NAME;
import static com.laydown.srouter.compiler.util.Const.OUTPUT_FILE_NAME_FIX;

public class FileUtil {

    public static void saveIntoAsset(Filer filer, Messager messager, boolean openAes, String simpleRouterKey, HashMap<String, JSONObject> destMap) {
        try {
            FileObject resource = filer.createResource(StandardLocation.CLASS_OUTPUT, "", OUTPUT_FILE_NAME);
            String resourcePath = resource.toUri().getPath();
            messager.printMessage(Diagnostic.Kind.NOTE, "resourcePath:\n" + resourcePath + "\n");
            String appPath = resourcePath.substring(0, resourcePath.indexOf("build"));
            messager.printMessage(Diagnostic.Kind.NOTE, "resourcePath-appPath:\n" + appPath + "\n");
            String assetsPath = appPath + "src/main/assets";

            String tmpStr = appPath.substring(0, appPath.length() - 1);


            String[] tmpStrList = appPath.substring(0, appPath.length() - 1).split("\\/");
            String last = tmpStrList[tmpStrList.length - 1];
            messager.printMessage(Diagnostic.Kind.NOTE, "resourcePath-tmpStr:\n" + tmpStr + "\n" + last + "\n");
            messager.printMessage(Diagnostic.Kind.NOTE, "resourcePath-assetsPath:\n" + assetsPath + "\n");

            File file = new File(assetsPath);
            if (!file.exists()) {
                boolean succ = file.mkdirs();
                messager.printMessage(Diagnostic.Kind.NOTE, "resourcePath-assetsPath-mkdirs:\n" + succ + "\n");
            }
            String content = "";
            if (openAes) {
                messager.printMessage(Diagnostic.Kind.NOTE, "openAes ---> " + openAes + " \n ");
                try {
                    if (StringUtils.isNotEmpty(simpleRouterKey)) {
                        messager.printMessage(Diagnostic.Kind.NOTE, simpleRouterKey);
                        String destStr = JSON.toJSONString(destMap);
                        messager.printMessage(Diagnostic.Kind.NOTE, "moduleName:destStr -> \n " + destStr + "\n");
                        content = AesHelper.encrypt(destStr, simpleRouterKey);
                        messager.printMessage(Diagnostic.Kind.NOTE, "moduleName:AES encrypt content -> \n " + content + "\n");
                        String decryptContent = AesHelper.decrypt(content, simpleRouterKey);
                        messager.printMessage(Diagnostic.Kind.NOTE, "moduleName:AES decrypt content -> \n " + decryptContent + "\n");

                    } else {
                        messager.printMessage(Diagnostic.Kind.ERROR, NO_MODULE_NAME_TIPS);
                    }
                } catch (Exception ex) {
                    messager.printMessage(Diagnostic.Kind.ERROR, "路由加密失败");
                }

            } else {
                content = JSON.toJSONString(destMap);
                messager.printMessage(Diagnostic.Kind.NOTE, "openAes ---> " + openAes + " \n ");
            }

            if (StringUtils.isNotEmpty(content)) {
                messager.printMessage(Diagnostic.Kind.NOTE, content);
                String fileName = last + OUTPUT_FILE_NAME;
                File outputFile = new File(assetsPath, Base64Utils.encodeToString(fileName) + OUTPUT_FILE_NAME_FIX);
                if (!outputFile.exists()) {
                    outputFile.createNewFile();
                }
                FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
                OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream);
                writer.write(content);
                writer.flush();
                fileOutputStream.close();
                writer.close();
                messager.printMessage(Diagnostic.Kind.NOTE, outputFile.getAbsolutePath());
            } else {
                messager.printMessage(Diagnostic.Kind.ERROR, "路由文件加密失败");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
