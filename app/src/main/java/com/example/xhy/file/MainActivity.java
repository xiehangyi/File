package com.example.xhy.file;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //  视图
    ListView listView;

    //  数据
    ArrayList<File> data;

    //适配器
    FileAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //  加载布局
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {

        listView = (ListView) findViewById(R.id.listView);  //1.

       // data = new ArrayList<>();                           //2.
        //  加载SD卡中的文件

        loadData();

        adapter = new FileAdapter(this,data);               //3.

        listView.setAdapter(adapter);                       //4.
    }

    private void loadData() {

        data = new ArrayList<>();

        //  外部存储文件
        File sdPath = Environment.getExternalStorageDirectory();

        //  获得目录中的所有文件
        File[] files = sdPath.listFiles();

        for(File f : files) {

            data.add(f);

        }

//        if(!sdPath.exists()){
//            sdPath.createNewFile();
//            sdPath.mkdir();
//            sdPath.mkdirs();
//
//        }


    }

    class MyFilter implements FileFilter{

        /**
         *
         * @param f
         * @return true 则这个文件出现在数组中，否则就排除了
         */
        @Override
        public boolean accept(File f) {
            return f.isFile();
        }
    }

    class MyFilter2 implements FilenameFilter{

        @Override
        public boolean accept(File dir, String filename) {
            return false;
        }
    }
}
