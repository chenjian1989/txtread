package com.example.administrator.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.example.administrator.application.App;
import com.example.administrator.inter.HttpCallback;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpUtil {

    public static int mReCount = 0;

    private static int mRetry = 5;

    private static ExecutorService mFixedThreadPool = Executors.newFixedThreadPool(20);

    public static String httpGetUrl(final String path, final String homeUrl, boolean isForce, final HttpCallback
            callback, boolean isNet) {
        if (!isForce) {
            // 先在本地缓存找，如果存在直接返回
            String value = App.getInstance().getData(path);
            if (!TextUtils.isEmpty(value)) {
                return value;
            }
        }
        if (isNet || App.getInstance().isNetworkAvailable()) {
            mFixedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    String str = getStringByBytes(http(path));
                    //将返回的数据保存到本地缓存中
                    if (!TextUtils.isEmpty(str)) {
                        if (App.getInstance().isContainChinese(str)) {
                            App.getInstance().saveData(path, str, homeUrl);
                        }
                        if (callback != null) {
                            callback.httpSuccess(str, path);
                        }
                    } else {
                        if (callback != null) {
                            callback.httpError("未获取到数据!");
                        }
                    }
                }
            });
        } else {
            if (callback != null) {
                callback.httpError("未连接网络!");
            }
        }
        return null;
    }

    public static void httpGetUrl(boolean isForce, String path, String homeUrl, HttpCallback callback, boolean isNet) {
        String str = httpGetUrl(path, homeUrl, isForce, callback, isNet);
        if (str != null) {
            callback.httpSuccess(str, path);
        }
    }

    /**
     * 下载图片，返回图片的本地路径
     *
     * @param path
     * @return
     */
    public static String httpImageUrl(final String path, final HttpCallback callback) {
        final File saveDir = new File(App.getInstance().getcachePath());
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
        // 本地找图片
        final String fileName = App.getInstance().getImageName(path);
        File[] imageList = saveDir.listFiles();
        if (imageList != null && imageList.length > 0) {
            for (int i = 0; i < imageList.length; i++) {
                if (fileName.equals(imageList[i].getName())) {
                    return imageList[i].getPath();
                }
            }
        }
        if (App.getInstance().isNetworkAvailable()) {
            mFixedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    byte[] bytes = http(path);
                    if (bytes != null) {
                        try {
                            //文件保存位置
                            File file = new File(saveDir.getPath() + File.separator + fileName);
                            FileOutputStream fos = new FileOutputStream(file);
                            fos.write(bytes);
//                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                            fos.close();
                            if (callback != null) {
                                callback.httpSuccess(file.getPath(), path);
                            }
                        } catch (Exception e) {
                            e.getStackTrace();
                            if (callback != null) {
                                callback.httpError("图片保存失败!");
                            }
                        }
                    } else {
                        if (callback != null) {
                            callback.httpError("未获取到数据!");
                        }
                    }
                }
            });
        } else {
            if (callback != null) {
                callback.httpError("未连接网络!");
            }
        }
        return null;
    }

    /**
     * 获取网络图片
     *
     * @param imageurl 图片网络地址
     * @return Bitmap 返回位图
     */
    public static Bitmap GetImageInputStream(String imageurl) {
        URL url;
        HttpURLConnection connection;
        Bitmap bitmap = null;
        try {
            url = new URL(imageurl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(6000); //超时设置
            connection.setDoInput(true);
            connection.setUseCaches(false); //设置不使用缓存
            InputStream inputStream = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private static byte[] http(String path) {
        path = path.replace("www.biquge.tw","www.xs.la").replace("http://www","https://www");
        mReCount++;
        HttpURLConnection conn = null;
        InputStream is = null;
        try {
            URL url = new URL(path);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(3000);
            conn.setRequestMethod("GET");
            //调用conn.setDoOutput()方法以显式开启请求体
            //conn.setDoOutput(true);
            //conn.setUseCaches(false);
            //在对各种参数配置完成后，通过调用connect方法建立TCP连接，但是并未真正获取数据
            //conn.connect()方法不必显式调用，当调用conn.getInputStream()方法时内部也会自动调用connect方法
            conn.connect();
            //调用getInputStream方法后，服务端才会收到请求，并阻塞式地接收服务端返回的数据
            is = conn.getInputStream();
            //将InputStream转换成byte数组,getBytesByInputStream会关闭输入流
            byte[] bytes = getBytesByInputStream(is);
            mReCount = 0;
            return bytes;
        } catch (Exception e) {
            e.getStackTrace();
            if (conn != null) {
                conn.disconnect();
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ee) {
                    ee.printStackTrace();
                }
            }
            LogUtil.e("HttpUtil--http() ERROR: " + e.getMessage());
            if (mReCount <= mRetry) {
                LogUtil.e("HttpUtil--http() 重试连接:  " + path + "   重试次数: " + mReCount);
                return http(path);
            }
        }
        return null;
    }

    //根据字节数组构建UTF-8字符串
    private static String getStringByBytes(byte[] bytes) {
        if (bytes == null) {
            return "";
        }
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
        int length;
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
