package com.androidy.azsecuer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.androidy.azsecuer.R;
import com.androidy.azsecuer.entity.CommunicationNumberInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ljh on 2016/8/11.
 */
public class CommunicationNumberAdapter extends BaseAdapter {
    private List<CommunicationNumberInfo> datas = new ArrayList<>();
    private Context context;
    public CommunicationNumberAdapter(List<CommunicationNumberInfo> datas,Context context){
        this.datas = datas;
        this.context =context;

    }
   public class Holder{
        TextView tv_left,tv_right;

    }
    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       Holder holder= new Holder();
        if(convertView ==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.list_communication_number_item,null);
            holder.tv_right = (TextView)convertView.findViewById(R.id.list_communication_number_name);
            holder.tv_left = (TextView)convertView.findViewById(R.id.list_communication_number_tel);
            convertView.setTag(holder);
        }else {
            holder = (Holder)convertView.getTag();
        }
        holder.tv_right.setText(datas.get(position).getName());
        holder.tv_left.setText(datas.get(position).getNumber());
        return convertView;
    }
}


