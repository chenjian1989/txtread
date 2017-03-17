package com.example.administrator.txtread;

import android.app.Application;

public class App extends Application {
    private static App instance;

    public static App getInstance() {
        return instance;
    }

    public int mScreenWidth;
    public int mScreenHeight;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public void InitWidthAndHeigh(int with, int height){
        mScreenWidth = with - 20;
        mScreenHeight = height - 20;
//        mScreenWidth = with - DisplayUtil.dip2px(this, 20) - 20;
//        mScreenHeight = height - DisplayUtil.dip2px(this, 20) - 20;
    }

}
