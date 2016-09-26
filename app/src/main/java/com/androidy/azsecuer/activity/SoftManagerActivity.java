package com.androidy.azsecuer.activity;


import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.androidy.azsecuer.R;
import com.androidy.azsecuer.activity.base.BaseActionBarActivity;
import com.androidy.azsecuer.adapter.CommunicationAdapter;
import com.androidy.azsecuer.biz.MemoryManager;
import com.androidy.azsecuer.biz.NewMemoryManager;
import com.androidy.azsecuer.entity.CommunicationInfo;
import com.androidy.azsecuer.util.PublicUtils;
import com.androidy.azsecuer.view.BatteryProgressBarMove;
import com.androidy.azsecuer.view.PiecharView;

import java.util.ArrayList;
import java.util.List;

public class SoftManagerActivity extends BaseActionBarActivity implements AdapterView.OnItemClickListener {
    private CommunicationAdapter adapter;
    private ListView lv_soft_manager;
    private BatteryProgressBarMove pb_built_space,pb_external_space;
    private TextView tv_built_space,tv_external_space,tv_include_built_space,tv_include_external_space;
    private PiecharView cview_softwareservice_piechart; // 自定义饼图
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soft_manager);
        initView();
        initData();
    }

    @Override
    public void initView() {
        setActionBarBack("软件管理");
        lv_soft_manager =(ListView)this.findViewById(R.id.lv_soft_manager);
        pb_built_space=(BatteryProgressBarMove)this.findViewById(R.id.pb_built_space);
        pb_external_space=(BatteryProgressBarMove)this.findViewById(R.id.pb_external_space);
        tv_built_space=(TextView)this.findViewById(R.id.tv_built_space);
        tv_external_space=(TextView)this.findViewById(R.id.tv_external_space);
        tv_include_built_space=(TextView)this.findViewById(R.id.tv_include_built_space);
        tv_include_external_space= (TextView)this.findViewById(R.id.tv_include_external_space);
        cview_softwareservice_piechart=(PiecharView)this.findViewById(R.id.cview_softwareservice_piechart);
        lv_soft_manager.setOnItemClickListener(this);
        updateMemory();
    }
    public void initData(){
        List<CommunicationInfo> infos = new ArrayList<>();
        infos.add(new CommunicationInfo("所有软件",1));
        infos.add(new CommunicationInfo("系统软件",2));
        infos.add(new CommunicationInfo("用户软件",3));
        adapter = new CommunicationAdapter(infos,this);
        lv_soft_manager.setAdapter(adapter);
    }
    public void updateMemory(){
        long totalInRom = NewMemoryManager.getTotalMemorySize(this); // 系统总内存
        long freeInRom = NewMemoryManager.getAvailableMemory(this);// 系统空闲内存
        long usedInRom = totalInRom - freeInRom; //系统已使用内存
        long totalOutRom = NewMemoryManager.getTotalExternalMemorySize();// SDCARD全部
        long freeOutRom = NewMemoryManager.getAvailableExternalMemorySize();// SDCARD空闲
        long usedOutRom = totalOutRom - freeOutRom;// SDCARD已使用
        int usedInRomProp100 = (int) ((float) usedInRom / totalInRom * 100);
        int usedOutRomProp100 = (int) ((float) usedOutRom / totalOutRom * 100);
        // 计算出内置和外置在整机上分别所占存储空间比例(饼图)
        long totalRom = totalInRom + totalOutRom;
        float totalInRomProp = (float) totalInRom / totalRom;
        int totalInRomProp360 = (int) (totalInRomProp * 360);;
        int totalInRomProp100 = (int) (totalInRomProp * 100);
        int totalOutRomProp360 = 360 - totalInRomProp360;
        int totalOutRomProp100 = 100 - totalInRomProp100;
        // 设置饼图UI
        int inRomColor = getResources().getColor(R.color.colorPrimary);
        int outRomColor = getResources().getColor(R.color.batteryPower_progress);
        int[][] datas = new int[][] { { inRomColor, totalInRomProp360, 0 }, { outRomColor, totalOutRomProp360, 0 }, };
        cview_softwareservice_piechart.setAngleWithAnim2(datas);

        tv_include_built_space.setText(PublicUtils.formatSize(totalInRom));
        tv_include_external_space.setText(PublicUtils.formatSize(totalOutRom));
        pb_built_space.setProgressMoved(usedInRomProp100);
        pb_external_space.setProgressMoved(usedOutRomProp100);
        tv_built_space.setText(PublicUtils.formatSize(usedInRom)+"/"+PublicUtils.formatSize(totalInRom));
        tv_external_space.setText(PublicUtils.formatSize(usedOutRom)+"/"+PublicUtils.formatSize(totalOutRom));
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle bundle = new Bundle();
        CommunicationInfo info = (CommunicationInfo)adapter.getItem(position);
        bundle.putString("idx",info.getName());
        startActivity(SoftManagerInfoActivity.class,bundle);
    }
}
