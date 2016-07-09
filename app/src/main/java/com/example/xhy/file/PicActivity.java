package com.example.xhy.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xhy.file.adapter.PicAdapter;

/**
 * 获取图片和视频的缩略图
 * 这两个方法必须在2.2及以上版本使用，因为其中使用了ThumbnailUtils这个类
 */
public class PicActivity extends Activity {

    private static final String TAG = "PicActivity";
    private GridView gridView;
    private ArrayList<Bitmap> data = new ArrayList<>();

    private Map<Integer,File> map = new HashMap<>();


    private File file;
    private File[] files;
    private ContentResolver cr;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic);

        gridView = (GridView) findViewById(R.id.gridView);

        getPicture();

    }

    /**
     * 从媒体库获取图片相关信息
     */
    private void getPicture() {
        View v = getLayoutInflater().inflate(R.layout.progress_demo,null);
        TextView textView = (TextView) v.findViewById(R.id.textView_sortSize);
        textView.setText("加载中...");
        final AlertDialog dialog = new AlertDialog.Builder(PicActivity.this)
                .setView(v)
                .create();
        dialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {

                cr = getContentResolver();
                String[] projection = {MediaStore.Images.Media._ID,
                        MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.DATA};
                Cursor cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,projection
                        ,null,null,null);
                if(cursor == null) {
                    Toast.makeText(PicActivity.this, "没有找到图片文件！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(cursor.moveToFirst()) {
                    int _id;
                    int image_id;
                    String image_path;
                    int _idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                    int image_idColumn = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
                    int dataColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                    Log.v(TAG,String.valueOf(image_idColumn));
                    do{
                        _id = cursor.getInt(_idColumn);
                        image_id = cursor.getInt(image_idColumn);
                        image_path = cursor.getString(dataColumn);


                        String str = image_path;

                        data.add(getImageThumbnail(str, 80, 80));
                        map.put(data.size() - 1, new File(str));

                    } while (cursor.moveToNext());


                }

                cursor.close();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        gridView.setAdapter(new PicAdapter(PicActivity.this, data));
                        setListener();
                        dialog.dismiss();
                    }
                });
            }


        }).start();



    }


    /**
     * 设置监听，点击图片显示大图
     */
    private void setListener() {

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                File file = map.get(position);

                Dialog dialog = new Dialog(PicActivity.this, R.style.dialog);
                View v = View.inflate(PicActivity.this, R.layout.imageview_pic_show, null);
                ImageView imageView = (ImageView) v.findViewById(R.id.imageView_pic_show);
                try {
                    InputStream in = new FileInputStream(file);
                    Bitmap bitmap = BitmapFactory.decodeStream(in);
                    imageView.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                dialog.setContentView(v);
                dialog.show();


            }
        });
    }

    /**
     * 根据指定的图像路径和大小来获取缩略图
     * 此方法有两点好处：
     *     1. 使用较小的内存空间，第一次获取的bitmap实际上为null，只是为了读取宽度和高度，
     *        第二次读取的bitmap是根据比例压缩过的图像，第三次读取的bitmap是所要的缩略图。
     *     2. 缩略图对于原图像来讲没有拉伸，这里使用了2.2版本的新工具ThumbnailUtils，使
     *        用这个工具生成的图像不会被拉伸。
     * @param imagePath 图像的路径
     * @param width 指定输出图像的宽度
     * @param height 指定输出图像的高度
     * @return 生成的缩略图
     */
    private Bitmap getImageThumbnail(String imagePath, int width, int height) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高，注意此处的bitmap为null
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        options.inJustDecodeBounds = false; // 设为 false
        // 计算缩放比
        int h = options.outHeight;
        int w = options.outWidth;
        int beWidth = w / width;
        int beHeight = h / height;
        int be = 1;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }


}


