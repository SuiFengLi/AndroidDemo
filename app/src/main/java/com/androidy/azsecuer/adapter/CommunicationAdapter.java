package com.androidy.azsecuer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.androidy.azsecuer.R;
import com.androidy.azsecuer.entity.CommunicationInfo;

import java.util.List;

/**
 * Created by ljh on 2016/8/9.
 */
public class CommunicationAdapter extends BaseAdapter {
    private List<CommunicationInfo> datas;
    private Context context;
    public CommunicationAdapter(List<CommunicationInfo> datas, Context context){
        this.datas = datas;
        this.context = context;

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
        TextView textView;
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.list_communication_item,null);
            textView = (TextView)convertView.findViewById(R.id.list_communication_item);
            convertView.setTag(textView);
        }else {
            textView =(TextView)convertView.getTag();
        }
        textView.setText(datas.get(position).getName());

        return convertView;
    }
}
