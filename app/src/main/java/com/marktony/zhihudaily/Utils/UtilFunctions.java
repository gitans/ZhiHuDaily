package com.marktony.zhihudaily.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by lizhaotailang on 2016/4/23.
 */
public class UtilFunctions {

    public static int getThemeState(Context context){
        // 从SharedPreferences读取主题的值，如果为日间主题，返回0，夜间主题返回1
        SharedPreferences sp = context.getSharedPreferences("user_settings",Context.MODE_PRIVATE);
        return sp.getInt("theme",0);
    }

    /**
     * 储存主题对应的int值
     * @param context 上下文
     * @param themeValue 主题对应int值 0为日间主题，1为夜间主题
     */
    public static void setThemeState(Context context,int themeValue){
        SharedPreferences sp = context.getSharedPreferences("user_settings",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("theme",themeValue);
        editor.apply();
    }

}
