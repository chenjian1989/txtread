package com.example.administrator.txtread;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Typeface;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class TxtUtil {

    private static String mData = "";

    private static String mUrl = "";

    private static Paint mPaint;

    private static int mX = 5;

    private static int mFontHeight = -1;

    private static int mY = 80;

    // 行距
    private static int mHangju = 0;

    // 字体大小
    private static int mTextSize = 50;

    // 字体颜色
    private static int textColor = Color.RED;

    // 背景颜色
    private static int bColor = Color.WHITE;

    private static List<String> mLines = new ArrayList<>();

    /**
     * 获取格式化后的数据
     *
     * @return
     */
    public static String getData() {
        return mData;
    }

    public static void setData(String str, String url) {
        mData = str;
        mUrl = url;
        // 章节数据发生变化,分页数据要刷新
        mLines.clear();
    }

    /**
     * 获取文字显示的高度
     *
     * @return
     */
    public static int getFontHeight() {
        if (mFontHeight == -1) {
            Paint.FontMetrics fm = getTxtPaint().getFontMetrics();
            return (int) Math.ceil(fm.descent - fm.top) + 2;
        } else {
            return mFontHeight;
        }
    }

    /**
     * 获取paint对象
     *
     * @return
     */
    public static Paint getTxtPaint() {
        if (mPaint == null) {
            mPaint = new Paint();
            mPaint.setColor(textColor);
            mPaint.setTypeface(Typeface.DEFAULT);
            //paint.setAntiAlias(true);
            mPaint.setFilterBitmap(true);
            mPaint.setSubpixelText(true);
            mPaint.setTextSize(mTextSize);
        }
        return mPaint;
    }

    /**
     * 设置字体大小
     *
     * @param size
     */
    public static void setTxtPaintTextSize(int size) {
        getTxtPaint().setTextSize(size);
        mTextSize = size;
        mY = size + 30;
        // 字体大小变化后,分页数据要刷新,字体高度要刷新
        mFontHeight = -1;
        mLines.clear();
    }

    public static void setTextColor(int color) {
        getTxtPaint().setColor(color);
        textColor = color;
    }

    public static void setbColor(int color) {
        bColor = color;
    }

    /**
     * 设置行距大小
     *
     * @param hangju
     */
    public static void setHangju(int hangju) {
        mHangju = hangju;
    }

    /**
     * 将一章的文字,按段落分开,存储在集合里
     *
     * @param datas
     * @return
     */
    public static List<String> SeperateByParagph(String datas) {
        List<String> paragphdatas = new ArrayList<>();
        if (datas != null) {
            String[] ps = datas.split("\n");
            for (int i = 0; i < ps.length; i++) {
                ps[i] = ps[i] + "  ";
                paragphdatas.add(ps[i]);
            }
//            if (paragphdatas.size() > 0) {
//                String lastp = paragphdatas.get(paragphdatas.size() - 1).substring(0,
//                        paragphdatas.get(paragphdatas.size() - 1).length() - 1);
//                paragphdatas.remove(paragphdatas.size() - 1);
//                paragphdatas.add(lastp);
//            }
        } else {
            paragphdatas.add(datas);
        }
        return paragphdatas;
    }

    /**
     * 将段落字符串截取成句数据，存储在集合里
     *
     * @param paragraphstr
     * @return
     */
    public static List<String> SeparateParagraphtoLines(String paragraphstr) {

        List<String> linesdata = new ArrayList<>();
        String str = paragraphstr;
        for (; str.length() > 0; ) {
            int nums = getTxtPaint().breakText(str, true, App.getInstance().mScreenWidth, null);
            if (nums <= str.length()) {
                String linnstr = str.substring(0, nums);
                linesdata.add(linnstr);
                str = str.substring(nums, str.length());
            } else {
                linesdata.add(str);
                str = "";
            }
        }
        return linesdata;
    }

    /**
     * 将一章文字全部分解成一行一行的数据,存储在集合里
     *
     * @return
     */
    public static List<String> parsingData() {
        if (mLines.size() == 0) {
            List<String> duan = SeperateByParagph(getData());
            if (duan != null && duan.size() > 0) {
                mLines.clear();
                for (String str : duan) {
                    List<String> line = SeparateParagraphtoLines(str);
                    mLines.addAll(line);
                }
            }
        }
        return mLines;
    }

    /**
     * 一章可以分为多少页码
     *
     * @return
     */
    public static int paging() {
        int count = getHang();
        List<String> lines = parsingData();
        if (lines.size() > 0) {
            int ye = lines.size() / count;
            if (lines.size() % count > 0) {
                ye++;
            }
            return ye;
        }
        return -1;
    }

    /**
     * 一页数据可以显示的行数
     *
     * @return
     */
    public static int getHang() {
        int height = App.getInstance().mScreenHeight - mY;
        return height / (getFontHeight() + mHangju);
    }

    /**
     * @param index 页码下标 从0开始
     * @return
     */
    public static Bitmap getBitMap(int index) {
        long a = System.currentTimeMillis();
        Bitmap bitmap = Bitmap.createBitmap(App.getInstance().mScreenWidth, App.getInstance().mScreenHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvas.drawColor(bColor);
        Paint paint = getTxtPaint();
        int FontHeight = getFontHeight();
        //Log.e("txtread", "FontHeight: " + FontHeight);
        List<String> lines = parsingData();
        //Log.e("txtread", "lines.size(): " + lines.size());
        int x = mX;
        int y = mY;
        int count = getHang();
        //Log.e("txtread", "count: " + count);
        int i = index * count;
        int end = count * (index + 1);
        for (; i < end; i++) {
            if (i < lines.size()) {
                canvas.drawText(lines.get(i), x, y, paint);
                y = y + FontHeight + mHangju;
            } else {
                break;
            }
        }
        App.getInstance().savetag(mUrl, index);
        long b = System.currentTimeMillis() - a;
        Log.e("txtread", "getBitmap()耗时: " + b);
        return bitmap;
    }

}
