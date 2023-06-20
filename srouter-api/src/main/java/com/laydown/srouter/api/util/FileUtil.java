package com.laydown.srouter.api.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class FileUtil {

    public static String getAssetFile(AssetManager assetManager, String fileName) {
        String result = "";
        try {
            //获取输入流
            InputStream mAssets = assetManager.open(fileName);

            //获取文件的字节数
            int lenght = mAssets.available();
            //创建byte数组
            byte[] buffer = new byte[lenght];
            //将文件中的数据写入到字节数组中
            mAssets.read(buffer);
            mAssets.close();
            result = new String(buffer);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("fuck", "error");
            return result;
        }
    }

    /**
     * @param assetsPath 如果读取assets根目录下的东西 传入""即可
     *                   如果需要读取assets下某个文件夹下的所有文件列表，传文件夹名称 即可
     *                   有多级文件夹，传入各级文件夹名称，使用"/"分隔即可
     * @return 所有文件名
     * @title: getAllAssetsList
     * @return: String[]
     */
    public static String[] getAllAssetsList(Context context, String assetsPath) {
        AssetManager manager = context.getAssets();
        String[] fileNames = null;
        try {
            fileNames = manager.list(assetsPath);
            for (String fileName : fileNames) {
                Log.d("tag", fileName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileNames;
    }

    public static ArrayList<String> filterArray(String[] list, String endpoint) {
        ArrayList<String> arrayList = new ArrayList<>();
        for (String item : list) {
            if (item.endsWith(endpoint)) {
                arrayList.add(item);
            }
        }
        return arrayList;
    }
}
