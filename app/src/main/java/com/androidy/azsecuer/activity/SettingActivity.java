package com.androidy.azsecuer.activity;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.androidy.azsecuer.R;
import com.androidy.azsecuer.activity.base.BaseActionBarActivity;
import com.androidy.azsecuer.notification.MainNotification;
import com.androidy.azsecuer.util.LogUtil;


public class SettingActivity extends BaseActionBarActivity implements CompoundButton.OnCheckedChangeListener {
    private ToggleButton tbtn01_setting_notify_action,tbtn02_setting_notify_action;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private final String KEY_AUTOSTART="CheckAutoStart",KEY_NOTIFYACTION="CheckNotifyAction";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        sharedPreferences = this.getSharedPreferences("SettingPreferences",this.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        setActionBarBack("设置");
        initView();
    }

    @Override
    public void initView() {
        tbtn01_setting_notify_action=(ToggleButton)this.findViewById(R.id.tbtn01_setting_notify_action);
        tbtn02_setting_notify_action=(ToggleButton)this.findViewById(R.id.tbtn02_setting_notify_action);
        tbtn01_setting_notify_action.setChecked(sharedPreferences.getBoolean(KEY_AUTOSTART,false));
        tbtn02_setting_notify_action.setChecked(sharedPreferences.getBoolean(KEY_NOTIFYACTION,false));
        tbtn01_setting_notify_action.setOnCheckedChangeListener(this);
        tbtn02_setting_notify_action.setOnCheckedChangeListener(this);
        LogUtil.p("SettingActivity","读取并设置");
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
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.tbtn01_setting_notify_action:
                editor.putBoolean(KEY_AUTOSTART,isChecked);

                LogUtil.p("SettingActivity","写入");
                break;
            case R.id.tbtn02_setting_notify_action:
                editor.putBoolean(KEY_NOTIFYACTION,isChecked);
                if(isChecked){
                    MainNotification.openNotification(this);
                    LogUtil.p("SettingActivity","添加");
                }else {
                    MainNotification.closeNotification(this);
                    LogUtil.p("SettingActivity","取消");
                }

//                NotificationManager notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
//               if(isChecked) {
//                   Notification notification = new Notification.Builder(this).setContentTitle("安全管家")
//                           .setContentText("点击进入主页")
//                           .setSmallIcon(R.drawable.ic_launcher)
//                           .setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, HomeActivity.class), PendingIntent.FLAG_UPDATE_CURRENT))
//                           .build();
//                   notificationManager.notify(10, notification);
//                   LogUtil.p("SettingActivity","添加");
//               }else {
//                   notificationManager.cancel(10);
//                   LogUtil.p("SettingActivity","取消");
//               }
                break;
        }
        editor.commit();
        LogUtil.p("SettingActivity","提交");
    }
}
