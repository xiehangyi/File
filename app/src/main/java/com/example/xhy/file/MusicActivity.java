package com.example.xhy.file;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.xhy.file.adapter.MusicAdapter;
import com.example.xhy.file.entity.Music;

import java.io.File;
import java.util.ArrayList;

public class MusicActivity extends AppCompatActivity {

    private ListView listView_music_show;
    private File file;
    private File[] files;
    private ArrayList<Music> data = new ArrayList<>();

    private ContentResolver cr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        listView_music_show = (ListView) findViewById(R.id.listView_music_show);

        getMusic();


    }

    /**
     * 从媒体库获取音乐相关信息
     */
    private void getMusic() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                cr = getContentResolver();
                String[] projection = {MediaStore.Audio.Media._ID,
                        MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.DATA
                        ,MediaStore.Audio.Media.SIZE,MediaStore.Audio.Media.DURATION};
                // 获取游标，使用游标查询媒体库数据
                Cursor cursor = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,projection,null,null
                        ,MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

                if(cursor == null) {
                    Toast.makeText(MusicActivity.this, "没有找到音乐文件！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(cursor.moveToFirst()) {

                    int _id;
                    String display_name;
                    String music_path;
                    String _size;
                    String _duration;

                    int _idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
                    int music_idColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME);
                    int dataColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
                    int sizeColumn = cursor.getColumnIndex(MediaStore.Audio.Media.SIZE);
                    int durationColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);

                    do {
                        _id = cursor.getInt(_idColumn);
                        display_name = cursor.getString(music_idColumn);
                        music_path = cursor.getString(dataColumn);
                        _size = cursor.getString(sizeColumn);
                        _duration = cursor.getString(durationColumn);

                        Music music = new Music(display_name,music_path,_size,_duration);
                        data.add(music);


                    } while (cursor.moveToNext());

                }
                cursor.close();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listView_music_show.setAdapter(new MusicAdapter(MusicActivity.this,data));
                        setListener();
                    }
                });


            }
        }).start();


    }

    private void demo() {
        // 获取文件地址
        String imagePath = Environment.getExternalStorageDirectory()
                .getAbsolutePath()
                + File.separator
                + "Download"
                + File.separator
                + "other"
                ;



        Log.v("ff",imagePath);
        file = new File(imagePath);
        files = file.listFiles();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.v("ee","开始。。。");
                if(files!=null) {
                    for (File f : files) {
                        if (f.isFile()) {
                            String name = f.getName();
                            Log.v("cc", name);
                            int a = name.lastIndexOf(".");
                            Log.v("bb", String.valueOf(a));
                            String s = name.substring(a);
                            Log.v("aa", s);
                            if (s.equals(".mp3")) {
                                //data.add(f);
                            }
                        }
                    }
                }else {Log.v("dd","文件为空");}
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //listView_music_show.setAdapter(new MusicAdapter(MusicActivity.this,data));
                        setListener();
                    }
                });
            }
        }).start();

    }

    private void setListener() {

        // 设置打开文件的方法
        listView_music_show.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File file = new File(data.get(position).getData());
                // 打开文件
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setAction(android.content.Intent.ACTION_VIEW);
                intent.addCategory("android.intent.category.DEFAULT");
                // *表示列出所有打开文件的选项
                intent.setDataAndType(Uri.fromFile(file), "*/*");
                startActivity(intent);
            }
        });
    }
}
