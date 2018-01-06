package com.example.administrator.Util;


import android.util.Log;

public class LogUtil {

    private static final boolean isLog = true;

    public static void e(String str){
        if(isLog){
            Log.e("txtread", str);
        }
    }
}
