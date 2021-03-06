package com.example.administrator.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.administrator.Util.LogUtil;
import com.example.administrator.inter.battery;
import com.example.administrator.application.App;

public class BatteryReceiver extends BroadcastReceiver {

    private battery mB;

    @Override
    public void onReceive(Context context, Intent intent) {
        //判断它是否是为电量变化的Broadcast Action
        if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
            //获取当前电量
            int level = intent.getIntExtra("level", 0);
            //电量的总刻度
            int scale = intent.getIntExtra("scale", 100);
            //把它转成百分比
            if(mB != null){
                mB.change(level, scale);
            }
            LogUtil.e("电量刷新!!!");
            int dianliang = level * 100 / scale;
            App.getInstance().setmBatteryValue(dianliang + "%");
        }
    }

    public void registerReceiver(battery b){
        mB = b;
    }
}
