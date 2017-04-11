package com.example.administrator.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TextView;

import com.example.administrator.txtread.R;
import com.example.administrator.view.GifView;

public class LoadingBlack extends Dialog {
    private Context context;

    /**
     * 提示信息请不要太长，最多四个字 ！
     */
    private String msg;

    private GifView gif;

    private TextView tv;

    public LoadingBlack(Context context, int theme, String msg) {
        super(context, theme);
        this.context = context;
        this.msg = msg;
        this.setCancelable(true);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_progress);
        tv = (TextView) findViewById(R.id.msg);
        tv.setText(msg);
    }

    public void onStart() {
        gif = (GifView) findViewById(R.id.gif);
        gif.setGifImage(R.drawable.loading);
        Resources r = context.getResources();
        gif.setShowDimension(r.getDimensionPixelSize(R.dimen.bigloading_w_h),
                r.getDimensionPixelSize(R.dimen.bigloading_w_h));
    }

    public void dismiss() {
        super.dismiss();
        if (gif != null) {
            gif.reCircle();
            gif = null;
        }
    }
}
