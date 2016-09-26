package com.androidy.azsecuer.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.androidy.azsecuer.R;
import com.androidy.azsecuer.activity.base.BaseActionBarActivity;
import com.androidy.azsecuer.biz.MemoryManager;
import com.androidy.azsecuer.biz.SpeedupManager;
import com.androidy.azsecuer.view.SpeedUpBallView;

import org.w3c.dom.Text;

/**
 * Created by ljh on 2016/8/3.
 */
public class HomeActivity extends BaseActionBarActivity {
    private SpeedUpBallView cview_speedup_outball;
    private TextView tv_speedup_inball_usageRate;// 使用率(加速球上)


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        setActionBarHome("手机管家");
        loadTheData();
    }

    @Override
    public void initView() {
        this.findViewById(R.id.home_down_telMgr).setOnClickListener(this);
        this.findViewById(R.id.home_down_rocket).setOnClickListener(this);
        this.findViewById(R.id.home_down_phoneMgr).setOnClickListener(this);
        this.findViewById(R.id.home_down_fileMgr).setOnClickListener(this);
        this.findViewById(R.id.home_down_sdClean).setOnClickListener(this);
        this.findViewById(R.id.home_down_softMgr).setOnClickListener(this);
        findViewById(R.id.iv_speedup_inball).setOnClickListener(this);
        cview_speedup_outball = (SpeedUpBallView) findViewById(R.id.cview_speedup_outball);
        tv_speedup_inball_usageRate = (TextView) findViewById(R.id.tv_speedup_inball_usageRate);
    }

    public void loadTheData() {
        long totalRam = MemoryManager.getTotalRamMemory();
        long freeRam = MemoryManager.getAvailRamMemory(HomeActivity.this);
        long useRam = totalRam - freeRam;
        final int useRamPorp100 = (int) ((float) useRam / (float) totalRam * 100);
        final int useRamProp360 = (int) ((float) useRam / (float) totalRam * 360);
        // UI设置
        tv_speedup_inball_usageRate.setText(useRamPorp100 + ""); // 使用率(%)
        cview_speedup_outball.setAngleWithAnim(useRamProp360); // 加速球(角度)
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_action_bar_right_icon:
                    startActivity(SettingActivity.class);
                break;
            case R.id.home_down_telMgr:
                    startActivity(CommunicationActivity.class);
                break;
            case R.id.home_down_phoneMgr:
                    startActivity(PhoneManagerActivity.class);
                break;
            case R.id.home_down_sdClean:
                    startActivity(SDCleanActivity.class);
                break;
            case R.id.home_down_rocket:
                    startActivity(PhoneRocketActivity.class);
                break;
            case R.id.home_down_softMgr:
                    startActivity(SoftManagerActivity.class);
                break;
            case R.id.home_down_fileMgr:
                    startActivity(FileManagerActivity.class);
                break;
            // 加速球
            case R.id.iv_speedup_inball:
                asynClearRunApp();
                break;
        }

    }
    private boolean isClearRuning;
    private void asynClearRunApp() {
        if (isClearRuning) {
            return;
        }
        isClearRuning = true;
        tv_speedup_inball_usageRate.setText("00");
        // 按下加速: #1 进行加速(循环旋转)
        final SpeedUpBallView.MyTimer myTimer = cview_speedup_outball.startWhileAnim();
        // 之后，开线程执行加速工作(1获取-2kill-3重计算运行内存大小)
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 1 一键清理
                SpeedupManager speedupManager = SpeedupManager.getInstance(HomeActivity.this);
                speedupManager.defSpeedup();
                myTimer.endWhileAnim();
                // 2 获取运行内存信息,计算换算出比例
                long totalRam = MemoryManager.getTotalRamMemory();
                long freeRam = MemoryManager.getAvailRamMemory(HomeActivity.this);
                long useRam = totalRam - freeRam;
                final int useRamPorp100 = (int) ((float) useRam / (float) totalRam * 100);
                final int useRamProp360 = (int) ((float) useRam / (float) totalRam * 360);
                // 3 UI设置
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_speedup_inball_usageRate.setText(useRamPorp100 + ""); // 使用率(%)
                        cview_speedup_outball.setAngleWithAnim(useRamProp360); // 加速球(角度)
                    }
                });
                isClearRuning = false;
            }
        }).start();
        // 线程这样随意处理，不做管理其实还是很危险的
    }
}
