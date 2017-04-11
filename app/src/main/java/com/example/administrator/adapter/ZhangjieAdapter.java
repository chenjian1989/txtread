package com.example.administrator.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.application.App;
import com.example.administrator.txtread.R;

import java.util.ArrayList;

public class ZhangjieAdapter extends BaseAdapter {
    /**
     * 上下文Context
     */
    private Context mCt;

    private ArrayList<String> mList;

    public ZhangjieAdapter(Context context){
        mCt = context;
    }

    public void initData(ArrayList<String> list){
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mCt).inflate(R.layout.item_zhangjie, null);
            viewHolder.textView_name = (TextView) convertView.findViewById(R.id.txt_name);
            viewHolder.textView_hc = (TextView) convertView.findViewById(R.id.txt_hc);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String str = getItem(position).toString();
        String value = App.getInstance().getData(str.split(";")[0]);
        if(!TextUtils.isEmpty(value)){
            viewHolder.textView_hc.setVisibility(View.VISIBLE);
        } else {
            viewHolder.textView_hc.setVisibility(View.GONE);
        }
        viewHolder.textView_name.setText(str.split(";")[1]);
        return convertView;
    }

    private final class ViewHolder {
        private TextView textView_name;
        private TextView textView_hc;
    }
}
