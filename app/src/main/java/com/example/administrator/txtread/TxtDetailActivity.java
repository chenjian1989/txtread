package com.example.administrator.txtread;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.Util.DownloadUtil;
import com.example.administrator.Util.LogUtil;
import com.example.administrator.Util.TxtUtil;
import com.example.administrator.adapter.ZhangjieAdapter;
import com.example.administrator.application.App;
import com.example.administrator.base.CommonBaseActivity;
import com.example.administrator.entity.HomeTxtEntity;
import com.example.administrator.inter.LocalCallBack;
import com.example.administrator.inter.battery;
import com.example.administrator.inter.callback;

import java.util.ArrayList;
import java.util.List;

public class TxtDetailActivity extends CommonBaseActivity implements GestureDetector.OnGestureListener, battery {
    private ImageView mImageView;
    private TextView mTextView;
    private RelativeLayout mRel_list;
    private ListView mListView;
    private TextView mText_zhangjie;
    private TextView mText_time;
    private TextView mText_dianliang;
    private TextView mText_zhang;
    private RelativeLayout mRel_menu;

    //定义手势检测器实例
    private GestureDetector detector;
    // http://www.biquge.tw/9_9080/5134179.html
    private String mUrl;

    private String mHomeUrl;

    private ArrayList<String> mChapters;

    private int mIndex = 0;

    private int mTotal = -1;

    private int mZhuangjieIndex = 0;

    private String mZhangjieName;

    private boolean isLoading = true;

    private DownloadUtil mDownloadUtil;

    private ZhangjieAdapter mZhangjie;

    private boolean isZhangjie = false;

