package com.example.administrator.Util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Typeface;
import android.text.TextUtils;

import com.example.administrator.application.App;
import com.example.administrator.txtread.R;

import java.util.ArrayList;
import java.util.List;

public class TxtUtil {

    private static String mTitle = "";

    private static String mData = "";

    private static String mUrl = "";

    private static String mHomeUrl = "";

    private static Paint mPaint;

    private static int mX = 5;

    private static int mFontHeight = -1;

    // 行距
    private static int mHangju = 0;

    // 字体大小 （小米是 55）默认 30
    private static int mTextSize = 30;

    private static int mY = mTextSize + 30;

    // 字体颜色
    private static int textColor = Color.BLACK;

    // 背景颜色
    private static int bColor = App.getInstance().getColorByResid(R.color.transparent);

    private static List<String> mLines = new ArrayList<>();

    public static String getmTitle() {
        return mTitle;
    }

    public static void setmTitle(String title) {
        mTitle = title;
    }

    /**
     * 获取格式化后的数据
     *
     * @return
     */
    public static String getData() {
        return mData;
    }

    public static void setData(String str, String url, String homeurl) {
        mData = str;
        mUrl = url;
        mHomeUrl = homeurl;
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
            mPaint.setAntiAlias(true);
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
            if (lines.size() % count == 1) {
                String lastData = lines.get(lines.size() - 1);
                lastData = lastData.replace("\r", "").replace(" ", "").replace("\t", "");
                if (!TextUtils.isEmpty(lastData)) {
                    ye++;
                }
            } else if (lines.size() % count == 2) {
                String lastData1 = lines.get(lines.size() - 1).replace("\r", "").replace(" ", "").replace("\t", "");
                String lastData2 = lines.get(lines.size() - 2).replace("\r", "").replace(" ", "").replace("\t", "");
                if (!TextUtils.isEmpty(lastData1) && !TextUtils.isEmpty(lastData2)) {
                    ye++;
                }
            } else if (lines.size() % count == 3) {
                String lastData1 = lines.get(lines.size() - 1).replace("\r", "").replace(" ", "").replace("\t", "");
                String lastData2 = lines.get(lines.size() - 2).replace("\r", "").replace(" ", "").replace("\t", "");
                String lastData3 = lines.get(lines.size() - 3).replace("\r", "").replace(" ", "").replace("\t", "");
                if (!TextUtils.isEmpty(lastData1) && !TextUtils.isEmpty(lastData2) && !TextUtils.isEmpty(lastData3)) {
                    ye++;
                }
            } else if (lines.size() % count > 3) {
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
        Bitmap bitmap = Bitmap.createBitmap(App.getInstance().mScreenWidth, App.getInstance().mScreenHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvas.drawColor(bColor);
        Paint paint = getTxtPaint();
        int FontHeight = getFontHeight();
        // LogUtil.e("FontHeight: " + FontHeight);
        List<String> lines = parsingData();
        // LogUtil.e("lines.size(): " + lines.size());
        int x = mX;
        int y = mY;
        int count = getHang();
        // LogUtil.e("count: " + count);
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
        App.getInstance().savetag(mHomeUrl, mUrl, index);
        return bitmap;
    }

}
