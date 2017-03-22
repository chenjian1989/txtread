package com.example.administrator.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.Util.PicUtil;
import com.example.administrator.entity.SearchEntity;
import com.example.administrator.txtread.R;

import java.util.List;

public class SearchAdapter extends BaseAdapter {

    /**
     * 上下文Context
     */
    private Context mCt;

    private List<SearchEntity> mList;

    private PicUtil mPicUtil;

    public SearchAdapter(Context context){
        mCt = context;
        mPicUtil = new PicUtil();
    }

    public void initData(List<SearchEntity> list){
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
    public View getView(int position, View convertView, ViewGroup parent) {
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
        final SearchEntity searchEntity = (SearchEntity) getItem(position);
        // 加载图片
        viewHolder.imageView.setTag(searchEntity.getFengmianUrl());
        if(searchEntity.getFengmianUrl().equals(".gif")){
            viewHolder.imageView.setImageResource(R.drawable.default_s);
        } else {
            mPicUtil.LoadImage(viewHolder.imageView, searchEntity.getFengmianUrl());
        }
        viewHolder.textView_name.setText(searchEntity.getName());
        viewHolder.textView_author.setText(searchEntity.getAuthor());
        viewHolder.textView_newest.setText(searchEntity.getNewest());
        return convertView;
    }

    private final class ViewHolder {

        private ImageView imageView;

        private TextView textView_name;

        private TextView textView_author;

        private TextView textView_newest;
    }
}
