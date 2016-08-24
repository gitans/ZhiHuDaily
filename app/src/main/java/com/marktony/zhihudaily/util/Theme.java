package com.marktony.zhihudaily.util;

import android.app.Activity;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import com.marktony.zhihudaily.R;
import com.marktony.zhihudaily.app.App;

/**
 * Created by Lizhaotailang on 2016/8/23.
 */

public class Theme {

    //对应attrs.xml文件的MyTheme
    public static final int DAY_THEME = 0;
    public static final int NIGHT_THEME = 1;

    //对应Style
    public static final int RESOURCES_DAY_THEME = R.style.DayTheme;
    public static final int RESOURCES_NIGHT_THEME = R.style.NightTheme;

    public static void setStatusBarColor(Activity activity) {

        // change the status bar's color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Window window = activity.getWindow();
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            // finally change the color
            if (App.getThemeValue() == DAY_THEME){
                window.setStatusBarColor(activity.getResources().getColor(R.color.colorStatusBarLight));
            } else {
                window.setStatusBarColor(activity.getResources().getColor(R.color.colorStatusBarDark));
            }

        }
    }

}
