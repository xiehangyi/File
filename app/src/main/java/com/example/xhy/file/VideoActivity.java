package com.example.xhy.file;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.xhy.file.adapter.PicAdapter;
import com.example.xhy.file.adapter.VideoAdapter;
import com.example.xhy.file.entity.Video;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VideoActivity extends AppCompatActivity {

    private static final String TAG = "VideoActivity";
    private ListView listView_video;
    private ArrayList<Video> data = new ArrayList<>();
    private Map<Integer,File> map = new HashMap<>();

    private ArrayList<String> path = new ArrayList<>();
    private ArrayList<HashMap<String,String>> list  = new ArrayList<>();
    private ContentResolver cr;

    private File[] files;
    private File file;


    private String[] videoType={".avi",".vcd",".SVCD",".DVD",".MPG",".wmv",
            ".ASF",".RM",".RMVB",".FLV",".F4V",".MOV",".QT",".mp4",".MPEG4",".3GP",".3G2",
            ".MKV",".TS",".TP",".MTS"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        listView_video = (ListView) findViewById(R.id.listView_video);

        getVideo();
    }

    /**
     * 从媒体库获取视频相关信息
     */
    private void getVideo() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                cr = getContentResolver();
                String[] projection = {MediaStore.Video.Media._ID,
                        MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media.DATA};
                Cursor cursor = cr.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,projection
                        ,null,null,null);
                int a = cursor.getCount();
                Log.v("aa",String.valueOf(a));
                int r = 0;
                if(cursor == null) {
                    Toast.makeText(VideoActivity.this,"没有找到视频文件！",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(cursor.moveToFirst()) {
                    int _id;
                    String display_name;
                    String video_path;
                    int _idColumn = cursor.getColumnIndex(MediaStore.Video.Media._ID);
                    int video_name = cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME);
                    int dataColumn = cursor.getColumnIndex(MediaStore.Video.Media.DATA);
                    Log.v(TAG,String.valueOf(video_name));

                    do{
                        _id = cursor.getInt(_idColumn);
                        display_name = cursor.getString(video_name);
                        video_path = cursor.getString(dataColumn);

                        Video video = new Video((getVideoThumbnail(video_path, 150, 150, MediaStore.Video.Thumbnails.MINI_KIND))
                        ,display_name,video_path);


                        data.add(video);
                    } while (cursor.moveToNext());

                    Log.v(TAG,String.valueOf(r));
                }




                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listView_video.setAdapter(new VideoAdapter(VideoActivity.this, data));
                        setListener();
                    }
                });
            }


        }).start();

    }




    private void setListener() {

        listView_video.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

    /**
     * 获取视频的缩略图
     * 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
     * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
     * @param videoPath 视频的路径
     * @param width 指定输出视频缩略图的宽度
     * @param height 指定输出视频缩略图的高度
     * @param kind 参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。
     *            其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
     * @return 指定大小的视频缩略图
     */
    private Bitmap getVideoThumbnail(String videoPath, int width, int height,
                                     int kind) {
        Bitmap bitmap = null;
        // 获取视频的缩略图
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        Log.v(TAG, "w" + bitmap.getWidth());
        Log.v(TAG, "h" + bitmap.getHeight());
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }
}
