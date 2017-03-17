package com.example.administrator.txtread;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class LoadingDialog extends Dialog {
    private ImageView img;
    private TextView txt;
    private Context mContext;

    public LoadingDialog(Context context) {
        super(context, R.style.progress_dialog);
        mContext = context;
        //加载布局文件
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.progress_dialog, null);
        img = (ImageView) view.findViewById(R.id.progress_dialog_img);
        txt = (TextView) view.findViewById(R.id.progress_dialog_txt);
        //给图片添加动态效果
        Animation anim = AnimationUtils.loadAnimation(context, R.anim.loading_dialog_progressbar);
        img.setAnimation(anim);
        txt.setText(R.string.progressbar_dialog_txt);
        //dialog添加视图
        setContentView(view);
    }

    public void showDialog(){
        Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.loading_dialog_progressbar);
        img.setAnimation(anim);
        show();
    }

    public void setMsg(String msg) {
        txt.setText(msg);
    }

}
