package com.example.xhy.file.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xhy.file.R;
import com.example.xhy.file.entity.Music;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by change100 on 2016/7/2.
 */
public class MusicAdapter extends BaseAdapter{

    private static final String TAG = "MusicAdapter";
    private Context context;
    private ArrayList<Music> data;
    private LayoutInflater inflater;

    public MusicAdapter(Context context, ArrayList<Music> data) {
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
        return 0;
    }

    /**
     * 写适配方法，显示音频文件的相关信息
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if(convertView == null){
            //  没有可重复
            //  创建一个视图项
            convertView = inflater.inflate(R.layout.listview_music_show,parent,false);

            //当前的ViewHolder需要保存视图结构
            holder = new ViewHolder(convertView);

            //视图绑定一个对象
            convertView.setTag(holder);
        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        //  绑定数据
        Music music = data.get(position);

        //在视图中绑定数据
        holder.bindData(music);

        return convertView;
    }

    class ViewHolder {

        private ImageView logo;
        private TextView textView_music_name;
        private TextView textView_music_size;
        private TextView textView_music_duration;


        public ViewHolder(View v){

            logo = (ImageView) v.findViewById(R.id.imageView_music_logo);
            textView_music_name = (TextView) v.findViewById(R.id.textView_music_name);
            textView_music_size = (TextView) v.findViewById(R.id.textView_music_size);
            textView_music_duration = (TextView) v.findViewById(R.id.textView_music_duration);


        }

        public void bindData(Music m) {

            logo.setImageResource(R.drawable.music);
            textView_music_name.setText(m.getName());

            textView_music_size.setText(FormatSize(Long.parseLong(m.getSize())));
            textView_music_duration.setText(FormatDuration(Long.parseLong(m.getTime())));
        }

        private String FormatSize(long size) {

            DecimalFormat decimalFormat = new DecimalFormat("#.00");
            String fileSizeStr = "";
            String wrongSize = "0B";
            if (size == 0) {
                return wrongSize;
            }
            if (size < 1024) {
                fileSizeStr = decimalFormat.format((double) size) + "B";
            } else if (size < 1048576) {
                fileSizeStr = decimalFormat.format((double) size / 1024) + "KB";
            } else if (size < 1073741824) {
                fileSizeStr = decimalFormat.format((double) size / 1048576) + "MB";
            } else {
                fileSizeStr = decimalFormat.format((double) size / 1073741824) + "GB";
            }


            return fileSizeStr;
        }


        private String FormatDuration(long size) {

            Log.v(TAG,String.valueOf(size));

            String fileSizeStr = "";
            String wrongSize = "0";
            if (size == 0) {
                return wrongSize;
            }
            if (size < 1000) {
                fileSizeStr = (int) size + "毫秒";
            } else if (size < 60000) {
                fileSizeStr = (int) (size / 1000) + "秒";
            } else if (size < 3600000) {
                fileSizeStr = ((int) size / 60000) + "分"+(((int)size%60000)/1000)+"秒";
            } else {

                StringBuilder sb = new StringBuilder();
                fileSizeStr = ((int) size / 3600000) + "小时";
                sb.append(fileSizeStr);
                size = size%3600000;
                fileSizeStr = ((int) size / 60000) + "分"+(((int)size%60000)/1000)+"秒";
                sb.append(fileSizeStr);
                fileSizeStr = sb.toString();

            }


            return fileSizeStr;

        }
    }




}
