package com.example.administrator.Util;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.widget.ImageView;

public class PicUtil {

    /**
     * 根据网络路径加载图片(imageview.setTag() 必需赋值)
     *
     * @param imageView
     * @param url
     * @param imageCallback
     */
    public void LoadImage(final ImageView imageView, final String url, final ImageCallback imageCallback) {
        final Handler handler = new Handler();
        final String path = HttpUtil.httpImageUrl(url, new HttpCallback() {
            @Override
            public void httpSuccess(final String data, String path) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (imageCallback != null) {
                            imageCallback.loadsuccess(data);
                        }
                        if (imageView != null && imageView.getTag().equals(url)) {
                            imageView.setImageBitmap(getBitmapByPath(data));
                        }
                    }
                });
            }
            @Override
            public void httpError(String des) {
                LogUtil.e("PicUtil-HttpError: " + des);
            }
        });
        if (path != null) {
            if (imageView != null && imageView.getTag().equals(url)) {
                imageView.setImageBitmap(getBitmapByPath(path));
            }
        }
    }

    /**
     * 加载图片到 imageview控件里(imageview.setTag() 必需赋值)
     *
     * @param imageView
     * @param url
     */
    public void LoadImage(ImageView imageView, String url) {
        LoadImage(imageView, url, null);
    }

    public void LoadImage(String url, ImageCallback imageCallback) {
        LoadImage(null, url, imageCallback);
    }

    /**
     * 根据本地路径读取图片文件(图片不缩放)
     *
     * @param path
     * @return
     */
    public Bitmap getBitmapByPath(String path) {
        return readBitmap(path, 0, 0);
    }

    /**
     * 读取图片，按照长宽比返回长宽不超过设定值的bitmap对象
     * <p>
     *
     * @param path      图片全路径
     * @param maxWidth  最大宽度
     * @param maxHeight 最大高度
     * @return Bitmap
     */
    public synchronized Bitmap readBitmap(String path, int maxWidth, int maxHeight) {

        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            // 设置为true,表示解析Bitmap对象，该对象不占内存，这里主要是要获得图片的高宽
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);

            // 设置缩放比例
            int be = computeScale(options, maxWidth, maxHeight);
            options.inSampleSize = be;

            // 设置为false,解析Bitmap对象加入到内存中
            options.inJustDecodeBounds = false;

            // 设置内存不足时，比bitmap对象可以被回收
            options.inPurgeable = true;
            options.inInputShareable = true;

            options.inPreferredConfig = Bitmap.Config.RGB_565;

            return BitmapFactory.decodeFile(path, options);
        } catch (Exception e) {
            LogUtil.e("readBitmap时出错: " + path + "  " + maxWidth + "  " + maxHeight + "  "
                    + e.getMessage());
            return null;
        }

    }

    /**
     * 根据View(主要是ImageView)的宽和高来计算Bitmap缩放比例。默认不缩放
     *
     * @param options
     * @param viewWidth
     * @param viewHeight
     * @return
     */
    public int computeScale(BitmapFactory.Options options, int viewWidth, int viewHeight) {
        int inSampleSize = 1;
        if (viewWidth == 0 || viewHeight == 0) {
            return inSampleSize;
        }
        int bitmapWidth = options.outWidth;
        int bitmapHeight = options.outHeight;

        // 假如Bitmap的宽度或高度大于我们设定图片的View的宽高，则计算缩放比例
        if (bitmapWidth > viewWidth || bitmapHeight > viewHeight) {
            int widthScale = (int) Math.ceil((float) bitmapWidth / (float) viewWidth);
            int heightScale = (int) Math.ceil((float) bitmapHeight / (float) viewHeight);

            // 为了保证图片不缩放变形，我们取缩放比例最大的那个
            inSampleSize = widthScale > heightScale ? widthScale : heightScale;
        }
        return inSampleSize;
    }
}
