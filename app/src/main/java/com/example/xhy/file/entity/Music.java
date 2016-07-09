package com.example.xhy.file.entity;

/**
 * 音频封装类
 * Created by change100 on 2016/7/5.
 */
public class Music {

    private String name;
    private String data;
    private String size;
    private String time;
    private int logo;

    public Music() {
    }

    public Music(String name, String data,String size, String time) {
        this.name = name;
        this.size = size;
        this.time = time;
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getLogo() {
        return logo;
    }

    public void setLogo(int logo) {
        this.logo = logo;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
