package com.example.administrator.txtread;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.Util.DownloadUtil;
import com.example.administrator.Util.HttpUtil;
import com.example.administrator.Util.LogUtil;
import com.example.administrator.adapter.TxtAdapter;
import com.example.administrator.application.App;
import com.example.administrator.base.CommonBaseActivity;
import com.example.administrator.dialog.ConfirmDialog;
import com.example.administrator.entity.HomeTxtEntity;
import com.example.administrator.inter.HttpCallback;
import com.example.administrator.inter.LocalCallBack;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends CommonBaseActivity {

    private TxtAdapter mTxtAdapter;

    private ListView mList_txt;

    private DownloadUtil mDownloadUtil;

    private List<HomeTxtEntity> mList_homes;

    private ConfirmDialog mConfirmDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        App.getInstance().verifyStoragePermissions(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (App.getInstance().ismIsUpdate()) {
            shuaxin(null);
            App.getInstance().setmIsUpdate(false);
        }
    }

    private void initView() {
        TextView txt_search = (TextView) findViewById(R.id.txt_search);
        mList_txt = (ListView) findViewById(R.id.listview_txtlist);

        txt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        mList_txt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HomeTxtEntity homeTxtEntity = (HomeTxtEntity) mTxtAdapter.getItem(i);
                Intent intent = new Intent(MainActivity.this, TxtDetailActivity.class);
                intent.putExtra("url", homeTxtEntity.getHomeUrl());
                startActivity(intent);
            }
        });

        mList_txt.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                ConfirmDialog confirmDialog = new ConfirmDialog(MainActivity.this, "确定移出书架!", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HomeTxtEntity homeTxtEntity = (HomeTxtEntity) mTxtAdapter.getItem(position);
                        App.getInstance().deleteHomeList(homeTxtEntity.getHomeUrl());
                        mList_homes.remove(homeTxtEntity);
                        mTxtAdapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this, "删除成功!", Toast.LENGTH_SHORT).show();
                    }
                });
                confirmDialog.show();
                return true;
            }
        });
    }

    private void initData() {
        mTxtAdapter = new TxtAdapter(MainActivity.this);
        mDownloadUtil = new DownloadUtil();
        queryLocalData();
    }

    private void queryLocalData() {
        final long a = System.currentTimeMillis();
        mDownloadUtil.queryLocalData(new LocalCallBack() {
            @Override
            public void LocalSuccess(List<HomeTxtEntity> hts) {
                mList_homes = hts;
                mTxtAdapter.initData(mList_homes);
                long b = System.currentTimeMillis() - a;
                LogUtil.e("耗时：  " + b);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mList_txt.setAdapter(mTxtAdapter);
                        houtaiDownload();
                    }
                });
            }
        }, false);
    }

    private void shuaxin(final String des) {
        mDownloadUtil.queryLocalData(new LocalCallBack() {
            @Override
            public void LocalSuccess(List<HomeTxtEntity> hts) {
                mList_homes = hts;
                mTxtAdapter.initData(hts);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTxtAdapter.notifyDataSetChanged();
                        if (!TextUtils.isEmpty(des)) {
                            Toast.makeText(MainActivity.this, des, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }, false);
    }

    private void houtaiDownload() {
        if (!App.getInstance().isNetworkAvailable()) {
            if (mConfirmDialog == null) {
                mConfirmDialog = new ConfirmDialog(MainActivity.this, "当前未连接网络!");
            }
            mConfirmDialog.show();
            return;
        }
        try {
            final List<String> homeList = App.getInstance().getHomeList();
            final List<String> temp = new ArrayList<>();
            temp.addAll(homeList);
            for (String path : homeList) {
                // 强制刷新列表数据
                HttpUtil.httpGetUrl(path, path, true, new HttpCallback() {
                    @Override
                    public void httpSuccess(String data, String url) {
                        if (temp.contains(url)) {
                            temp.remove(url);
                            if (temp.size() == 0) {
                                LogUtil.e("书架数据刷新完成!");
                                shuaxin("书架刷新完成!");
                            }
                        }
                    }

                    @Override
                    public void httpError(String des) {
                        LogUtil.e("MainActivity--强刷书架数据ERROR: " + des);
                    }
                }, true);
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

}
