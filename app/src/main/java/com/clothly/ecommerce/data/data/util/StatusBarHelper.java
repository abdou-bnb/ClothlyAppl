package com.clothly.ecommerce.data.data.util;

import android.app.Activity;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.Window;
import android.view.WindowManager;

public class StatusBarHelper {
    public static void getStatusBarTransparent(Activity activity, int colorId){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_OVERSCAN);
            window.setStatusBarColor(ContextCompat.getColor(activity, colorId));
        }
    }
}
