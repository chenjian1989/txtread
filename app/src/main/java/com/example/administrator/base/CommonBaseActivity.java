package com.example.administrator.base;

import android.app.Activity;
import android.os.Bundle;

import com.example.administrator.dialog.MyDialog;

/**
 * Activity基类
 */
public abstract class CommonBaseActivity extends Activity {

    /* 提示框对象 */
    private MyDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 弹出自定义对话框
     *
     * @param cancelable
     */
    protected void showSelfDefineDialog(boolean cancelable) {
        if(mDialog == null){
            mDialog = new MyDialog(this);
            mDialog.setCanceledOnTouchOutside(false);
        } else {
            mDialog.donghua();
        }
        mDialog.setCancelable(cancelable);
        mDialog.show();
    }

    /**
     * 隐藏自定义对话框
     */
    protected void removeSelfDefineDialog() {
        if(mDialog != null){
            mDialog.dismiss();
        }
    }
}
