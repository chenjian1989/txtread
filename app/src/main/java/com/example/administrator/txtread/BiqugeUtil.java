package com.example.administrator.txtread;

import android.text.TextUtils;
import android.util.Log;

public class BiqugeUtil {

    private String mUrl;

    public BiqugeUtil(String url) {
        mUrl = url;
    }

    /**
     * 必须在线程中调用
     */
    public void getHttp(final callback c) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                long a = System.currentTimeMillis();
                String str = HttpUtil.httpGetUrl(mUrl);
                if (!TextUtils.isEmpty(str)) {
                    try {
                        int index = str.indexOf("<div id=\"content\">");
                        str = str.substring(index + 18);
                        int end = str.indexOf("</div>");
                        str = str.substring(0, end);
                        int i = str.indexOf("<script");
                        str = str.substring(0, i);
                        str = str.replace("<br/>", "\n").replace("&lt;", "").replace("&gt;", "").replace("&nbsp;", "");
                        TxtUtil.setData(str, mUrl);
                        c.loadsuccess();
                        //Log.e("txtread", "转码后的str: " + str);
                    } catch (Exception e) {
                        e.getStackTrace();
                        c.loaderror();
                        Log.e("txtread", "BiqugeUtil--getHttp() ERROR: " + e.getMessage());
                    }
                } else {
                    c.loaderror();
                }
                long b = System.currentTimeMillis() - a;
                Log.e("txtread", mUrl + "请求耗时: " + b);
            }
        }).start();
    }

    public void nextpage(callback c) {
        try {
            int index = mUrl.lastIndexOf("/");
            String str = mUrl.substring(index + 1, mUrl.length() - 5);
            int z = Integer.parseInt(str) + 1;
            str = mUrl.substring(0, index + 1);
            str = str + z + ".html";
            mUrl = str;
            //Log.e("txtread", "nextpage-str:  " + str);
            getHttp(c);
        } catch (Exception e){
            e.getStackTrace();
            c.loaderror();
            Log.e("txtread", "BiqugeUtil--nextpage() ERROR: " + e.getMessage());
        }
    }

    public void prevpage(callback c){
        try {
            int index = mUrl.lastIndexOf("/");
            String str = mUrl.substring(index + 1, mUrl.length() - 5);
            int z = Integer.parseInt(str) - 1;
            str = mUrl.substring(0, index + 1);
            str = str + z + ".html";
            mUrl = str;
            //Log.e("txtread", "prevpage-str:  " + str);
            getHttp(c);
        } catch (Exception e){
            e.getStackTrace();
            c.loaderror();
            Log.e("txtread", "BiqugeUtil--prevpage() ERROR: " + e.getMessage());
        }
    }

}
