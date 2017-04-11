package com.example.administrator.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;

import com.example.administrator.Util.LogUtil;
import com.example.administrator.entity.GifFrame;
import com.example.administrator.inter.GifAction;
import com.example.administrator.thread.GifDecoder;

import java.io.InputStream;

/**
 * GifView
 * 本类可以显示一个gif动画，其使用方法和android的其它view（如imageview)一样。
 * 如果要显示的gif太大，会出现OOM的问题。
 *
 */
public class GifView extends View implements GifAction {
    /**
     * gif解码器
     */
    private GifDecoder gifDecoder = null;
    /**
     * 当前要画的帧的图
     */
    private Bitmap currentImage = null;

    private boolean isRun = true;

    private boolean pause = false;

    private boolean drawInCenter = false;

    private int showWidth = -1;
    private Rect rect = null;

    private DrawThread drawThread = null;

    private Handler redrawHandler;

    private GifImageType animationType = GifImageType.SYNC_DECODER;

    /**
     * 解码过程中，Gif动画显示的方式<br>
     * 如果图片较大，那么解码过程会比较长，这个解码过程中，gif如何显示
     *
     * @author liao
     */
    public enum GifImageType {
        /**
         * 在解码过程中，不显示图片，直到解码全部成功后，再显示
         */
        WAIT_FINISH(0),
        /**
         * 和解码过程同步，解码进行到哪里，图片显示到哪里
         */
        SYNC_DECODER(1),
        /**
         * 在解码过程中，只显示第一帧图片
         */
        COVER(2);

        GifImageType(int i) {
            nativeInt = i;
        }

        final int nativeInt;
    }

    public GifView(Context context) {
        super(context);

    }

    public GifView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GifView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    /**
     * 设置图片，并开始解码
     *
     * @param gif 要设置的图片
     */
    private void setGifDecoderImage(byte[] gif) {
        redrawHandler = mRedrawHandler;

        if (gifDecoder != null) {
            gifDecoder.free();
            gifDecoder = null;
        }
        gifDecoder = new GifDecoder(gif, this);
        gifDecoder.start();
    }

    /**
     * 设置图片，开始解码
     *
     * @param is 要设置的图片
     */
    private void setGifDecoderImage(InputStream is) {
        redrawHandler = mRedrawHandler;

        if (gifDecoder != null) {
            gifDecoder.free();
            gifDecoder = null;
        }
        gifDecoder = new GifDecoder(is, this);
        gifDecoder.start();
    }

    /**
     * 以字节数据形式设置gif图片
     *
     * @param gif 图片
     */
    public void setGifImage(byte[] gif) {
        setGifDecoderImage(gif);
    }

    /**
     * 以字节流形式设置gif图片
     *
     * @param is 图片
     */
    public void setGifImage(InputStream is) {
        setGifDecoderImage(is);
    }

    /**
     * 以资源形式设置gif图片
     *
     * @param resId gif图片的资源ID
     */
    public void setGifImage(int resId) {
        Resources r = this.getResources();
        InputStream is = r.openRawResource(resId);
        setGifDecoderImage(is);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (gifDecoder == null)
            return;
        if (currentImage == null) {
            currentImage = gifDecoder.getImage();
        }
        if (currentImage == null) {
            return;
        }
        int saveCount = canvas.getSaveCount();
        canvas.save();
        canvas.translate(getPaddingLeft(), getPaddingTop());
        if (showWidth == -1) {
            if (drawInCenter) {
                int x = (getWidth() - currentImage.getWidth()) / 2;
                x = x > 0 ? x : 0;
                int y = (getHeight() - currentImage.getHeight()) / 2;
                y = y > 0 ? y : 0;
                canvas.drawBitmap(currentImage, x, y, null);
            } else {
                canvas.drawBitmap(currentImage, 0, 0, null);
            }
        } else {
            try {
                canvas.drawBitmap(currentImage, null, rect, null);
            } catch (Exception e) {
            }
        }
        canvas.restoreToCount(saveCount);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int pleft = getPaddingLeft();
        int pright = getPaddingRight();
        int ptop = getPaddingTop();
        int pbottom = getPaddingBottom();

        int widthSize;
        int heightSize;

        int w;
        int h;

        if (gifDecoder == null) {
            w = 1;
            h = 1;
        } else {
            w = gifDecoder.width;
            h = gifDecoder.height;
        }

        w += pleft + pright;
        h += ptop + pbottom;

        w = Math.max(w, getSuggestedMinimumWidth());
        h = Math.max(h, getSuggestedMinimumHeight());

        widthSize = resolveSize(w, widthMeasureSpec);
        heightSize = resolveSize(h, heightMeasureSpec);

        setMeasuredDimension(widthSize, heightSize);
    }