    private final String DATE_FORMAT = "HH:mm:ss";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_txtdetail);
        mHomeUrl = getIntent().getStringExtra("url");
        // 下载工具初始化
        mDownloadUtil = new DownloadUtil();
        showSelfDefineDialog(true);
        mDownloadUtil.queryLocaData(new LocalCallBack() {
            @Override
            public void LocalSuccess(List<HomeTxtEntity> hts) {
                if (hts.size() == 1) {
                    mChapters = hts.get(0).getChapters();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            init_Chapters();
                        }
                    });
                }
            }
        }, mHomeUrl);
        init();
    }

    private void init_Chapters() {
        if (mChapters != null && mChapters.size() > 0) {
            mDownloadUtil.setChapters(mChapters);
            // 创建章节列表适配器
            mZhangjie = new ZhangjieAdapter(TxtDetailActivity.this);
            mZhangjie.initData(mChapters);
            mListView.setAdapter(mZhangjie);
            if (TextUtils.isEmpty(mUrl)) {
                mUrl = mChapters.get(0).split(";")[0];
                mDownloadUtil.setUrl(mUrl);
                initData();
            } else {
                setZhangjieAndName();
            }
        }
    }

    private void init() {
        String[] strs = App.getInstance().gettag(mHomeUrl);
        if (strs != null && strs.length == 2) {
            mUrl = strs[0];
            mIndex = Integer.parseInt(strs[1]);
        }
        // 初始化屏幕分辨率
        initDisplayMetrics();
        // 注册电量监听
        App.getInstance().registerReceiver(this);
        // 创建手势检测器
        detector = new GestureDetector(this, this);

        mDownloadUtil.init(mUrl, mHomeUrl, mChapters);

        mImageView = (ImageView) findViewById(R.id.img_txt);
        // 页码显示
        mTextView = (TextView) findViewById(R.id.txt_yema);
        // 章节显示
        mText_zhangjie = (TextView) findViewById(R.id.txt_zhangjie);
        // 章节标题
        mText_zhang = (TextView) findViewById(R.id.txt_zhang);
        // 电量显示
        mText_dianliang = (TextView) findViewById(R.id.txt_dianliang);
        mText_dianliang.setText(App.getInstance().getmBatteryValue());
        // 时间显示
//        mText_time = (TextView) findViewById(R.id.txt_time);
//        mText_time.setText(App.getInstance().getDate(DATE_FORMAT));
//        // 后台刷新时间
//        shuaxinTime();

        mRel_menu = (RelativeLayout) findViewById(R.id.rel_menu);
        Button button_list = (Button) findViewById(R.id.btn_zhangjie);
        Button button_xiazai = (Button) findViewById(R.id.btn_xiazai);
        Button button_xiazai1 = (Button) findViewById(R.id.btn_xiazai1);

        button_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRel_menu.setVisibility(View.GONE);
                mRel_list.setVisibility(View.VISIBLE);
                mZhangjie.notifyDataSetChanged();
                mListView.setSelection(mZhuangjieIndex);
            }
        });

        button_xiazai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRel_menu.setVisibility(View.GONE);
                // 后台缓存当前章节以后的所有数据
                mDownloadUtil.DownFuture(50);
            }
        });

        button_xiazai1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRel_menu.setVisibility(View.GONE);
                // 后台缓存当前章节以后的所有数据
                mDownloadUtil.DownFuture(500);
            }
        });

        mRel_list = (RelativeLayout) findViewById(R.id.rel_list);
        mListView = (ListView) findViewById(R.id.listview_txtlist);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String str = mZhangjie.getItem(i).toString();
                mUrl = str.split(";")[0];
                // 初始化，需要跳转的url和页码下标
                mDownloadUtil.setUrl(mUrl);
                mIndex = 0;
                // 隐藏章节列表
                mRel_list.setVisibility(View.GONE);
                isZhangjie = false;
                // 清空imageview 并重新加载bitmap
                // clearAll();
                initData();
            }
        });
        if (!TextUtils.isEmpty(mUrl)) {
            initData();
        }
    }

    private void initData() {
        showSelfDefineDialog(true);
        mDownloadUtil.DownloadData(new callback() {
            @Override
            public void loadsuccess(String url) {
                mUrl = url;
                success();
            }

            @Override
            public void loaderror(String str) {
                loaderrors(str);
                LogUtil.e("getHttp()--loaderror " + str);
            }
        });
    }

    private void initDisplayMetrics() {
        //定义DisplayMetrics 对象
        DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        App.getInstance().InitWidthAndHeigh(dm.widthPixels, dm.heightPixels);

        LogUtil.e("dm.widthPixels: " + dm.widthPixels);
        LogUtil.e("dm.heightPixels: " + dm.heightPixels);
    }

    private void load() {
        isLoading = false;
        mTotal = TxtUtil.paging();
        if (mIndex < mTotal && mIndex >= 0) {
            mImageView.setImageBitmap(TxtUtil.getBitMap(mIndex));
            int ind = mIndex + 1;
            mTextView.setText(ind + "/" + mTotal);
        } else {
            Toast.makeText(this, "数据格式错误,或没有数据!!", Toast.LENGTH_SHORT).show();
        }
    }

    private String calcZhangjie() {
        if (mChapters != null && mChapters.size() > 0) {
            for (int i = 1; i <= mChapters.size(); i++) {
                String url = mChapters.get(i - 1);
                if (mUrl.equals(url.split(";")[0])) {
                    mZhuangjieIndex = i - 1;
                    mZhangjieName = url.split(";")[1];
                    return i + "/" + mChapters.size();
                }
            }
        } else {
            return "章节正在初始化!";
        }
        return "";
    }

    private void shuaxinTime() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.getStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mText_time.setText(App.getInstance().getDate(DATE_FORMAT));
                        }
                    });
                }
            }
        }).start();
    }


    //将该activity上的触碰事件交给GestureDetector处理
    public boolean onTouchEvent(MotionEvent me) {
        return detector.onTouchEvent(me);
    }

    @Override
    public boolean onDown(MotionEvent me) {
        return false;
    }

    /**
     * 滑屏监测
     */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        float minMove = 80;         //最小滑动距离
        float minVelocity = 0;      //最小滑动速度
        float beginX = e1.getX();
        float endX = e2.getX();
        float beginY = e1.getY();
        float endY = e2.getY();

        if (beginX - endX > minMove && Math.abs(velocityX) > minVelocity) {
            //左滑
            //Toast.makeText(this, velocityX + "左滑", Toast.LENGTH_SHORT).show();
            hfy();
        } else if (endX - beginX > minMove && Math.abs(velocityX) > minVelocity) {
            //右滑
            //Toast.makeText(this, velocityX + "右滑", Toast.LENGTH_SHORT).show();
            qfy();
        } else if (beginY - endY > minMove && Math.abs(velocityY) > minVelocity) {
            //上滑
            //Toast.makeText(this, velocityX + "上滑", Toast.LENGTH_SHORT).show();
        } else if (endY - beginY > minMove && Math.abs(velocityY) > minVelocity) {
            //下滑
            //Toast.makeText(this, velocityX + "下滑", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    private void hfy() {
        if (isLoading) {
            return;
        }
        isLoading = true;
        mIndex++;
        if (mTotal <= mIndex) {
            mIndex--;
            // clearAll();
            showSelfDefineDialog(true);
            mDownloadUtil.nextpage(new callback() {
                @Override
                public void loadsuccess(String url) {
                    mUrl = url;
                    mIndex = 0;
                    success();
                }

                @Override
                public void loaderror(String str) {
                    loaderrors(str);
                    LogUtil.e("nextpage()--loaderror " + str);
                }
            });
        } else {
            load();
        }
    }


    private void qfy() {
        if (isLoading) {
            return;
        }
        isLoading = true;
        mIndex--;
        if (mIndex < 0) {
            mIndex++;
            // clearAll();
            showSelfDefineDialog(true);
            mDownloadUtil.prevpage(new callback() {
                @Override
                public void loadsuccess(String url) {
                    mUrl = url;
                    mTotal = TxtUtil.paging();
                    mIndex = mTotal - 1;
                    success();
                }

                @Override
                public void loaderror(String str) {
                    loaderrors(str);
                    LogUtil.e("prevpage()--loaderror " + str);
                }
            });
        } else {
            load();
        }
    }

    private void clearAll() {
        mImageView.setImageBitmap(null);
        mTextView.setText("");
        mText_zhang.setText("");
        mText_zhangjie.setText("");
    }

    private void success() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                removeSelfDefineDialog();
                setZhangjieAndName();
                load();
            }
        });
    }

    private void loaderrors(String str) {
        if (TextUtils.isEmpty(str)) {
            str = "未知错误";
        }
        MainErrorToast(str);
    }

    private void MainErrorToast(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(TxtDetailActivity.this, str, Toast.LENGTH_SHORT).show();
                removeSelfDefineDialog();
                setZhangjieAndName();
            }
        });
    }

    private void setZhangjieAndName() {
        mText_zhangjie.setText(calcZhangjie());
        if (TextUtils.isEmpty(mZhangjieName)) {
            mZhangjieName = TxtUtil.getmTitle();
        }
        mText_zhang.setText(mZhangjieName);
    }

    @Override
    public boolean onSingleTapUp(MotionEvent me) {
        if (isZhangjie) {
            mRel_list.setVisibility(View.GONE);
            mRel_menu.setVisibility(View.GONE);
            isZhangjie = false;
            return false;
        }
        float x = me.getX();
        float y = me.getY();
        int w = App.getInstance().mScreenWidth / 3;
        int yw = 2 * w;
        int h = App.getInstance().mScreenHeight / 3;
        int yh = 2 * h;

        if ((x > yw && y > h) || y > yh) {
            hfy();
        } else if (x < w || y < h) {
            qfy();
        } else {
            // 点击中间部分，显示章节列表
            if (mChapters != null && mChapters.size() > 0) {
                mRel_menu.setVisibility(View.VISIBLE);
                isZhangjie = true;
            } else {
                Toast.makeText(TxtDetailActivity.this, "章节正在初始化,请稍后!", Toast.LENGTH_SHORT).show();
            }
        }
        return false;
    }


    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float velocityX,
                            float velocityY) {
        return false;
    }

    @Override
    public void change(int level, int scale) {
        // "电池电量为" + ((level * 100) / scale) + "%";
        int dianliang = level * 100 / scale;
        if (mText_dianliang != null) {
            mText_dianliang.setText(dianliang + "%");
        }
    }
}
