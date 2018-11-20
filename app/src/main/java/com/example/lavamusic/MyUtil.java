package com.example.lavamusic;

import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

/*
@author 石同尘 中央民族大学 计算机科学与技术
 */
public class MyUtil {
    //SDK版本大于4.4，设置状态栏透明化，导航栏透明化，填满屏幕
    public static void fillScreen(AppCompatActivity activity){
        if(activity.getSupportActionBar()!=null)
            activity.getSupportActionBar().hide();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }
    //获取上下文
    public static Context getContext(){
        return MyApplication.getmContext();
    }
    @ColorInt
    public static int getColor(@ColorRes int id){
        int color=ContextCompat.getColor(getContext(),id);
        return color;
    }
}
