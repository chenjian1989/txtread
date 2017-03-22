package com.example.administrator.txtread;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.Util.DownloadUtil;
import com.example.administrator.Util.LocalCallBack;
import com.example.administrator.Util.LogUtil;
import com.example.administrator.Util.TxtUtil;
import com.example.administrator.Util.callback;
import com.example.administrator.adapter.ZhangjieAdapter;
import com.example.administrator.dialog.LoadingDialog;
import com.example.administrator.entity.HomeTxtEntity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


public class TxtDetailActivity extends Activity implements GestureDetector.OnGestureListener, battery {
    private ImageView mImageView;
    private TextView mTextView;
    private RelativeLayout mRel_list;
    private ListView mListView;
    private TextView mText_zhangjie;
    private TextView mText_time;
    private TextView mText_dianliang;
    private TextView mText_zhang;

    //定义手势检测器实例
    private GestureDetector detector;
    // http://www.biquge.tw/9_9080/5134179.html
    private String mUrl = "http://www.biquge.tw/9_9080/5306729.html";

    private String mHomeUrl;

    private ArrayList<String> mChapters;

    private int mIndex = 0;

    private int mTotal = -1;

    private int mZhuangjieIndex = 0;

    private String mZhangjieName;

    private boolean isLoading = true;

    private DownloadUtil mDownloadUtil;

    private LoadingDialog mLoadingDialog;

    private ZhangjieAdapter mZhangjie;

    private boolean isZhangjie = false;

    private static final int MSG_LOAD = 1;

    private static final int MSG_ERROR = 2;

    private static final int MSG_TIME = 3;

    private static final int MSG_LOAD_CHAPTER = 4;

    private static final String DATE_FORMAT = "HH:mm:ss";

    private MyHandler myHandler;

    private static class MyHandler extends Handler {
        WeakReference<TxtDetailActivity> mActivityReference;

        MyHandler(TxtDetailActivity activity) {
            mActivityReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final TxtDetailActivity activity = mActivityReference.get();
            if (activity != null) {
                switch (msg.what) {
                    case MSG_LOAD:
                        activity.MainLoad();
                        break;
                    case MSG_ERROR:
                        activity.MainErrorToast(msg.obj.toString());
                        break;
                    case MSG_TIME:
                        activity.mText_time.setText(App.getInstance().getDate(DATE_FORMAT));
                        break;
                    case MSG_LOAD_CHAPTER:
                        activity.init();
                        break;
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_txtdetail);

        mHomeUrl = getIntent().getStringExtra("url");
        myHandler = new MyHandler(this);
        // 加载动画
        mLoadingDialog = new LoadingDialog(this);
        mLoadingDialog.setCancelable(false);
        mLoadingDialog.setCanceledOnTouchOutside(false);
        mLoadingDialog.showDialog();
        // 下载工具初始化
        mDownloadUtil = new DownloadUtil();
        mDownloadUtil.queryLocaData(new LocalCallBack() {
            @Override
            public void LocalSuccess(List<HomeTxtEntity> hts) {
                if(hts.size() == 1){
                    mChapters = hts.get(0).getChapters();
                    myHandler.sendEmptyMessage(MSG_LOAD_CHAPTER);
                }
            }
        }, mHomeUrl);
    }

    private void init() {
        mLoadingDialog.dismiss();
        if (mChapters != null && mChapters.size() > 0) {
            mUrl = mChapters.get(0).split(";")[0];
        }

        String[] strs = App.getInstance().gettag(mHomeUrl);
        if (strs != null && strs.length == 2) {
            mUrl = strs[0];
            mIndex = Integer.parseInt(strs[1]);
        }

        initDisplayMetrics();
        // 注册电量监听
        App.getInstance().registerReceiver(this);
        // 创建手势检测器
        detector = new GestureDetector(this, this);
        // 创建章节列表适配器
        mZhangjie = new ZhangjieAdapter(TxtDetailActivity.this);
        mZhangjie.initData(mChapters);

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

        mRel_list = (RelativeLayout) findViewById(R.id.rel_list);
        mListView = (ListView) findViewById(R.id.listview_txtlist);
        mListView.setAdapter(mZhangjie);
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
                clearAll();
                initData();
            }
        });
        initData();
    }

    private void initData() {
        mLoadingDialog.showDialog();
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
        // 后台缓存当前章节以后的所有数据
        //mDownloadUtil.DownFuture();
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
                    myHandler.sendEmptyMessage(MSG_TIME);
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
            clearAll();
            mLoadingDialog.showDialog();
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
            clearAll();
            mLoadingDialog.showDialog();
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
        myHandler.sendEmptyMessage(MSG_LOAD);
    }

    private void loaderrors(String str) {
        if (TextUtils.isEmpty(str)) {
            str = "未知错误";
        }
        Message message = new Message();
        message.obj = str;
        message.what = MSG_ERROR;
        myHandler.handleMessage(message);
    }

    private void MainErrorToast(String str) {
        Toast.makeText(TxtDetailActivity.this, str, Toast.LENGTH_SHORT).show();
        mLoadingDialog.dismiss();
        load();
    }

    private void MainLoad() {
        mLoadingDialog.dismiss();
        mText_zhangjie.setText(calcZhangjie());
        mText_zhang.setText(mZhangjieName);
        load();
    }


    @Override
    public boolean onSingleTapUp(MotionEvent me) {
        if (isZhangjie) {
            mRel_list.setVisibility(View.GONE);
            isZhangjie = false;
            return false;
        }
        float x = me.getX();
        float y = me.getY();
        int w = App.getInstance().mScreenWidth / 3;
        int yw = 2 * w;
        int h = App.getInstance().mScreenHeight / 3;
        int yh = 2 * h;

        if((x > yw && y > h) || y > yh){
            hfy();
        } else if(x < w || y < h){
            qfy();
        } else {
            // 点击中间部分，显示章节列表
            mRel_list.setVisibility(View.VISIBLE);
            isZhangjie = true;
            mListView.setSelection(mZhuangjieIndex);
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
