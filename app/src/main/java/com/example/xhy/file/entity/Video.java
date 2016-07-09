package com.example.xhy.file.entity;

import android.graphics.Bitmap;

/**
 * 视频封装类
 * Created by change100 on 2016/7/6.
 */
public class Video {

    private Bitmap bitmap;
    private String name;
    private String data;

    public Video() {
    }

    public Video(Bitmap bitmap, String name,String data) {
        this.bitmap = bitmap;
        this.name = name;
        this.data = data;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
