package com.androidy.azsecuer.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.androidy.azsecuer.R;
import com.androidy.azsecuer.activity.base.BaseActionBarActivity;
import com.androidy.azsecuer.activity.db.AssetsDBManager;
import com.androidy.azsecuer.activity.db.DBManager;
import com.androidy.azsecuer.adapter.CommunicationAdapter;
import com.androidy.azsecuer.entity.CommunicationInfo;

import java.io.IOException;

public class CommunicationActivity extends BaseActionBarActivity implements AdapterView.OnItemClickListener {
    private ListView list_communication;
    private  CommunicationAdapter communicationAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communication);
        setActionBarBack("通讯大全");
        initView();
        initData();
    }

    private void initData(){
        DBManager.creatFile(this);
        if (DBManager.dbFileExists()){
            try {
                AssetsDBManager.copyDB(this,"commonnum.db",DBManager.fileDB);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        communicationAdapter = new CommunicationAdapter( DBManager.readClassList(),this);
        list_communication.setAdapter(communicationAdapter);
    }

    @Override
    public void initView() {
    list_communication=(ListView)this.findViewById(R.id.list_communication);
        list_communication.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_action_bar_left_icon:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Bundle bundle = new Bundle();
            bundle.putInt("idx" ,((CommunicationInfo)communicationAdapter.getItem(position)).getIdx() );
            startActivity(CommunicationNumberActivity.class,bundle);
    }
}
