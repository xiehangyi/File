package com.example.xhy.file;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {

    long SDkSize;
    long SDzSize;


    // 当前打开的文件
    private File file;
    private File[] files;

    //  视图
    ListView listView;
    TextView textView_path;
    ProgressBar progressBar;

    // 文件的绝对路径
    private String abpath;

    //  数据
    ArrayList<File> data;

    //适配器
    FileAdapter adapter;
    Stack<File> stackfiles;


    private boolean isfinish = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        textView_path = (TextView) findViewById(R.id.textView_path);

        initView();
        setProgressPercent();

    }

    private void setProgressPercent() {

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        TextView textView_avi = (TextView) findViewById(R.id.textView_avi);
        TextView textView_tol = (TextView) findViewById(R.id.textView_tol);
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            StatFs fs = new StatFs(Environment.getExternalStorageDirectory().getPath());

            SDzSize = fs.getTotalBytes();
            int zsize = (int) (SDzSize / 1024 / 1024 / 1024);
            progressBar.setMax(zsize);

            SDkSize = fs.getAvailableBytes();
            int ksize = (int) (SDkSize / 1024 / 1024 / 1024);
            progressBar.setProgress(ksize);

            textView_avi.setText("可用:" + String.valueOf(ksize) + "GB");
            textView_tol.setText("总共:" + String.valueOf(zsize) + "GB");

        } else {
            textView_avi.setText("内存异常，无法显示数据");
        }

    }

