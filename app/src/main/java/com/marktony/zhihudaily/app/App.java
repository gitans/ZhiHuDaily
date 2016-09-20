package com.marktony.zhihudaily.app;

import android.app.Application;
import android.content.SharedPreferences;

import com.marktony.zhihudaily.util.Theme;

/**
 * Created by Lizhaotailang on 2016/8/23.
 */

public class App extends Application {

    private static int MyTheme = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        getLocalData();
    }

    private void getLocalData() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_settings",MODE_PRIVATE);
        MyTheme = sharedPreferences.getInt("theme",0);

    }
    public static int getThemeValue(){
        return MyTheme;
    }

    public static void setThemeValue(int themeValue){
        MyTheme = themeValue;
    }

    public static int getThemeResources(){
        switch (MyTheme){
            case Theme.DAY_THEME:
                return Theme.RESOURCES_DAY_THEME;
            case Theme.NIGHT_THEME:
                return Theme.RESOURCES_NIGHT_THEME;
            default:
                return Theme.RESOURCES_DAY_THEME;
        }
    }

}
