package com.example.xhy.file;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class TypeActivity extends AppCompatActivity {

    private ImageView imageView_pic;
    private ImageView imageView_video;
    private ImageView imageView_music;
    private ImageView imageView_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);

        initView();
        setListener();
    }



    /**
     * 初始化视图
     */
    private void initView() {

        imageView_pic = (ImageView) findViewById(R.id.imageView_pic);
        imageView_music = (ImageView) findViewById(R.id.imageView_music);
        imageView_video = (ImageView) findViewById(R.id.imageView_video);
        imageView_txt = (ImageView) findViewById(R.id.imageView_txt);
    }

    /**
     * 设置监听事件
     */
    private void setListener() {

        imageView_pic.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(TypeActivity.this,PicActivity.class);
                        startActivity(intent);
                    }
                }
        );

        imageView_music.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(TypeActivity.this,MusicActivity.class);
                        startActivity(intent);
                    }
                }
        );

        imageView_txt.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(TypeActivity.this,DocActivity.class);
                        startActivity(intent);
                    }
                }
        );

        imageView_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TypeActivity.this,VideoActivity.class);
                startActivity(intent);
            }
        });

    }
}
