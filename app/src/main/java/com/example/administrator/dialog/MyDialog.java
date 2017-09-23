package com.example.administrator.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.administrator.txtread.R;

/**
 * 自定义dialog
 */
public class MyDialog extends Dialog {

    private Context context;
    private ImageView ivProgress;


    public MyDialog(Context context) {
        super(context, R.style.MyDialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_layout);
        ivProgress = (ImageView) findViewById(R.id.ivProgress);
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.dialog_progress_anim);
        ivProgress.startAnimation(animation);
    }
}
