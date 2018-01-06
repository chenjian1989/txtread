package com.example.administrator.txtread;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.Util.DownloadUtil;
import com.example.administrator.Util.HttpUtil;
import com.example.administrator.Util.LogUtil;
import com.example.administrator.adapter.SearchAdapter;
import com.example.administrator.application.App;
import com.example.administrator.base.CommonBaseActivity;
import com.example.administrator.dialog.ConfirmDialog;
import com.example.administrator.entity.SearchEntity;
import com.example.administrator.inter.HttpCallback;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends CommonBaseActivity {

    private EditText mEdit_search;

    private ListView mListview;

    private SearchAdapter mSearchAdapter;

    private DownloadUtil mDownloadUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
    }

    private void initView() {

        Button btn = (Button) findViewById(R.id.btn_search);
        mEdit_search = (EditText) findViewById(R.id.edit_search);
        mListview = (ListView) findViewById(R.id.listview_txtlist);

        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                ConfirmDialog confirmDialog = new ConfirmDialog(SearchActivity.this, "确定添加到书架!", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SearchEntity s = (SearchEntity) mSearchAdapter.getItem(i);
                        addShuJia(s.getHomeUrl());
                    }
                });
                confirmDialog.show();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = mEdit_search.getText().toString();
                if (!TextUtils.isEmpty(str)) {
                    SearchXs(str);
                } else {
                    Toast.makeText(SearchActivity.this, "不能为空!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String str = mEdit_search.getText().toString();
                if (!TextUtils.isEmpty(str)) {
                    if(!str.contains("http")){
                        if (str.contains("_")) {
                            str = "https://www.xs.la/" + str + "/";
                        } else {
                            str = "http://www.shuqizw.com/book/" + str + ".html";
                        }
                    }
                    addShuJia(str);
                } else {
                    Toast.makeText(SearchActivity.this, "不能为空!", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        mSearchAdapter = new SearchAdapter(SearchActivity.this);
        mDownloadUtil = new DownloadUtil();
    }

    /**
     * 加入书架 (主线程中调用)
     *
     * @param homeUrl 书本首页
     */
    private void addShuJia(final String homeUrl) {
        List<String> homeList = App.getInstance().getHomeList();
        if (homeList.contains(homeUrl)) {
            App.getInstance().showToast("已经在书架中!");
        } else {
            showSelfDefineDialog(true);
            HttpUtil.httpGetUrl(homeUrl, homeUrl, true, new HttpCallback() {
                @Override
                public void httpSuccess(String data, String url) {
                    LogUtil.e(url + "添加到书架----ok!!");
                    mainToast("成功添加到书架!");
                    App.getInstance().saveHomeList(homeUrl);
                    App.getInstance().setmIsUpdate(true);
                }

                @Override
                public void httpError(String des) {
                    LogUtil.e("SearchActivity-HttpError: " + des);
                    mainToast("加入书架失败!");
                }
            }, false);
        }
    }

    private void mainToast(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                removeSelfDefineDialog();
                Toast.makeText(SearchActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void SearchXs(String data) {
        //http://zhannei.baidu.com/cse/search?s=1393206249994657467&q=%E4%B8%80%E5%BF%B5%E6%B0%B8%E6%81%92
        //http://zhannei.baidu.com/cse/search?s=16829369641378287696&q=
        //http://zhannei.baidu.com/cse/search?q=%D2%BB%C4%EE%D3%C0%BA%E3&s=2950257706739235360&ie=gbk&entry=1
        String url_index = "http://zhannei.baidu.com/cse/search?q=";
        String url_end = "&s=2950257706739235360&ie=gbk&entry=1";
        try {
            showSelfDefineDialog(true);
            String url = url_index + java.net.URLEncoder.encode(data, "gbk") + url_end;
            HttpUtil.httpGetUrl(url, url, true, new HttpCallback() {
                @Override
                public void httpSuccess(String data, String url) {
                    final List<SearchEntity> searchEntities = mDownloadUtil.query(data);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            removeSelfDefineDialog();
                            mSearchAdapter.initData(searchEntities);
                            mListview.setAdapter(mSearchAdapter);
                        }
                    });
                }

                @Override
                public void httpError(String des) {
                    LogUtil.e("MainActivity-HttpError: " + des);
                    mainToast("搜索异常!");
                }
            }, false);
        } catch (UnsupportedEncodingException e) {
            e.getStackTrace();
        }
    }
}
