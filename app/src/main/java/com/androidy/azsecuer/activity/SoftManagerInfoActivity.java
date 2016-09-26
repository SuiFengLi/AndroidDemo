package com.androidy.azsecuer.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.androidy.azsecuer.R;
import com.androidy.azsecuer.activity.base.BaseActionBarActivity;
import com.androidy.azsecuer.adapter.SoftManagerInfoAdapter;
import com.androidy.azsecuer.entity.SoftManagerInfo;
import com.androidy.azsecuer.util.TaskUtils;

import java.util.List;

public class SoftManagerInfoActivity extends BaseActionBarActivity {
    private String name;
    private ListView lv;
    private SoftManagerInfoAdapter adapter;
    private List<SoftManagerInfo> infos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soft_manager_info);
        initData();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        asyncLoadApp();
    }

    private void asyncLoadApp(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                name= SoftManagerInfoActivity.this.getIntent().getExtras().getString("idx");
                switch (name){
                    case "所有软件":
                        infos = TaskUtils.getSoftManagerInfo(SoftManagerInfoActivity.this);
                        break;
                    case "系统软件":
                        infos = TaskUtils.getSystemSoftManagerInfo(SoftManagerInfoActivity.this);
                        break;
                    case "用户软件":
                        infos = TaskUtils.getUserSoftManagerInfo(SoftManagerInfoActivity.this);
                        break;
                }
                adapter.setSoftManagerInfo(infos);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }.start();

    }

    @Override
    public void initView() {
        name= this.getIntent().getExtras().getString("idx");
        setActionBarBack(name);
        lv= (ListView)this.findViewById(R.id.lv_soft_manager_info);
        this.findViewById(R.id.btn_soft_manager_delete).setOnClickListener(this);
        adapter = new SoftManagerInfoAdapter(this);
        adapter.setSoftManagerInfo(infos);
        lv.setAdapter(adapter);
    }
    public void initData(){
         name= this.getIntent().getExtras().getString("idx");
        switch (name){
            case "所有软件":
                infos = TaskUtils.getSoftManagerInfo(this);
                break;
            case "系统软件":
                infos = TaskUtils.getSystemSoftManagerInfo(this);
                break;
            case "用户软件":
                infos = TaskUtils.getUserSoftManagerInfo(this);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_action_bar_left_icon:
                finish();
                break;
            case R.id.btn_soft_manager_delete:
            List<SoftManagerInfo> infosList =adapter.getSoftManagerInfo();
                for (SoftManagerInfo infoList:infosList){
                    if (infoList.isSelected()){
                        Intent intent = new Intent(Intent.ACTION_DELETE);
                        intent.setData(Uri.parse("package:"+infoList.getPackageName()));
                        startActivity(intent);
                    }
                }
                break;
        }
    }
}