//    private void replaceBar(){
//
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
////        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
////        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_24dp);
//
//
//    }


    private void initView() {
        //1.
        stackfiles = new Stack<>();
        data = new ArrayList<>();
        //2.
        //  加载SD卡中的文件
        //  外部存储文件
        file = Environment.getExternalStorageDirectory();
        loadData(file);
        makeAdapter();
    }

    private void makeAdapter() {
        adapter = new FileAdapter(this, data);               //3.
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                file = data.get(position);
                if (!file.isFile()) {
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

        abpath = "当前位置：" + file.getAbsolutePath();
        if (!file.getName().equals("0")) {

            stackfiles.push(file.getParentFile());

        }

        textView_path.setText(abpath);


        data.clear();
        //  获得目录中的所有文件
        files = file.listFiles();

        for (File f : files) {
            data.add(f);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.actionbar_menu, menu);

        MenuItem item = menu.findItem(R.id.item_sear);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint("请输入文件名");
        // 设置确认
        searchView.setSubmitButtonEnabled(true);

//        searchView.setSuggestionsAdapter();
//        searchView.setOnSuggestionListener();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Toast.makeText(MainActivity.this, "搜索: " + query, Toast.LENGTH_SHORT).show();

                // 显示等待框
                View v = getLayoutInflater().inflate(R.layout.progress_demo, null);
                Dialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setView(v)
                        .create();
                dialog.show();

                stackfiles.push(files[0].getParentFile());
                search_query(query);
                dialog.dismiss();


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void search_query(String query) {

        char[] c1 = query.toCharArray();
        char[] c2;
        File[] file_search = files;
        ArrayList<File> arrayList = new ArrayList<>();
        File f;
        Boolean b;

        for (int j = 0; j < file_search.length; j++) {

            f = file_search[j];
            c2 = f.getName().toCharArray();
            b = false;
            for (int i = 0; i < c1.length; i++) {

                for (int m = 0; m < c2.length; ) {
                    if(c1[i] == c2[m]){
                        b = true;
                        break;
                    }
                    m++;
                    if(m == c2.length){
                        b = false;
                    }
                }
                if(!b){
                    break;
                }
            }
            Log.v("aa",f.getName()+String.valueOf(b));
            if(b){
                arrayList.add(f);
            }
        }

        data.clear();
        for (File file:arrayList){
            data.add(file);
        }
        adapter.notifyDataSetChanged();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.item_back:
                doBack();
                break;
            case R.id.item_add:
                doAdd();
                break;
            case R.id.action_sort_size:
                Toast.makeText(MainActivity.this, "OK", Toast.LENGTH_SHORT).show();
                try {
                    doSort_size();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.action_sort_word:
                doSort_word();
                break;
            case R.id.action_sort_length:
                doSort_length();
                break;
            case R.id.action_sort_date:
                showToast("该功能暂时还未实现，敬请期待");
                break;
            case R.id.action_sort_type:
                showToast("该功能暂时还未实现，敬请期待");
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 按文件名长度排序
     */
    private void doSort_length() {

        File[] file_sort = files;
        File[] file_sort_lat = new File[files.length];
        for(int i = 0; i < file_sort.length; i++){

            file_sort_lat[i] = file_sort[i];
            for(int j = i; j > 0; j--) {

                if(file_sort_lat[j].getName().length() < file_sort_lat[j-1].getName().length()) {

                    File temp = file_sort_lat[j];
                    file_sort_lat[j] = file_sort_lat[j-1];
                    file_sort_lat[j-1] = temp;
                } else {
                    break;
                }

            }
        }

        data.clear();
        for(File file:file_sort_lat) {
            data.add(file);
        }
        adapter.notifyDataSetChanged();

    }

    /**
     * 按首字母顺序排序
     */
    private void doSort_word() {

        // 得到该目录下所有文件
        File[] files_sort = files;
        File[] files_sort_lat = new File[files.length];
        String[] words = new String[files.length];

        for (int i = 0; i < files_sort.length; i++) {

            words[i] = files_sort[i].getName();
            files_sort_lat[i] = files_sort[i];

            if (i > 0) {
                for (int j = i; j > 0; j--) {
                    if (String_Sort(words[j], files_sort_lat[j - 1].getName()) == 1) {

                        String temp = words[j];
                        words[j] = words[j - 1];
                        words[j - 1] = temp;

                        File file = files_sort_lat[j];
                        files_sort_lat[j] = files_sort_lat[j - 1];
                        files_sort_lat[j - 1] = file;

                    } else {
                        break;
                    }
                }
            }
        }
        data.clear();
        for (File file : files_sort_lat) {
            data.add(file);
        }
        adapter.notifyDataSetChanged();

    }

    private int String_Sort(String str1, String str2) {

        char[] c1 = str1.toCharArray();
        char[] c2 = str2.toCharArray();


        if ((64 < c1[0] && c1[0] < 91) || (96 < c1[0] && c1[0] < 123)) {
            if ((64 < c1[0] && c1[0] < 91) || (96 < c1[0] && c1[0] < 123)) {
                if (c1[0] < c2[0]) {
                    return 1;
                } else {
                    return 2;
                }
            } else {
                return 1;
            }
        } else {
            if ((64 < c1[0] && c1[0] < 91) || (96 < c1[0] && c1[0] < 123)) {
                return 2;
            } else {
                return 1;
            }
        }


    }


    /**
     * 按大小排序
     *
     * @throws Exception
     */
    private void doSort_size() throws Exception {

        // 得到该目录下所有文件
        File[] files_sort = files;
        File[] files_sort_lat = new File[files.length];
        long[] longs = new long[files.length];
        long sizeFile;
        for (int i = 0; i < files_sort.length; i++) {

            if (files_sort[i].isFile()) {
                sizeFile = getFileSize(files_sort[i]);
            } else {
                sizeFile = getFileSizes(files_sort[i]);
            }
            longs[i] = sizeFile;

            files_sort_lat[i] = files_sort[i];

            if (i > 0) {
                for (int j = i; j > 0; j--) {
                    if (longs[j] > longs[j - 1]) {
                        long temp = longs[j];
                        longs[j] = longs[j - 1];
                        longs[j - 1] = temp;

                        File file = files_sort_lat[j];
                        files_sort_lat[j] = files_sort_lat[j - 1];
                        files_sort_lat[j - 1] = file;

                    } else {
                        break;
                    }
                }
            }
        }

        data.clear();
        for (File file : files_sort_lat) {
            data.add(file);
        }
        adapter.notifyDataSetChanged();

    }

    private long getFileSize(File file) throws Exception {

        long size = 0;
        if (file.exists()) {

            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
            fis.close();
        }

        return size;
    }

    private long getFileSizes(File file) throws Exception {

        long size = 0;
        if (file.exists()) {

            File[] files_open = file.listFiles();
            for (File f : files_open) {
                if (f.isFile()) {
                    size += getFileSize(f);
                } else {
                    size += getFileSizes(f);
                }
            }
        }

        return size;
    }


    /**
     * 添加文件
     */
    private void doAdd() {

        View v = getLayoutInflater().inflate(R.layout.rename_demo, null);
        final EditText editText = (EditText) v.findViewById(R.id.editText);
        Button btn_ok = (Button) v.findViewById(R.id.btn_ok);
        Button btn_cancel = (Button) v.findViewById(R.id.btn_cancel);
        final Dialog dialog = new AlertDialog.Builder(this)
                .setTitle("新建文件夹")
                .setView(v)
                .create();
        dialog.show();
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editText.getText().toString();
                name = file.getAbsolutePath() + "/" + name;
                Log.v("filename", name);
                File newFile = new File(name);
                newFile.mkdir();
                loadData(file);
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    /**
     * 返回上一级
     */
    public void doBack() {


        if (stackfiles.size() > 0) {
            File filepar = stackfiles.pop();
            file = filepar;
            loadData(filepar);
            adapter.notifyDataSetChanged();
        } else if (stackfiles.size() == 0) {
            Toast.makeText(MainActivity.this, "已经是根目录，无法回到上一级", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (isfinish) {
            finish();
        } else {
            Toast.makeText(MainActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    isfinish = true;
                    try {
                        Thread.sleep(2000);
                        isfinish = false;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }).start();
        }

    }

    private void showToast(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
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