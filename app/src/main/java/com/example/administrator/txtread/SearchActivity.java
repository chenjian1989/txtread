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
                        List<String> homeList = App.getInstance().getHomeList();
                        if (homeList.contains(s.getHomeUrl())) {
                            App.getInstance().showToast("已经在书架中!");
                            return;
                        }
                        final List<String> lists = new ArrayList<>();
                        lists.add(s.getHomeUrl());
                        showSelfDefineDialog(true);
                        HttpUtil.httpGetUrl(s.getHomeUrl(), true, new HttpCallback() {
                            @Override
                            public void httpSuccess(String data, String url) {
                                LogUtil.e(url + "添加到书架----ok!!");
                                mainToast("成功添加到书架!");
                                App.getInstance().saveHomeList(lists);
                                App.getInstance().setmIsUpdate(true);
                            }

                            @Override
                            public void httpError(String des) {
                                LogUtil.e("SearchActivity-HttpError: " + des);
                                mainToast("加入书架失败!");
                            }
                        }, false);
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
                    showSelfDefineDialog(true);
                    SearchXs(str);
                } else {
                    Toast.makeText(SearchActivity.this, "不能为空!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mSearchAdapter = new SearchAdapter(SearchActivity.this);
        mDownloadUtil = new DownloadUtil();
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
        String url_index = "http://zhannei.baidu.com/cse/search?s=16829369641378287696&q=";
        String url_end = "&isNeedCheckDomain=1&jump=1";
        try {
            String url = url_index + java.net.URLEncoder.encode(data, "utf-8") + url_end;
            HttpUtil.httpGetUrl(url, true, new HttpCallback() {
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
