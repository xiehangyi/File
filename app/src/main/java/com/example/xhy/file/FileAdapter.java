package com.example.xhy.file;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.content.DialogInterface;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * FileAdapter 显示文件列表
 */
public class FileAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<File> data;

    private LayoutInflater layoutInflater;
    private static HashMap<String ,String> map;

    /**
     * 创建文件适配器(数据和视图间的桥)
     *
     * @param context
     * @param data
     */
    public FileAdapter(Context context, ArrayList<File> data) {

        this.context = context;
        this.data = data;
        map = new HashMap<>();

        layoutInflater = LayoutInflater.from(context);
    }

    /**
     * 获得总数
     *
     * @return 数据的总数
     */
    @Override
    public int getCount() {
        return data.size();
    }

    /**
     * 获得特定位置的数据项
     *
     * @param position int 位置
     * @return File 数据项
     */
    @Override
    public File getItem(int position) {
        return data.get(position);
    }

    /**
     * 获得特定位置的数据编号
     *
     * @param position
     * @return
     */
    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * 获得特定位置的视图项
     *
     * @param position    int 位置
     * @param convertView View 可重用的视图项
     * @param parent      ListView 父元素
     * @return View 加载了数据的视图项
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {

            //  加载XML布局，实例化，并且返回视图
            convertView = layoutInflater.inflate(R.layout.file_item, parent, false);

            //  获得【视图项】的结构
            holder = new ViewHolder(convertView, position);

            //  为视图绑定一个数据（结构）
            convertView.setTag(holder);
        } else {
            //重可复用的视图项中获得结构
            holder = (ViewHolder) convertView.getTag();
        }

        try {
            holder.bindData(data.get(position));
        } catch (Exception e) {
            e.printStackTrace();
        }


        return convertView;
    }


    /**
     * 适配器视图中【视图项】的结构
     */
    class ViewHolder {

        /**
         * 图标：标识文件类型
         */
        private ImageView icon;
        private TextView fileName;
        private TextView fileInfo;
        private ImageButton more;
        private TextView filesize;

        private int position;
        File file;

        PopMenuListener listener;


        public ViewHolder(View v, int position) {


            icon = (ImageView) v.findViewById(R.id.imageView);
            fileName = (TextView) v.findViewById(R.id.textView_name);
            fileInfo = (TextView) v.findViewById(R.id.textView_info);
            more = (ImageButton) v.findViewById(R.id.imageButton);
            filesize = (TextView) v.findViewById(R.id.textView_size);

            this.position = position;


            listener = new PopMenuListener(v, position);


            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //  创建弹出菜单
                    PopupMenu menu = new PopupMenu(context, v);

                    //  加载 XML 文件
                    menu.inflate(R.menu.file_item);

                    //注册监听器
                    menu.setOnMenuItemClickListener(listener);

                    //  显示
                    menu.show();

                }
            });

        }

        public void bindData(File f) throws Exception {

            file = f;
            icon.setImageResource(f.isFile()
                    ? R.drawable.ic_attachment_24dp : R.drawable.ic_folder_open_24dp);
            fileName.setText(f.getName());

            int size = (f.list() == null) ? 0 : f.list().length;
            fileInfo.setText(f.isFile()
                    ? String.format("文件：%d 字节", getFileSize(f))
                    : String.format("目录：%d 文件", size));


            if(map.get(f.getName())==null) {
                long fileSize;
                String filesizeStr = "";
                if (!f.isFile()) {

                    fileSize = getFileSizes(f);
                    filesizeStr = FormatFileSize(fileSize);
                    filesize.setText(filesizeStr);
                    map.put(f.getName(), filesizeStr);
                }
            }else {
                filesize.setText(map.get(f.getName()));
            }

            listener.setFile(f);

        }

        /**
         * 用于保存数据
         */
        class setData {

            private String[] saveData;

            public setData() {

                saveData = new String[data.size()];

            }
        }


//        private void re(){
//
//            file.delete();
////            showToast("删除");
//            data.remove(position);
//            Log.v("aa",String.valueOf(position));
//            notifyDataSetChanged();
//        }

        private String FormatFileSize(long size) {

            DecimalFormat decimalFormat = new DecimalFormat("#.00");
            String fileSizeStr = "";
            String wrongSize = "0B";
            if (size == 0) {
                return wrongSize;
            }
            if (size < 1024) {
                fileSizeStr = decimalFormat.format((double) size) + "B";
            } else if (size < 1048576) {
                fileSizeStr = decimalFormat.format((double) size / 1024) + "KB";
            } else if (size < 1073741824) {
                fileSizeStr = decimalFormat.format((double) size / 1048576) + "MB";
            } else {
                fileSizeStr = decimalFormat.format((double) size / 1073741824) + "GB";
            }


            return fileSizeStr;
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


    }


    /**
     * 菜单监听器
     */
    class PopMenuListener implements PopupMenu.OnMenuItemClickListener {

        /**
         * 操作文件
         */
        private File file;
        private View v;
        private int position;

        public PopMenuListener(View v, int position) {
            this.v = v;
            this.position = position;
        }

        /**
         * 菜单项点击
         *
         * @param item 事件源
         * @return true  事件处理完毕
         */
        @Override
        public boolean onMenuItemClick(MenuItem item) {

            switch (item.getItemId()) {
                case R.id.action_copy:
                    doCopy();
                    break;
                case R.id.action_remove:
                    doRemove();
                    break;
                case R.id.action_rename:
                    doRename();
                    break;

            }
            return true;
        }

        private void doRename() {

            View view = layoutInflater.inflate(R.layout.rename_demo, null);
            final EditText editText = (EditText) view.findViewById(R.id.editText);
            Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
            Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
            final Dialog dialog = new AlertDialog.Builder(context)
                    .setTitle("重命名")
                    .setView(view)
                    .create();
            dialog.show();

            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String newName = editText.getText().toString();
                    File newFile = new File(file.getParentFile().getAbsolutePath() + "/" + newName);
                    file.renameTo(newFile);
                    dialog.dismiss();
                    data.remove(position);
                    Log.v("aa", String.valueOf(position));
                    data.add(newFile);
                    notifyDataSetChanged();
                }
            });

            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });


        }


        private void doRemove() {

            file.delete();
            showToast("删除");
            data.remove(position);
            Log.v("aa", String.valueOf(position));
            notifyDataSetChanged();

        }

        private void doCopy() {
            showToast("该功能暂时还未实现，敬请期待！");

        }

        private void showToast(String msg) {
            Toast.makeText(context, msg , Toast.LENGTH_SHORT).show();
        }

        public void setFile(File file) {

            this.file = file;

        }
    }


}
