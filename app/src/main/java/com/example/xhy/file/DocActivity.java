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

import com.example.xhy.file.adapter.DocAdapter;
import com.example.xhy.file.adapter.MusicAdapter;

import java.io.File;
import java.util.ArrayList;

public class DocActivity extends AppCompatActivity {

    private ListView listView_doc;
    private File file;
    private File[] files;
    private ArrayList<File> data = new ArrayList<>();

    private ContentResolver cr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc);

        listView_doc = (ListView) findViewById(R.id.listView_doc);

        demo();

        //getDoc();

    }

    private void getDoc() {

        cr = getContentResolver();
//        Cursor cursor = cr.query(MediaStore.Files.getContentUri())
    }

    private void demo(){
        // 获取文件地址
        String imagePath = Environment.getExternalStorageDirectory()
                .getAbsolutePath()
                + File.separator
                + "tencent"
                + File.separator
                + "QQfile_recv"
                ;



        Log.v("ff", imagePath);
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
                            if (s.equals("xls") || s.equals(".txt") || s.equals(".doc") || s.equals(".wps") || s.equals(".docx")  ) {
                                data.add(f);
                            }
                        }
                    }
                }else {Log.v("dd","文件为空");}
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listView_doc.setAdapter(new DocAdapter(DocActivity.this,data));
                        setListener();
                    }
                });
            }
        }).start();
    }

    private void setListener() {

        listView_doc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File file = data.get(position);
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
