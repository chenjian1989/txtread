package com.example.administrator.Util;

import android.text.TextUtils;

import com.example.administrator.entity.HomeTxtEntity;
import com.example.administrator.entity.SearchEntity;
import com.example.administrator.entity.TxtEntity;
import com.example.administrator.inter.DownCallback;
import com.example.administrator.inter.HttpCallback;
import com.example.administrator.inter.Ihtml;
import com.example.administrator.inter.LocalCallBack;
import com.example.administrator.inter.callback;
import com.example.administrator.application.App;

import java.util.ArrayList;
import java.util.List;

public class DownloadUtil {

    private String mUrl;

    private String mHomeUrl;

    private ArrayList<String> mChapters;

    private String mPager_prev;

    private String mPager_next;

    private Ihtml mHtml;

    private int mTotal = 5;

    public DownloadUtil() {
    }

    public void init(String url, String homeurl, ArrayList<String> chapters) {
        if(!TextUtils.isEmpty(url)){
            mUrl = url;
            mHtml = InitHtml(url);
        }
        mHomeUrl = homeurl;
        mChapters = chapters;
    }

    public void setChapters(ArrayList<String> chapters){
        mChapters = chapters;
    }

    public void setUrl(String url) {
        mUrl = url;
        mHtml = InitHtml(url);
    }

    /**
     * 初始化解析工具类
     *
     * @param url
     * @return
     */
    private Ihtml InitHtml(String url) {
        Ihtml html = new BiqugeUtil();
        if (url.contains(App.getInstance().XS_LA) || url.contains(App.getInstance().BIQUGE)) {
            html = new BiqugeUtil();
        } else if(url.contains(App.getInstance().SHUQI)){
            html = new ShuqizwUtil();
        }
        return html;
    }

