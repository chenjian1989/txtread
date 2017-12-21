package com.example.administrator.application;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

import com.example.administrator.Util.DisplayUtil;
import com.example.administrator.db.DBHelper;
import com.example.administrator.db.DBManager;
import com.example.administrator.db.MobileColumn;
import com.example.administrator.inter.battery;
import com.example.administrator.receiver.BatteryReceiver;
import com.example.administrator.txtread.TxtDetailActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App extends Application {
    private static App instance;

    public static App getInstance() {
        return instance;
    }

    public int mScreenWidth;
    public int mScreenHeight;

    public final String BIQUGE = "biquge.tw";

    private String mFilePath = null;

    private String mBatteryValue = "";

    private boolean mIsUpdate = false;

    private DBManager mDBManager;

    private BatteryReceiver mBatteryReceiver;

    private Handler mHandler = new Handler();

    private Toast mToast;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mDBManager = new DBManager(getApplicationContext());
        //注册广播接受者java代码
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        //创建广播接受者对象
        mBatteryReceiver = new BatteryReceiver();
        //注册receiver
        registerReceiver(mBatteryReceiver, intentFilter);
    }

    public void registerReceiver(battery b) {
        mBatteryReceiver.registerReceiver(b);
    }

    /**
     * 显示提示信息
     */
    public void showToast(final String msg) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mToast != null) {
                    mToast.cancel();
                }
                mToast = Toast.makeText(instance, msg, Toast.LENGTH_SHORT);
                mToast.show();
            }
        });
    }

    public void InitWidthAndHeigh(int with, int height) {
        mScreenWidth = with - DisplayUtil.sp2px(this, 28) - 20;
        mScreenHeight = height - DisplayUtil.sp2px(this, 28) - 20;
    }

    public String substringnew(String str, String startData, String endData) {
        int index = str.indexOf(startData);
        if(index >= 0){
            str = str.substring(str.indexOf(startData) + startData.length());
            int end = str.indexOf(endData);
            if(end >= 0){
                return str.substring(0, str.indexOf(endData));
            }
        }
        return str;
    }

    public String substringnew(String str, int index){
        if(index >= 0){
            return str.substring(index);
        }
        return str;
    }

    public String substringnew(String str, int index, int end){
        if(index >= 0 && end >= 0){
            return str.substring(index, end);
        }
        return str;
    }

    public void savetag(String homeUrl, String url, int index) {
        //存入数据
        ContentValues values = new ContentValues();
        values.put(MobileColumn.SHUJIA_URL, homeUrl);
        if (!TextUtils.isEmpty(url)) {
            values.put(MobileColumn.SHUJIA_LASTURL, url);
        }
        if (index >= 0) {
            values.put(MobileColumn.SHUJIA_ZHANGJIE, index);
        }
        Cursor c = mDBManager.query(DBHelper.SHUJIA_TABLE, null, MobileColumn.SHUJIA_URL + "=?"
                , new String[]{homeUrl}, null, null, null);
        if (c.getCount() > 0) {
            mDBManager.update(DBHelper.SHUJIA_TABLE, values, MobileColumn.SHUJIA_URL + "=?"
                    , new String[]{homeUrl});
        } else {
            mDBManager.insert(DBHelper.SHUJIA_TABLE, values);
        }
        c.close();
    }

    public String[] gettag(String homeUrl) {
        String[] str = new String[2];
        Cursor c = mDBManager.query(DBHelper.SHUJIA_TABLE, null, MobileColumn.SHUJIA_URL + "=?"
                , new String[]{homeUrl}, null, null, null);
        while (c.moveToNext()) {
            str[0] = c.getString(c.getColumnIndex(MobileColumn.SHUJIA_LASTURL));
            str[1] = c.getString(c.getColumnIndex(MobileColumn.SHUJIA_ZHANGJIE));
        }
        c.close();
        if (!TextUtils.isEmpty(str[0]) && !TextUtils.isEmpty(str[1])) {
            return str;
        } else {
            return null;
        }
    }

    public void saveHomeList(List<String> homelists) {
        if (homelists != null && homelists.size() > 0) {
            for (String homeurl : homelists) {
                savetag(homeurl, null, -1);
            }
        }
    }

    public void saveHomeList(String homeurl){
        savetag(homeurl, null, -1);
    }

    public void deleteHomeList(String url){
        mDBManager.delete(DBHelper.SHUJIA_TABLE, MobileColumn.SHUJIA_URL + "=?", new String[]{url});
        mDBManager.delete(DBHelper.DATA_TABLE, MobileColumn.DATA_HOMEURL + "=?", new String[]{url});
    }

    public List<String> getHomeList() {
        List<String> list = new ArrayList<>();
        Cursor c = mDBManager.query(DBHelper.SHUJIA_TABLE, new String[]{MobileColumn.SHUJIA_URL}, null
                , null, null, null, null);
        while (c.moveToNext()) {
            String str = c.getString(c.getColumnIndex(MobileColumn.SHUJIA_URL));
            if (!TextUtils.isEmpty(str)) {
                list.add(str);
            }
        }
        c.close();
        return list;
    }

    public void saveData(String key, String value, String homeUrl) {
        //存入数据
        ContentValues values = new ContentValues();
        values.put(MobileColumn.DATA_URL, key);
        values.put(MobileColumn.DATA_VALUE, value);
        values.put(MobileColumn.DATA_HOMEURL, homeUrl);
        Cursor c = mDBManager.query(DBHelper.DATA_TABLE, null, MobileColumn.DATA_URL + "=?"
                , new String[]{key}, null, null, null);
        if (c.getCount() > 0) {
            mDBManager.update(DBHelper.DATA_TABLE, values, MobileColumn.DATA_URL + "=?"
                    , new String[]{key});
        } else {
            mDBManager.insert(DBHelper.DATA_TABLE, values);
        }
        c.close();
    }

    public String getData(String key) {
        Cursor c = mDBManager.query(DBHelper.DATA_TABLE, null, MobileColumn.DATA_URL + "=?"
                , new String[]{key}, null, null, null);
        String str = null;
        while (c.moveToNext()) {
            str = c.getString(c.getColumnIndex(MobileColumn.DATA_VALUE));
        }
        c.close();
        return str;
    }

    /**
     * 返回手机存储目录的位置
     * <p>
     * 如果有sd卡，返回sd卡的根目录，否则返回手机的缓存目录
     *
     * @return String
     */
    public String getFilePath() {
        if (mFilePath != null) {
            return mFilePath;
        } else {
            if (existSDCard()) {
                mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
                        + getPackageName() + File.separator;
                return mFilePath;
            } else {
                mFilePath = getCacheDir().getAbsolutePath() + File.separator + getPackageName() + File.separator;
                return mFilePath;
            }
        }
    }

    public String getcachePath() {
        return getFilePath() + "cache" + File.separator;
    }

    /**
     * 返回是否存在sd卡.
     *
     * @return
     */
    public boolean existSDCard() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return true;
        } else
            return false;
    }

    public String getImageName(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    /**
     * 获取当前的网络连接状态
     *
     * @return
     */
    public boolean isNetworkConnected() {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable();
        }
        return false;
    }

    /**
     * 检查网络是否好用.
     *
     * @return true or false
     */
    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            // 如果仅仅是用来判断网络连接　　　　　　
            // 则可以使用 cm.getActiveNetworkInfo().isAvailable();
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo workinfo : info) {
                    if (workinfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 判断WIFI网络是否可用
     *
     * @return
     */
    public boolean isWifiConnected() {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (mWiFiNetworkInfo != null) {
            return mWiFiNetworkInfo.isAvailable();
        }
        return false;
    }

    /**
     * 判断MOBILE网络是否可用
     *
     * @return
     */
    public boolean isMobileConnected() {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mMobileNetworkInfo = mConnectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mMobileNetworkInfo != null) {
            return mMobileNetworkInfo.isAvailable();
        }
        return false;
    }

    /**
     * 判断网络类型
     *
     * @return 返回值 -1：没有网络  1：WIFI网络2：wap网络3：net网络
     */
    public int GetNetype() {
        int netType = -1;
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet")) {
                netType = 3;
            } else {
                netType = 2;
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = 1;
        }
        return netType;
    }

    /**
     * 判断字符串中是否包含了中文
     * @param str
     * @return
     */
    public boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    public String getDate(String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date curDate = new Date(System.currentTimeMillis());
        //获取当前时间
        return formatter.format(curDate);
    }

    public String getmBatteryValue() {
        return mBatteryValue;
    }

    public void setmBatteryValue(String mBatteryValue) {
        this.mBatteryValue = mBatteryValue;
    }

    public boolean ismIsUpdate() {
        return mIsUpdate;
    }

    public void setmIsUpdate(boolean mIsUpdate) {
        this.mIsUpdate = mIsUpdate;
    }

    public int getColorByResid(int id){
        return getResources().getColor(id);
    }
}
