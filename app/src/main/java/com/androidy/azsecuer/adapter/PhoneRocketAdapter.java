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
import com.androidy.azsecuer.entity.TaskInfo;
import com.androidy.azsecuer.util.PublicUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ljh on 2016/8/18.
 */
public class PhoneRocketAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private Context context;
    private List<TaskInfo> taskInfos = new ArrayList<>();

    public PhoneRocketAdapter(Context context){
        this.context =context;
        layoutInflater = LayoutInflater.from(context);
    }

    public List<TaskInfo> getTaskInfos() {
        return taskInfos;
    }

    public void setTaskInfos(List<TaskInfo> taskInfos) {
        this.taskInfos = taskInfos;
    }

    @Override
    public int getCount() {
        return taskInfos.size();
    }

    @Override
    public TaskInfo getItem(int position) {
        return taskInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder = new Holder();
        convertView = layoutInflater.inflate(R.layout.list_phone_rocket_item,null);
        holder.cb_phone_rocket = (CheckBox)convertView.findViewById(R.id.cb_phone_rocket);
        holder.img_phone_rocket_icon=(ImageView)convertView.findViewById(R.id.img_phone_rocket_icon);
        holder.tv_phone_rocket_TaskName=(TextView)convertView.findViewById(R.id.tv_phone_rocket_TaskName);
        holder.tv_phone_rocket_size=(TextView)convertView.findViewById(R.id.tv_phone_rocket_size);
        holder.tv_phone_rocket_progressName=(TextView)convertView.findViewById(R.id.tv_phone_rocket_progressName);
        final TaskInfo temp = getItem(position);
        holder.img_phone_rocket_icon.setImageDrawable(temp.getIcon());
        holder.tv_phone_rocket_TaskName.setText(temp.getTaskName());
        holder.tv_phone_rocket_size.setText(PublicUtils.formatSizeK(temp.getMemory()));
        if (temp.isUserTask()){
            holder.tv_phone_rocket_progressName.setText("用户进程");
        }else {
            holder.tv_phone_rocket_progressName.setText("系统进程");
            holder.tv_phone_rocket_progressName.setTextColor(context.getResources().getColor(R.color.red));
        }
        holder.cb_phone_rocket.setChecked(temp.isSelected());
        holder.cb_phone_rocket.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                temp.setSelected(isChecked);
            }
        });
        return convertView;
    }

    public class Holder{
        public CheckBox cb_phone_rocket;
        public ImageView img_phone_rocket_icon;
        public TextView tv_phone_rocket_TaskName;
        public TextView tv_phone_rocket_size;
        public TextView tv_phone_rocket_progressName;
    }

}
