package com.androidy.azsecuer.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidy.azsecuer.R;
import com.androidy.azsecuer.entity.SDCleanInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ljh on 2016/8/17.
 */
public class SDCleanAdapter extends BaseAdapter {
    private List<SDCleanInfo> datas = new ArrayList<>();
    private Context context;
    private LayoutInflater layoutInflater;
    private SDCleanInfo sdCleanInfo;
    public SDCleanAdapter(Context context){
        this.context =context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setData(List<SDCleanInfo> datas){
       this.datas = datas;
    }

    public List<SDCleanInfo> getData(){
        return datas;
    }
    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public SDCleanInfo getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            convertView = layoutInflater.inflate(R.layout.list_clean_item,null);
        sdCleanInfo = getItem(position);
        CheckBox checkBox =(CheckBox)convertView.findViewById(R.id.cb_clean_left);
        checkBox.setChecked(sdCleanInfo.isSelected());
        ( (ImageView)convertView.findViewById(R.id.img_clean_icon)).setImageDrawable(sdCleanInfo.getDrawable());
        ( (TextView)convertView.findViewById(R.id.tv_clean_fileSize)).setText(sdCleanInfo.getFileSize()+"");
        ((TextView)convertView.findViewById(R.id.tv_clean_fileText)).setText(sdCleanInfo.getSoftChinesename());
        checkBox.setTag(sdCleanInfo);
        checkBox.setChecked(sdCleanInfo.isSelected());
        checkBox.setOnCheckedChangeListener(checkedChangeListener);
        return convertView;
    }

    private CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            ((SDCleanInfo)buttonView.getTag()).setSelected(isChecked);
        }
    };
}
