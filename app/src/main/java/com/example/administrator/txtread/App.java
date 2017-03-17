package com.example.administrator.txtread;

import android.app.Application;
import android.content.SharedPreferences;

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

    public void savetag(String url, int index){
        //获取SharedPreferences对象
        SharedPreferences sp = getApplicationContext().getSharedPreferences("SP", MODE_PRIVATE);
        //存入数据
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("STRING_KEY", url + ";" + index);
        editor.commit();
    }

    public String[] gettag(){
        SharedPreferences sp = getApplicationContext().getSharedPreferences("SP", MODE_PRIVATE);
        String str = sp.getString("STRING_KEY", "");
        String[] strs = str.split(";");
        return strs;
    }

}
