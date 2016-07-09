package com.example.xhy.file.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xhy.file.R;
import com.example.xhy.file.entity.Music;
import com.example.xhy.file.entity.Video;

import java.util.ArrayList;

/**
 * Created by change100 on 2016/7/3.
 */
public class VideoAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Video> data;
    private LayoutInflater inflater;

    public VideoAdapter(Context context, ArrayList<Video> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
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
     * 写适配方法，显示视频文件信息
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            //  没有可重复
            //  创建一个视图项
            convertView = inflater.inflate(R.layout.imageview_video_show, parent, false);

            //当前的ViewHolder需要保存视图结构
            holder = new ViewHolder(convertView);

            //视图绑定一个对象
            convertView.setTag(holder);
        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        //  绑定数据
        Video video = data.get(position);

        //在视图中绑定数据
        holder.bindData(video);

        return convertView;
    }


    class ViewHolder {

        private ImageView video;
        private TextView textView_video_name;



        public ViewHolder(View v) {

            video = (ImageView) v.findViewById(R.id.imageView_video_show);
            textView_video_name = (TextView) v.findViewById(R.id.textView_video_name);

        }

        public void bindData(Video v) {

            video.setImageBitmap(v.getBitmap());
            textView_video_name.setText(v.getName());

        }
    }
}
