package com.example.xhy.file;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.content.DialogInterface;

import java.io.File;
import java.util.ArrayList;

/**
 * FileAdapter 显示文件列表
 */
public class FileAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<File> data;

    private LayoutInflater layoutInflater;

    /**
     * 创建文件适配器(数据和视图间的桥)
     *
     * @param context
     * @param data
     */
    public FileAdapter(Context context, ArrayList<File> data) {

        this.context = context;
        this.data = data;

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
            holder = new ViewHolder(convertView);

            //  为视图绑定一个数据（结构）
            convertView.setTag(holder);
        } else {
            //重可复用的视图项中获得结构
            holder = (ViewHolder) convertView.getTag();
        }

        holder.bindData(data.get(position));


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

        PopMenuListener listener;

        public ViewHolder(View v) {

            icon = (ImageView) v.findViewById(R.id.imageView);
            fileName = (TextView) v.findViewById(R.id.textView_name);
            fileInfo = (TextView) v.findViewById(R.id.textView_info);
            more = (ImageButton) v.findViewById(R.id.imageButton);


            listener = new PopMenuListener(v);


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

        public void bindData(File f) {

            icon.setImageResource(f.isFile()
                    ? R.drawable.ic_attachment_24dp : R.drawable.ic_folder_open_24dp);
            fileName.setText(f.getName());

            int size = (f.list() == null) ? 0 : f.list().length;
            fileInfo.setText(f.isFile()
                    ? String.format("文件：%d 字节", f.length())
                    : String.format("目录：%d 文件",size));

            listener.setFile(f);


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

            public PopMenuListener(View v){
                this.v = v;
            }

            /**
             * 菜单项点击
             *
             * @param item  事件源
             * @return true  事件处理完毕
             */
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch(item.getItemId()){
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
                showToast("重命名");
               // showRenameDialog(v);
            }



            private void doRemove() {

                file.delete();
                showToast("删除");
                notifyDataSetChanged();
            }

            private void doCopy() {
                showToast("复制");

            }

            private void showToast(String msg) {
                Toast.makeText(context,msg+file.getName(),Toast.LENGTH_SHORT).show();
            }

            public void setFile(File file) {

                this.file = file;

            }
        }
    }

}
