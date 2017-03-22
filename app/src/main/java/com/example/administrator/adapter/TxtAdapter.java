package com.example.administrator.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.Util.PicUtil;
import com.example.administrator.entity.HomeTxtEntity;
import com.example.administrator.txtread.R;

import java.util.List;

public class TxtAdapter extends BaseAdapter {

    /**
     * 上下文Context
     */
    private Context mCt;

    private List<HomeTxtEntity> mList;

    private PicUtil mPicUtil;

    public TxtAdapter(Context context){
        mCt = context;
        mPicUtil = new PicUtil();
    }

    public void initData(List<HomeTxtEntity> list){
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
            convertView = LayoutInflater.from(mCt).inflate(R.layout.item_txt, null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image_fengmian);
            viewHolder.textView_name = (TextView) convertView.findViewById(R.id.txt_name);
            viewHolder.textView_author = (TextView) convertView.findViewById(R.id.txt_author);
            viewHolder.textView_newest = (TextView) convertView.findViewById(R.id.txt_newest);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final HomeTxtEntity homeTxtEntity = (HomeTxtEntity) getItem(position);
        // 加载图片
        viewHolder.imageView.setTag(homeTxtEntity.getFengmianUrl());
        if(homeTxtEntity.getFengmianUrl().equals(".gif")){
            viewHolder.imageView.setImageResource(R.drawable.default_s);
        } else {
            mPicUtil.LoadImage(viewHolder.imageView, homeTxtEntity.getFengmianUrl());
        }

        viewHolder.textView_name.setText(homeTxtEntity.getName());
        viewHolder.textView_author.setText(homeTxtEntity.getAuthor());
        viewHolder.textView_newest.setText(homeTxtEntity.getNewest());
//        LogUtil.e("getView()-homeTxtEntity: " + homeTxtEntity.getFengmianUrl());
//        LogUtil.e("getView()-homeTxtEntity: " + homeTxtEntity.getNewest());
        return convertView;

    }

    private final class ViewHolder {

        private ImageView imageView;

        private TextView textView_name;

        private TextView textView_author;

        private TextView textView_newest;
    }
}