    public void queryLocaData(final LocalCallBack c, final String homeurl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<HomeTxtEntity> lists = new ArrayList<>();
                Ihtml html_home = InitHtml(homeurl);
                String value = App.getInstance().getData(homeurl);
                if (!TextUtils.isEmpty(value)) {
                    long a = System.currentTimeMillis();
                    HomeTxtEntity homeTxtEntity = html_home.jiexihome(value, homeurl, true);
                    lists.add(homeTxtEntity);
                    long b = System.currentTimeMillis() - a;
                    LogUtil.e("jiexihome 耗时： " + b);
                }
                c.LocalSuccess(lists);
            }
        }).start();
    }

    public void queryLocalData(final LocalCallBack c, final boolean isZhangjie) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<HomeTxtEntity> lists = new ArrayList<>();
                List<String> homeList = App.getInstance().getHomeList();
                if (homeList != null && homeList.size() > 0) {
                    Ihtml html_home;
                    for (String url : homeList) {
                        html_home = InitHtml(url);
                        String value = App.getInstance().getData(url);
                        if (!TextUtils.isEmpty(value)) {
                            HomeTxtEntity homeTxtEntity = html_home.jiexihome(value, url, isZhangjie);
                            lists.add(homeTxtEntity);
                        }
                    }
                }
                c.LocalSuccess(lists);
            }
        }).start();
    }

    public List<SearchEntity> query(String data) {
        BiqugeUtil biqugeUtil = new BiqugeUtil();
        return biqugeUtil.jiexiSearch(data);
    }

    /**
     * 获取数据并解析
     *
     * @param c
     */
    public void DownloadData(final callback c, final boolean isForce) {
        if (mHtml == null && TextUtils.isEmpty(mUrl)) {
            c.loaderror("地址为空或地址无法解析!");
            return;
        }

        final long a = System.currentTimeMillis();
        HttpUtil.httpGetUrl(isForce, mUrl, mHomeUrl, new HttpCallback() {
            @Override
            public void httpSuccess(String data, String url) {
                if(url.contains(App.getInstance().SHUQI)){
                    String url2 = mUrl.replace(".html","_2.html");
                    // 解析章节数据
                    TxtEntity txtEntity = mHtml.jiexishuju(data, mUrl);
                    if (TextUtils.isEmpty(txtEntity.getError())) {
                        TxtUtil.setmTitle(txtEntity.getTitle());
                        final String str1 = txtEntity.getData();
                        HttpUtil.httpGetUrl(isForce, url2, mHomeUrl, new HttpCallback() {
                            @Override
                            public void httpSuccess(String data, String url) {
                                String url3 = mUrl.replace(".html","_3.html");
                                TxtEntity txtEntity = mHtml.jiexishuju(data, mUrl);
                                if (TextUtils.isEmpty(txtEntity.getError())) {
                                    final String str2 = txtEntity.getData();
                                    HttpUtil.httpGetUrl(isForce, url3, mHomeUrl, new HttpCallback() {
                                        @Override
                                        public void httpSuccess(String data, String url) {
                                            TxtEntity txtEntity = mHtml.jiexishuju(data, mUrl);
                                            if (TextUtils.isEmpty(txtEntity.getError())) {
                                                final String str3 = txtEntity.getData();
                                                TxtUtil.setData(str1 + str2 + str3, mUrl, mHomeUrl);
                                                prestrain(mTotal, null);
                                                haoshi(mUrl, a);
                                                c.loadsuccess(mUrl);
                                            }
                                        }

                                        @Override
                                        public void httpError(String des) {
                                            c.loaderror(des);
                                        }
                                    }, false);
                                }
                            }
                            @Override
                            public void httpError(String des) {
                                c.loaderror(des);
                            }
                        }, false);
                    } else {
                        c.loaderror(txtEntity.getError());
                    }
                } else {
                    jiexi(c, data);
                    // 当前显示的章节加载完成后，后台进行预加载
                    prestrain(mTotal, null);
                    haoshi(mUrl, a);
                }
            }
            @Override
            public void httpError(String des) {
                c.loaderror(des);
            }
        }, false);
    }

    private int ii = 0;

    private void haoshi(String url, long a) {
        long b = System.currentTimeMillis() - a;
        ii++;
        LogUtil.e(url + "--请求耗时: " + b + "    " + ii);
    }

    private void jiexi(callback c, String str) {
        // 解析章节数据
        TxtEntity txtEntity = mHtml.jiexishuju(str, mUrl);
        if (TextUtils.isEmpty(txtEntity.getError())) {
            TxtUtil.setmTitle(txtEntity.getTitle());
            TxtUtil.setData(txtEntity.getData(), mUrl, mHomeUrl);
            mPager_next = txtEntity.getNext();
            mPager_prev = txtEntity.getPrev();
            c.loadsuccess(mUrl);
        } else {
            c.loaderror(txtEntity.getError());
        }
    }

    private String getNextpageUrl() {
        if (mChapters != null && mChapters.size() > 0) {
            for (int i = 0; i < mChapters.size(); i++) {
                String url = mChapters.get(i).split(";")[0];
                if (url.equals(mUrl)) {
                    if (i + 1 < mChapters.size()) {
                        return mChapters.get(i + 1).split(";")[0];
                    }
                    break;
                }
            }
        } else {
            return mPager_next;
        }
        return null;
    }

    private String getPrevpageUrl() {
        if (mChapters != null && mChapters.size() > 0) {
            for (int i = 0; i < mChapters.size(); i++) {
                String url = mChapters.get(i).split(";")[0];
                if (url.equals(mUrl)) {
                    if (i - 1 >= 0) {
                        return mChapters.get(i - 1).split(";")[0];
                    }
                    break;
                }
            }
        } else {
            return mPager_prev;
        }
        return null;
    }

    /**
     * 获取前count章的路径地址
     *
     * @param count
     * @return
     */
    private List<String> getPrevpageUrl(int count) {
        List<String> prevs = new ArrayList<>();
        if (mChapters != null && mChapters.size() > 0) {
            for (int i = 0; i < mChapters.size(); i++) {
                String url = mChapters.get(i).split(";")[0];
                if (url.equals(mUrl)) {
                    for (int j = 1; j <= count; j++) {
                        if (i - j >= 0) {
                            prevs.add(mChapters.get(i - j).split(";")[0]);
                        } else {
                            break;
                        }
                    }
                    break;
                }
            }
        }
        return prevs;
    }

    /**
     * 获取后count章的路径地址
     *
     * @param count
     * @return
     */
    private List<String> getNextpageUrl(int count) {
        List<String> nexts = new ArrayList<>();
        if (mChapters != null && mChapters.size() > 0) {
            for (int i = 0; i < mChapters.size(); i++) {
                String url = mChapters.get(i).split(";")[0];
                if (url.equals(mUrl)) {
                    for (int j = 1; j <= count; j++) {
                        if (i + j < mChapters.size()) {
                            nexts.add(mChapters.get(i + j).split(";")[0]);
                        } else {
                            break;
                        }
                    }
                    break;
                }
            }
        }
        return nexts;
    }

    public void nextpage(callback c) {
        try {
            mPager_next = getNextpageUrl();
            if (mPager_next != null) {
                //LogUtil.e("nextpage-str:  " + mPager_next);
                mUrl = mPager_next;
                DownloadData(c, false);
            } else {
                // 没有下一章了
                c.loaderror("已经是最后一章了!");
            }
        } catch (Exception e) {
            e.getStackTrace();
            c.loaderror(e.getMessage());
            LogUtil.e("BiqugeUtil--nextpage() ERROR: " + e.getMessage());
        }
    }

    public void prevpage(callback c) {
        try {
            mPager_prev = getPrevpageUrl();
            if (mPager_prev != null) {
                mUrl = mPager_prev;
                //LogUtil.e("prevpage-str:  " + mUrl);
                DownloadData(c, false);
            } else {
                // 已经是第一章了
                c.loaderror("已经是第一章了!");
            }
        } catch (Exception e) {
            e.getStackTrace();
            c.loaderror(e.getMessage());
            LogUtil.e("BiqugeUtil--prevpage() ERROR: " + e.getMessage());
        }
    }

    /**
     * 预加载或后台下载缓存到本地
     */
    private void prestrain(List<String> nexts, final DownCallback callback) {
        if (!App.getInstance().isNetworkAvailable()) {
            LogUtil.e("DownloadUtil--prestrain--当前未连接网络!");
            return;
        }
        final List<String> temp = new ArrayList<>();
        temp.addAll(nexts);
        final int end = nexts.size();
        for (final String next_url : nexts) {
            if (!TextUtils.isEmpty(next_url)) {
                final long a = System.currentTimeMillis();
                if(next_url.contains(App.getInstance().SHUQI)){
                    HttpUtil.httpGetUrl(false, next_url, mHomeUrl, new HttpCallback() {
                        @Override
                        public void httpSuccess(String data, String url) {
                            String url2 = next_url.replace(".html","_2.html");
                            HttpUtil.httpGetUrl(false, url2, mHomeUrl, new HttpCallback() {
                                @Override
                                public void httpSuccess(String data, String url) {
                                    String url3 = next_url.replace(".html","_3.html");
                                    HttpUtil.httpGetUrl(false, url3, mHomeUrl, new HttpCallback() {
                                        @Override
                                        public void httpSuccess(String data, String url) {
                                            haoshi(next_url, a);
                                            temp.remove(next_url);
                                            if (callback != null) {
                                                callback.donestate(end - temp.size(), end);
                                            }
                                        }

                                        @Override
                                        public void httpError(String des) {
                                            LogUtil.e("DownloadUtil-prestrain-HttpError: " + des);
                                        }
                                    }, true);
                                }

                                @Override
                                public void httpError(String des) {
                                    LogUtil.e("DownloadUtil-prestrain-HttpError: " + des);
                                }
                            }, true);
                        }

                        @Override
                        public void httpError(String des) {
                            LogUtil.e("DownloadUtil-prestrain-HttpError: " + des);
                        }
                    }, true);
                } else {
                    HttpUtil.httpGetUrl(false, next_url, mHomeUrl, new HttpCallback() {
                        @Override
                        public void httpSuccess(String data, String url) {
                            haoshi(url, a);
                            temp.remove(url);
                            if (callback != null) {
                                callback.donestate(end - temp.size(), end);
                            }
                        }

                        @Override
                        public void httpError(String des) {
                            LogUtil.e("DownloadUtil-prestrain-HttpError: " + des);
                        }
                    }, true);
                }
            }
        }
    }

    private void prestrain(final int Count, final DownCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                prestrain(getNextpageUrl(Count), callback);
            }
        }).start();
    }

    /**
     * 缓存当前章节以后的所有数据
     */
    public void DownFuture(int count) {
        if(App.getInstance().isNetworkAvailable()){
            prestrain(count, new DownCallback() {
                @Override
                public void donestate(int index, int end) {
                    if (index == end) {
                        LogUtil.e("预加载数据完成!");
                        App.getInstance().showToast("缓存下载完成!");
                    }
                }
            });
        } else {
            App.getInstance().showToast("当前未连接网络!");
        }
    }
}
