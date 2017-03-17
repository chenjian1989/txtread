package com.example.administrator.txtread;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements GestureDetector.OnGestureListener {

    private ImageView mImageView;
    private TextView mTextView;

    //定义手势检测器实例
    private GestureDetector detector;

    private int mIndex = 0;

    private int mTotal = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        initDisplayMetrics();
        //创建手势检测器
        detector = new GestureDetector(this, this);
        mTotal = TxtUtil.paging();
        mImageView = (ImageView) findViewById(R.id.img_txt);
        mTextView = (TextView) findViewById(R.id.txt_yema);
        load();
    }

    private void initDisplayMetrics() {
        //定义DisplayMetrics 对象
        DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        App.getInstance().InitWidthAndHeigh(dm.widthPixels, dm.heightPixels);

        Log.e("txtread", "dm.widthPixels: " + dm.widthPixels);
        Log.e("txtread", "dm.heightPixels: " + dm.heightPixels);
    }

    private void load() {
        if (mIndex < mTotal && mIndex >= 0) {
            mImageView.setImageBitmap(TxtUtil.getBitMap(mIndex));
            int ind = mIndex + 1;
            mTextView.setText(ind + "/" + mTotal);
        } else {
            Toast.makeText(this, "数据格式错误,或没有数据!!", Toast.LENGTH_SHORT).show();
        }
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

    private void hfy(){
        mIndex++;
        if (mTotal <= mIndex) {
            mIndex--;
            Toast.makeText(this, "已经是最后一页!!", Toast.LENGTH_SHORT).show();
        } else {
            load();
        }
    }

    private void qfy(){
        mIndex--;
        if (mIndex < 0) {
            mIndex++;
            Toast.makeText(this, "已经是第一页!!", Toast.LENGTH_SHORT).show();
        } else {
            load();
        }
    }


    @Override
    public boolean onSingleTapUp(MotionEvent me) {
        float x = me.getX();
        float y = me.getY();
        int w = App.getInstance().mScreenWidth / 3;
        int yw = 2 * w;

        if(x < w){
            qfy();
        }
        if(x > yw){
            hfy();
        }

        //Toast.makeText(this, "您单击的位置是:\nx:" + x + "\n y:" + y, Toast.LENGTH_SHORT).show();
        return false;
    }


    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float velocityX,
                            float velocityY) {
        return false;
    }
}
