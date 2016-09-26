package com.androidy.azsecuer.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidy.azsecuer.R;
import com.androidy.azsecuer.activity.base.BaseActionBarActivity;
import com.androidy.azsecuer.adapter.PhoneRocketAdapter;
import com.androidy.azsecuer.biz.MemoryManager;
import com.androidy.azsecuer.entity.TaskInfo;
import com.androidy.azsecuer.util.PublicUtils;
import com.androidy.azsecuer.util.TaskUtils;
import com.androidy.azsecuer.view.BatteryProgressBarMove;

import java.util.ArrayList;
import java.util.List;

public class PhoneRocketActivity extends BaseActionBarActivity {
    private List<TaskInfo> taskInfos = new ArrayList<>();
    private PhoneRocketAdapter phoneRocketAdapter;
    private BatteryProgressBarMove pb_phone_rocket;
    private TextView tv_phone_rocket_memory;
    private Button btn_phone_rocket_show;
    private ProgressBar fl_pb;
    private ListView fl_lv;
    private CheckBox cb_phone_rocket_selectAll;
    private ActivityManager activityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_rocket);
        loadData();
        initView();
    }

    @Override
    public void initView() {
        setActionBarBack("手机加速");
        pb_phone_rocket = (BatteryProgressBarMove)this.findViewById(R.id.pb_phone_rocket);
        tv_phone_rocket_memory = (TextView)this.findViewById(R.id.tv_phone_rocket_memory);
        cb_phone_rocket_selectAll = (CheckBox)this.findViewById(R.id.cb_phone_rocket_select_all);
        btn_phone_rocket_show=(Button) this.findViewById(R.id.btn_phone_rocket_show);
        ( (TextView)this.findViewById(R.id.tv_phone_rocket_name)).setText(Build.BRAND.toUpperCase());
        this.findViewById(R.id.btn_phone_rocket_delete).setOnClickListener(this);
        btn_phone_rocket_show.setOnClickListener(this);
        cb_phone_rocket_selectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                asynSelect(isChecked);
            }
        });
        fl_lv = (ListView)this.findViewById(R.id.fl_lv);
        fl_pb = (ProgressBar)this.findViewById(R.id.fl_pb);
        updateMemory();
        fl_lv.setAdapter(phoneRocketAdapter);
    }
    public void loadData(){
        asynLoadTaskInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_action_bar_left_icon:
                finish();
                break;
            case R.id.btn_phone_rocket_delete:
                asynDelete();
                cb_phone_rocket_selectAll.setChecked(false);
                break;
            case R.id.btn_phone_rocket_show:
                if (btn_phone_rocket_show.getText().equals("显示系统进程")){
                    phoneRocketAdapter.setTaskInfos(TaskUtils.getTaskInfos(this));
                    phoneRocketAdapter.notifyDataSetChanged();
                    btn_phone_rocket_show.setText("隐藏系统进程");
                }else {
                    phoneRocketAdapter.setTaskInfos(TaskUtils.getUserTaskInfos(this));
                    phoneRocketAdapter.notifyDataSetChanged();
                    btn_phone_rocket_show.setText("显示系统进程");
                }
                cb_phone_rocket_selectAll.setChecked(false);
                break;
        }
    }

    private void updateMemory(){
        final long total = MemoryManager.getTotalRamMemory();
        final long avail = MemoryManager.getAvailRamMemory(this);
        final long use = total - avail;
        final int percent =  (int)((float)use/(float)total*100);
        pb_phone_rocket.setMax(100);
        pb_phone_rocket.setProgressMoved(percent);
        StringBuffer sb = new StringBuffer();
        sb.append("可用内存:");
        sb.append(PublicUtils.formatSize(avail)+"/");
        sb.append(PublicUtils.formatSize(total));
        tv_phone_rocket_memory.setText(sb.toString());
    }

    private void asynLoadTaskInfo(){
        phoneRocketAdapter = new PhoneRocketAdapter(this);
        new Thread(){
            @Override
            public void run() {
                super.run();
                taskInfos = TaskUtils.getUserTaskInfos(PhoneRocketActivity.this);
                phoneRocketAdapter.setTaskInfos(taskInfos);
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        phoneRocketAdapter.notifyDataSetChanged();
                        fl_lv.setVisibility(View.VISIBLE);
                        fl_pb.setVisibility(View.INVISIBLE);
                    }
                });
            }
        }.start();
    }

    public void asynDelete(){
        fl_pb.setVisibility(View.VISIBLE);
        fl_lv.setVisibility(View.INVISIBLE);
        final List<TaskInfo> dataNoSelect = new ArrayList<>();
        final List<TaskInfo> taskInfoShow = phoneRocketAdapter.getTaskInfos();
        activityManager = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
        new Thread(){
            @Override
            public void run() {
                super.run();
                for (TaskInfo taskInfo:taskInfoShow){
                    if (taskInfo.isSelected()){
                        activityManager.killBackgroundProcesses(taskInfo.getPackageName());
                    }else {
                        dataNoSelect.add(taskInfo);
                    }
                }
                phoneRocketAdapter.setTaskInfos(dataNoSelect);
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        phoneRocketAdapter.notifyDataSetChanged();
                        fl_lv.setVisibility(View.VISIBLE);
                        fl_pb.setVisibility(View.INVISIBLE);
                    }
                });
            }
        }.start();
    }

    public void asynSelect(boolean isSelect){
      final  boolean isChecked = isSelect;
     final  List<TaskInfo> taskInfoSelect = phoneRocketAdapter.getTaskInfos();
        new Thread(){
            @Override
            public void run() {
                super.run();
                for(TaskInfo taskInfo:taskInfoSelect){
                    taskInfo.setSelected(isChecked);
                }
                phoneRocketAdapter.setTaskInfos(taskInfoSelect);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        phoneRocketAdapter.notifyDataSetChanged();
                    }
                });
            }
        }.start();
    }


}