    /**
     * 只显示第一帧图片<br>
     * 调用本方法后，gif不会显示动画，只会显示gif的第一帧图
     */
    public void showCover() {
        if (gifDecoder == null)
            return;
        pause = true;
        currentImage = gifDecoder.getImage();
        invalidate();
    }

    /**
     * 继续显示动画<br>
     * 本方法在调用showCover后，会让动画继续显示，如果没有调用showCover方法，则没有任何效果
     */
    public void showAnimation() {
        if (pause) {
            pause = false;
        }
    }

    /**
     * 设置gif在解码过程中的显示方式<br>
     * <strong>本方法只能在setGifImage方法之前设置，否则设置无效</strong>
     *
     * @param type 显示方式
     */
    public void setGifImageType(GifImageType type) {
        if (gifDecoder == null)
            animationType = type;
    }

    /**
     * 设置要显示的图片的大小<br>
     * 当设置了图片大小 之后，会按照设置的大小来显示gif（按设置后的大小来进行拉伸或压缩）
     *
     * @param width  要显示的图片宽
     * @param height 要显示的图片高
     */
    public void setShowDimension(int width, int height) {
        if (width > 0 && height > 0) {
            showWidth = width;
            rect = new Rect();
            rect.left = 0;
            rect.top = 0;
            rect.right = width;
            rect.bottom = height;
        }
    }

    public void parseOk(boolean parseStatus, int frameIndex) {
        if (parseStatus) {
            if (gifDecoder != null) {
                switch (animationType) {
                    case WAIT_FINISH:
                        if (frameIndex == -1) {
                            if (gifDecoder.getFrameCount() > 1) { // 当帧数大于1时，启动动画线程
                                DrawThread dt = new DrawThread();
                                dt.start();
                            } else {
                                reDraw();
                            }
                        }
                        break;
                    case COVER:
                        if (frameIndex == 1) {
                            currentImage = gifDecoder.getImage();
                            reDraw();
                        } else if (frameIndex == -1) {
                            if (gifDecoder.getFrameCount() > 1) {
                                if (drawThread == null) {
                                    drawThread = new DrawThread();
                                    drawThread.start();
                                }
                            } else {
                                reDraw();
                            }
                        }
                        break;
                    case SYNC_DECODER:
                        if (frameIndex == 1) {
                            currentImage = gifDecoder.getImage();
                            reDraw();
                        } else if (frameIndex == -1) {
                            reDraw();
                        } else {
                            if (drawThread == null) {
                                drawThread = new DrawThread();
                                drawThread.start();
                            }
                        }
                        break;
                }

            } else {
                LogUtil.e("parse error");
            }
        }
    }

    private void reDraw() {
        if (redrawHandler != null) {
            try {
                Message msg = redrawHandler.obtainMessage();
                if (msg != null && redrawHandler != null) {
                    redrawHandler.sendMessage(msg);
                }
            } catch (Exception e) {
                ;
            }
        }
    }

    public void reCircle() {
        this.redrawHandler = null;
        this.drawThread = null;
    }

    private Handler mRedrawHandler = new Handler() {
        public void handleMessage(Message msg) {
            invalidate();
        }
    };

    public boolean isDrawInCenter() {
        return drawInCenter;
    }

    public void setDrawInCenter(boolean drawInCenter) {
        this.drawInCenter = drawInCenter;
    }

    /**
     * 动画线程
     *
     * @author liao
     */
    private class DrawThread extends Thread {

        @Override
        public void run() {
            if (gifDecoder == null) {
                return;
            }
            while (isRun) {
                if (!pause) {
                    // if(gifDecoder.parseOk()){
                    if (gifDecoder != null) {
                        GifFrame frame = gifDecoder.next();
                        if (null != frame) {
                            currentImage = frame.image;
                            long sp = frame.delay;
                            if (redrawHandler != null) {
                                try {
                                    Message msg = redrawHandler.obtainMessage();
                                    if (msg != null && redrawHandler != null) {
                                        redrawHandler.sendMessage(msg);
                                    }
                                } catch (Exception e) {
                                    e.getStackTrace();
                                }
                                SystemClock.sleep(sp);
                            } else {
                                if (currentImage != null && !currentImage.isRecycled()) {
                                    currentImage.recycle();
                                    currentImage = null;
                                }

                                if (gifDecoder != null) {
                                    gifDecoder.free();
                                    gifDecoder = null;
                                }
                                break;
                            }
                        }
                    }
                } else {
                    SystemClock.sleep(10);
                }
            }
        }
    }
}