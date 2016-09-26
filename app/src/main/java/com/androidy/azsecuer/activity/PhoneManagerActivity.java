package com.androidy.azsecuer.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.androidy.azsecuer.R;
import com.androidy.azsecuer.activity.base.BaseActionBarActivity;
import com.androidy.azsecuer.adapter.PhoneManagerAdapter;
import com.androidy.azsecuer.biz.PhoneManager;
import com.androidy.azsecuer.entity.PhoneManagerInfoChild;
import com.androidy.azsecuer.entity.PhoneManagerInfoGroup;
import com.androidy.azsecuer.view.BatteryProgressBarMove;

import java.util.ArrayList;

public class PhoneManagerActivity extends BaseActionBarActivity {
    private BatteryProgressBarMove pb_batteryPower;
    private BatteryPowerReceiver batteryPowerReceiver;
    private ExpandableListView expandableListView;
    private PhoneManager phoneManager;
    private TextView tv_battery_power;
    private PhoneManagerAdapter adapter;
    public class BatteryPowerReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
           final int current = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);
            tv_battery_power.setText(current+"%");
            pb_batteryPower.setProgressMoved(current);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_manager);
        initView();
        listenerView();
        loadTheData();
        batteryPowerReceiver = new BatteryPowerReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryPowerReceiver,intentFilter);
        expandableListView.setAdapter(adapter);
    }

    @Override
    public void initView() {
        setActionBarBack("手机检测");
        pb_batteryPower = (BatteryProgressBarMove)this.findViewById(R.id.pb_battery_power);
        tv_battery_power= (TextView) this.findViewById(R.id.tv_battery_power);
        expandableListView=(ExpandableListView)this.findViewById(R.id.lv_phone_manager_group);
        adapter = new PhoneManagerAdapter(this);
    }
    private void listenerView() {
        // 对ExpandableListView的展开进行监听
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                // 每次展开时,将其它的先闭合(保证每次只会有一个是展开的)
                int groupCount = adapter.getGroupCount();
                for (int i = 0; i < groupCount; i++) {
                    if (i != groupPosition) {
                        expandableListView.collapseGroup(i);
                    }
                }
            }
        });
    }

    private void loadTheData() {
        asyncLoad();
    }

    private void asyncLoad() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                phoneManager = new PhoneManager(PhoneManagerActivity.this);
                // 设备信息
                Drawable icon = getResources().getDrawable(R.drawable.setting_info_icon_version);
                final PhoneManagerInfoGroup phoneGoupInfo = new PhoneManagerInfoGroup(icon, "设备信息");
                final ArrayList<PhoneManagerInfoChild> phoneChildInfos = phoneManager.getPhoneMessage();
                // 系统信息
                icon = getResources().getDrawable(R.drawable.setting_info_icon_root);
                final PhoneManagerInfoGroup systemGoupInfo = new PhoneManagerInfoGroup(icon, "系统信息");
                final ArrayList<PhoneManagerInfoChild> systemChildInfos = phoneManager.getSystemMessage();
                // 网络信息
                icon = getResources().getDrawable(R.drawable.setting_info_icon_cpu);
                final PhoneManagerInfoGroup wifiGoupInfo = new PhoneManagerInfoGroup(icon, "网络信息");
                final ArrayList<PhoneManagerInfoChild> wifiChildInfos = phoneManager.getWIFIMessage();
                // 相机信息
                icon = getResources().getDrawable(R.drawable.setting_info_icon_camera);
                final PhoneManagerInfoGroup camerGoupInfo = new PhoneManagerInfoGroup(icon, "相机信息");
                final ArrayList<PhoneManagerInfoChild> camerChildInfos = phoneManager.getCameraMessage();
                // 存储信息
                icon = getResources().getDrawable(R.drawable.setting_info_icon_space);
                final PhoneManagerInfoGroup memoryGoupInfo = new PhoneManagerInfoGroup(icon, "存储信息");
                final ArrayList<PhoneManagerInfoChild> memoryChildInfos = phoneManager.getMemoryMessage(PhoneManagerActivity.this);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.addData(phoneGoupInfo, phoneChildInfos);
                        adapter.addData(systemGoupInfo, systemChildInfos);
                        adapter.addData(wifiGoupInfo, wifiChildInfos);
                        adapter.addData(camerGoupInfo, camerChildInfos);
                        adapter.addData(memoryGoupInfo, memoryChildInfos);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_action_bar_left_icon:
                finish();
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(batteryPowerReceiver);
    }
}
