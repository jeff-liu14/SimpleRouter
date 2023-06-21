package com.laydown.srouter.tool;

import android.content.Intent;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Helper {
    public interface CallBack {
        void onActivityResult(@Nullable ActivityResult result);
    }

    public static ActivityResultLauncher<Intent> startActivityForResult(@NonNull ComponentActivity activity, @NonNull Intent simpleRouterBuildIntent, @Nullable CallBack callBack) {
        return activity.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (callBack != null) {
                    callBack.onActivityResult(result);
                }
            }
        });
    }
}
