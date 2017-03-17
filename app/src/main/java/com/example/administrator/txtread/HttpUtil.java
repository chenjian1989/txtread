package com.example.administrator.txtread;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {

    public static int mReCount = 0;

    public static String httpGetUrl(String path){
        mReCount ++;
        HttpURLConnection conn = null;
        InputStream is = null;
        try {
            URL url = new URL(path);
            conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(3000);
            conn.setRequestMethod("GET");
            //调用conn.setDoOutput()方法以显式开启请求体
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            //在对各种参数配置完成后，通过调用connect方法建立TCP连接，但是并未真正获取数据
            //conn.connect()方法不必显式调用，当调用conn.getInputStream()方法时内部也会自动调用connect方法
            conn.connect();
            //调用getInputStream方法后，服务端才会收到请求，并阻塞式地接收服务端返回的数据
            is = conn.getInputStream();
            //将InputStream转换成byte数组,getBytesByInputStream会关闭输入流
            String str = getStringByBytes(getBytesByInputStream(is));
            mReCount = 0;
            return str;
        } catch (Exception e){
            e.getStackTrace();
            if(conn != null){
                conn.disconnect();
            }
            if(is != null){
                try {
                    is.close();
                } catch (IOException ee) {
                    ee.printStackTrace();
                }
            }
            Log.e("txtread", "HttpUtil--httpGetUrl() ERROR: " + e.getMessage());
            if(mReCount <= 3){
                Log.e("txtread", "HttpUtil--httpGetUrl() 重试连接:  " + path + "   重试次数: " + mReCount);
                return httpGetUrl(path);
            }
        }
        return "";
    }

    //根据字节数组构建UTF-8字符串
    private static String getStringByBytes(byte[] bytes) {
        String str = "";
        try {
            str = new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    //从InputStream中读取数据，转换成byte数组，最后关闭InputStream
    private static byte[] getBytesByInputStream(InputStream is) {
        byte[] bytes = null;
        BufferedInputStream bis = new BufferedInputStream(is);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedOutputStream bos = new BufferedOutputStream(baos);
        byte[] buffer = new byte[1024 * 8];
        int length = 0;
        try {
            while ((length = bis.read(buffer)) > 0) {
                bos.write(buffer, 0, length);
            }
            bos.flush();
            bytes = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bytes;
    }
}
