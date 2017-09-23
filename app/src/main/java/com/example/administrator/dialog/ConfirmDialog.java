package com.example.administrator.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.txtread.R;

public class ConfirmDialog extends Dialog{

    private String mMsg;

    private View.OnClickListener mCallback;

    public ConfirmDialog(Context context, String msg, View.OnClickListener call){
        super(context, R.style.Theme_dialog_empty);
        mMsg = msg;
        mCallback = call;
        this.setCanceledOnTouchOutside(false);
        this.setCancelable(true);
    }

    public ConfirmDialog(Context context, String msg){
        super(context, R.style.Theme_dialog_empty_two);
        mMsg = msg;
        this.setCanceledOnTouchOutside(false);
        this.setCancelable(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_dialog);
        initView();
    }

    private void initView(){
        Button btnRetry = (Button) findViewById(R.id.btn_confirm);
        Button btnCancel = (Button) findViewById(R.id.btn_cancel);
        TextView tvTitle = (TextView) findViewById(R.id.notic_title);
        TextView tvMessage = (TextView) findViewById(R.id.common_text);
        tvMessage.setText(mMsg);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(mCallback != null){
                    mCallback.onClick(v);
                }
            }
        });
    }
}
