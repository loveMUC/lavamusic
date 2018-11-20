package com.example.lavamusic;
/*
@author 石同尘 中央民族大学 计算机科学与技术
获取全局上下文
 */
import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext=getApplicationContext();
    }
    public static Context getmContext(){
        return mContext;
    }
}
