package com.androidy.azsecuer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidy.azsecuer.R;
import com.androidy.azsecuer.entity.FileManagerInfo;
import com.androidy.azsecuer.util.PublicUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ljh on 2016/8/24.
 */
public class FileManagerAdapter extends BaseAdapter {

    private List<FileManagerInfo> dates = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private Context context;
    public FileManagerAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }
    public void setData(List<FileManagerInfo> dates){
        this.dates = dates;
    }

    public List<FileManagerInfo> getData(){
        return dates;
    }

    public void addData(FileManagerInfo fileManagerInfo) {
        if (fileManagerInfo != null) {
            dates.add(fileManagerInfo);
        }
    }

    @Override
    public int getCount() {
        return dates.size();
    }

    @Override
    public FileManagerInfo getItem(int position) {
        return dates.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.list_file_manager_item,null);
        TextView tv_file_manager_type_name =(TextView)convertView.findViewById(R.id.tv_file_manager_type_name);
        TextView tv_file_manager_size=(TextView) convertView.findViewById(R.id.tv_file_manager_size);
        ProgressBar pb_file_manager_item = (ProgressBar) convertView.findViewById(R.id.pb_file_manager_item);
        ImageView iv_file_manager_item=(ImageView) convertView.findViewById(R.id.iv_file_manager_item);
        FileManagerInfo temp = getItem(position);
        tv_file_manager_type_name.setText(temp.getTypeName());
        if (!temp.isLoading()){
            pb_file_manager_item.setVisibility(View.VISIBLE);
            iv_file_manager_item.setVisibility(View.GONE);
            tv_file_manager_size.setText("计算中");
        }else {
            pb_file_manager_item.setVisibility(View.GONE);
            iv_file_manager_item.setVisibility(View.VISIBLE);
            tv_file_manager_size.setText(PublicUtils.formatSize(temp.getSize()));
        }

        return convertView;
    }
}
