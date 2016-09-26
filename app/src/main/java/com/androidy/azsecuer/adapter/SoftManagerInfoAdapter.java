package com.androidy.azsecuer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidy.azsecuer.R;
import com.androidy.azsecuer.entity.SoftManagerInfo;
import com.androidy.azsecuer.entity.TaskInfo;

import java.util.List;

/**
 * Created by ljh on 2016/8/22.
 */
public class SoftManagerInfoAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private Context context;
    private List<SoftManagerInfo> softManagerInfo;
    public SoftManagerInfoAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public List<SoftManagerInfo> getSoftManagerInfo() {
        return softManagerInfo;
    }

    public void setSoftManagerInfo(List<SoftManagerInfo> taskInfos) {
        this.softManagerInfo = taskInfos;
    }

    @Override
    public int getCount() {
        return softManagerInfo.size();
    }

    @Override
    public SoftManagerInfo getItem(int position) {
        return softManagerInfo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.list_phone_rocket_item,null);
        final SoftManagerInfo temp = getItem(position);
        ((ImageView)convertView.findViewById(R.id.img_phone_rocket_icon)).setImageDrawable(temp.getIcon());
        ((TextView)convertView.findViewById(R.id.tv_phone_rocket_TaskName)).setText(temp.getTaskName());
        ((TextView)convertView.findViewById(R.id.tv_phone_rocket_size)).setText(temp.getPackageName());
        ((TextView)convertView.findViewById(R.id.tv_phone_rocket_progressName)).setText(temp.getTaskVersion());
        CheckBox checkBox  =  (CheckBox)convertView.findViewById(R.id.cb_phone_rocket);
        checkBox.setChecked(temp.isSelected());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                temp.setSelected(isChecked);
            }
        });
        return convertView;
    }
}
