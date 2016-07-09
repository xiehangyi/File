package com.example.xhy.file.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xhy.file.R;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by change100 on 2016/7/2.
 */
public class DocAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<File> data;
    private LayoutInflater inflater;

    public DocAdapter(Context context, ArrayList<File> data) {
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if(convertView == null){
            //  没有可重复
            //  创建一个视图项
            convertView = inflater.inflate(R.layout.listview_doc_show,parent,false);

            //当前的ViewHolder需要保存视图结构
            holder = new ViewHolder(convertView);

            //视图绑定一个对象
            convertView.setTag(holder);
        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        //  绑定数据
        File file = data.get(position);

        //在视图中绑定数据
        holder.bindData(file);

        return convertView;
    }

    class ViewHolder {

        private ImageView logo;
        private TextView textView_doc_name;
        private TextView textView_doc_size;


        public ViewHolder(View v){

            logo = (ImageView) v.findViewById(R.id.imageView_doc_logo);
            textView_doc_name = (TextView) v.findViewById(R.id.textView_doc_name);
            textView_doc_size = (TextView) v.findViewById(R.id.textView_doc_size);


        }

        public void bindData(File f) {

            logo.setImageResource(R.drawable.ic_description_24dp1);
            textView_doc_name.setText(f.getName());
            textView_doc_size.setText(FormatFileSize(f.length()));
        }

        private String FormatFileSize(long size) {

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
    }
}
