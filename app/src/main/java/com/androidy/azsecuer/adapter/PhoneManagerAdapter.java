package com.androidy.azsecuer.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidy.azsecuer.R;
import com.androidy.azsecuer.entity.PhoneManagerInfoChild;
import com.androidy.azsecuer.entity.PhoneManagerInfoGroup;

import java.util.HashMap;
import java.util.ArrayList;

/**
 * Created by ljh on 2016/8/16.
 */
public class PhoneManagerAdapter extends BaseExpandableListAdapter {
    private LayoutInflater layoutInflater;
    private ArrayList<PhoneManagerInfoGroup> groupListData = new ArrayList<>();
    private HashMap<String,ArrayList<PhoneManagerInfoChild>> childListData = new HashMap<>();

    public PhoneManagerAdapter(Context context){
        layoutInflater = LayoutInflater.from(context);
    }
    public void addData(PhoneManagerInfoGroup group,ArrayList<PhoneManagerInfoChild> childs){
        groupListData.add(group);
        childListData.put(group.getText(),childs);
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getGroupCount() {
        return groupListData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childListData.get(groupListData.get(groupPosition).getText()).size();
    }

    @Override
    public PhoneManagerInfoGroup getGroup(int groupPosition) {
        return groupListData.get(groupPosition);
    }

    @Override
    public PhoneManagerInfoChild getChild(int groupPosition, int childPosition) {
        return childListData.get(groupListData.get(groupPosition).getText()).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.list_phone_manager_group,null);
        PhoneManagerInfoGroup temp = getGroup(groupPosition);
        ( (ImageView)convertView.findViewById(R.id.img_phoneManager_group_icon)).setImageDrawable(temp.getIcon());
        ( (TextView)convertView.findViewById(R.id.tv_phoneManager_group_title)).setText(temp.getText());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.list_phone_manager_child,null);
        PhoneManagerInfoChild temp = getChild(groupPosition, childPosition);
        ( (TextView)convertView.findViewById(R.id.tv_phoneManager_child_left)).setText(temp.getTitle() );
        ( (TextView)convertView.findViewById(R.id.tv_phoneManager_child_right)).setText(temp.getText() );
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


}
