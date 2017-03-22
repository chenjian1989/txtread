package com.example.administrator.txtread;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.Util.DownloadUtil;
import com.example.administrator.Util.HttpCallback;
import com.example.administrator.Util.HttpUtil;
import com.example.administrator.Util.LogUtil;
import com.example.administrator.adapter.SearchAdapter;
import com.example.administrator.dialog.LoadingDialog;
import com.example.administrator.entity.SearchEntity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends Activity {

    private EditText mEdit_search;

    private ListView mListview;

    private SearchAdapter mSearchAdapter;

    private DownloadUtil mDownloadUtil;

    private LoadingDialog mLoadingDialog;

    private Handler mHandler = new Handler();

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
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final List<String> lists = new ArrayList<>();
                SearchEntity s = (SearchEntity) mSearchAdapter.getItem(i);
                lists.add(s.getHomeUrl());
                for (String path : lists) {
                    mLoadingDialog.showDialog();
                    // 强制刷新列表数据
                    HttpUtil.httpGetUrl(path, true, new HttpCallback() {
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
                    });
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = mEdit_search.getText().toString();
                if(!TextUtils.isEmpty(str)){
                    mLoadingDialog.showDialog();
                    SearchXs(str);
                } else {
                    Toast.makeText(SearchActivity.this, "不能为空!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mSearchAdapter = new SearchAdapter(SearchActivity.this);
        mDownloadUtil = new DownloadUtil();
        // 加载动画
        mLoadingDialog = new LoadingDialog(this);
        mLoadingDialog.setCancelable(true);
        mLoadingDialog.setCanceledOnTouchOutside(false);
    }

    private void mainToast(final String str){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mLoadingDialog.dismiss();
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
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mLoadingDialog.dismiss();
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
            });
        } catch (UnsupportedEncodingException e) {
            e.getStackTrace();
        }
    }
}
