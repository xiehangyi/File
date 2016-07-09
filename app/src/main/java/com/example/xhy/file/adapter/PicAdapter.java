package com.example.xhy.file.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by change100 on 2016/7/2.
 */
public class PicAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<Bitmap> data;

    public PicAdapter(Context context, ArrayList<Bitmap> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 写适配方法，显示图片的缩略图
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(150, 150));//设置ImageView对象布局
            imageView.setAdjustViewBounds(false);//设置边界对齐
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);//设置刻度的类型
            imageView.setPadding(8, 8, 8, 8);//设置间距
        }
        else {
            imageView = (ImageView) convertView;
        }
        imageView.setImageBitmap(data.get(position));
        return imageView;
    }
}
