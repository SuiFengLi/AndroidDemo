package com.androidy.azsecuer.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.androidy.azsecuer.R;
import com.androidy.azsecuer.activity.base.BaseActionBarActivity;
import com.androidy.azsecuer.activity.db.AssetsDBManager;
import com.androidy.azsecuer.activity.db.DBCleanManager;
import com.androidy.azsecuer.adapter.SDCleanAdapter;
import com.androidy.azsecuer.entity.SDCleanInfo;
import com.androidy.azsecuer.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SDCleanActivity extends BaseActionBarActivity {
    private List<SDCleanInfo> datas;
    private SDCleanAdapter sdCleanAdapter;
    private ProgressBar fl_pb;
    private ListView fl_lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdclean);
        loadData();
        initView();
    }


    public void asynLoadRubbishInfo(){
        datas = new ArrayList<>();
        sdCleanAdapter = new SDCleanAdapter(this);
        new Thread(){
            @Override
            public void run() {
                super.run();
                datas = DBCleanManager.readSoftDetail(SDCleanActivity.this);
                sdCleanAdapter.setData(datas);
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        sdCleanAdapter.notifyDataSetChanged();
                        fl_pb.setVisibility(View.INVISIBLE);
                        fl_lv.setVisibility(View.VISIBLE);
                        Log.i("SDCleanActivity","隐藏pb,显示lv");
                    }
                });
            }
        }.start();
    }

    public void asynDelete(){
        fl_lv.setVisibility(View.INVISIBLE);
        fl_pb.setVisibility(View.VISIBLE);
        final List<SDCleanInfo> dataNoSelect = new ArrayList<>();
        new Thread(){
            @Override
            public void run() {
                super.run();
                for (SDCleanInfo data:datas) {
                    if (data.isSelected()){
                        File file =new File(data.getFilepath());
                        FileUtil.deleteFile(file);
                    }else {
                        dataNoSelect.add(data);
                    }
                }
                sdCleanAdapter.setData(dataNoSelect);
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        sdCleanAdapter.notifyDataSetChanged();
                        fl_pb.setVisibility(View.INVISIBLE);
                        fl_lv.setVisibility(View.VISIBLE);
                    }
                });
            }
        }.start();
    }



    public void loadData(){
        asynLoadRubbishInfo();
    }

    @Override
    public void initView() {
        setActionBarBack("垃圾清理");
        this.findViewById(R.id.btn_clean).setOnClickListener(this);
        fl_lv =(ListView)this.findViewById(R.id.fl_lv);
        fl_pb=(ProgressBar)this.findViewById(R.id.fl_pb);
        DBCleanManager.creatFile(this);
        if(DBCleanManager.isFile()){
            try {
                AssetsDBManager.copyDB(this,"clearpath.db",DBCleanManager.fileDB);
                Log.i("SDCleanActivity","copy成功");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        fl_lv.setAdapter(sdCleanAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_action_bar_left_icon:
                finish();
                break;
            case R.id.btn_clean:
                asynDelete();
                Log.i("SDCleanActivity","删除成功");
                break;
        }
    }
}
