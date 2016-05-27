package com.example.xhy.file;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Stack;


public class MainActivity extends AppCompatActivity {

    //  视图
    ListView listView;

    //  数据
    ArrayList<File> data;

    //适配器
    FileAdapter adapter;
    Stack<File> stackfiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //  加载布局
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);

        initView();


    }


    private void initView() {
        //1.
        stackfiles = new Stack<>();
        data = new ArrayList<>();
        //2.
        //  加载SD卡中的文件
        //  外部存储文件
        File sdPath = Environment.getExternalStorageDirectory();
        loadData(sdPath);
        adapter = new FileAdapter(this, data);               //3.
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            File file;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                file = data.get(position);
                if(!file.isFile()) {
                    loadData(file);
                    adapter.notifyDataSetChanged();
                } else {
                    // 打开文件
                    Intent intent = new Intent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setAction(android.content.Intent.ACTION_VIEW);
                    // *表示列出所有打开文件的选项
                    intent.setDataAndType(Uri.fromFile(file), "*/*");
                    startActivity(intent);
                }
            }
        });


    }

    private void loadData(File file) {

        stackfiles.push(file.getParentFile());

        data.clear();
        //  获得目录中的所有文件
        File[] files = file.listFiles();

        for (File f : files) {
            data.add(f);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.actionbar_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.item_back){

            if(stackfiles!=null) {
                File file = stackfiles.pop();

                loadData(file);
                adapter.notifyDataSetChanged();

            }

        }
        return super.onOptionsItemSelected(item);
    }

    class MyFilter implements FileFilter {

        /**
         * @param f
         * @return true 则这个文件出现在数组中，否则就排除了
         */
        @Override
        public boolean accept(File f) {
            return f.isFile();
        }
    }

    class MyFilter2 implements FilenameFilter {

        @Override
        public boolean accept(File dir, String filename) {
            return false;
        }
    }


}