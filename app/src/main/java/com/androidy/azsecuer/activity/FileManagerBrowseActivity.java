package com.androidy.azsecuer.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidy.azsecuer.R;
import com.androidy.azsecuer.activity.base.BaseActionBarActivity;
import com.androidy.azsecuer.adapter.FileManagerBrowseAdapter;
import com.androidy.azsecuer.biz.FileSearchManager;
import com.androidy.azsecuer.entity.FileInfo;
import com.androidy.azsecuer.util.FileUtil;
import com.androidy.azsecuer.util.PublicUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class FileManagerBrowseActivity extends BaseActionBarActivity implements CompoundButton.OnCheckedChangeListener {

    private TextView tv_filebrowse_number;
    private TextView tv_filebrowse_size;
    private ListView lv_file_manager_browse;
    private Button btn_delfile;
    private CheckBox cb_deffile;
    private FileManagerBrowseAdapter browseAdapter;
    private FileSearchManager searchManager;
    /** 文件类型(key) */
    private String fileType;
    /** 所有文件分类集合 */
    private HashMap<String, ArrayList<FileInfo>> fileInfos;
    /** 所有文件大小集合 */
    private HashMap<String, Long> fileSizes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_manager_browse);
        // 初始加载当前页面控件
        initView();
        // 加载数据
        loadTheData();
    }

    @Override
    public void initView() {
        setActionBarBack("文件浏览");
        tv_filebrowse_number = (TextView) findViewById(R.id.tv_file_manager_browse_number);
        tv_filebrowse_size = (TextView) findViewById(R.id.tv_file_manager_browse_size);
        lv_file_manager_browse = (ListView) findViewById(R.id.lv_file_manager_browse);
        btn_delfile = (Button) findViewById(R.id.btn_file_manager_browse_delete);
        cb_deffile = (CheckBox) findViewById(R.id.cb_file_manager_browse_all);
        cb_deffile.setOnCheckedChangeListener(this);
        btn_delfile.setOnClickListener(this);

    }


    private void loadTheData() {
        // 取得FileManagerActivity传入的文件类型
        fileType = getIntent().getStringExtra("fileType");
        // 取得文件列表数据信息
        searchManager = FileSearchManager.getInstance(false);
        fileInfos = searchManager.getFileInfos(); // 文件实体集合(Map)
        fileSizes = searchManager.getFileSizes(); // 文件大小集合(Map)
        long size = fileSizes.get(fileType);
        long count = fileInfos.get(fileType).size();
        // 将文件列表数量和大小分别设置到对应的文件控件上
        tv_filebrowse_number.setText(count+"");
        tv_filebrowse_size.setText(PublicUtils.formatSize(size));
        // 将文件实体集合数据适配到文件列表控件上
        browseAdapter = new FileManagerBrowseAdapter(this, lv_file_manager_browse);
        browseAdapter.addDataToAdapter(fileInfos.get(fileType));
        lv_file_manager_browse.setAdapter(browseAdapter);
        lv_file_manager_browse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("FileBrowse","11111");
                FileInfo fileInfo = browseAdapter.getItem(position);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.fromFile(fileInfo.getFile());
                String type = fileInfo.getOpenType();
                intent.setDataAndType(data, type);
                Log.i("FileBrowse",type+"--------"+data);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // TODO Auto-generated method stub
        int count = browseAdapter.getCount();
        for (int i = 0; i < count; i++) {
            browseAdapter.getItem(i).setSelect(isChecked);
        }
        browseAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        int viewID = v.getId();
        switch (viewID) {
            case R.id.iv_action_bar_left_icon:
                finish();
                break;
            case R.id.btn_file_manager_browse_delete:
                final ArrayList<FileInfo> infosNoSelects = new ArrayList<FileInfo>();
               new Thread(){
                   @Override
                   public void run() {
                       super.run();
                       for (FileInfo info:browseAdapter.getDataList()){
                           if (info.isSelect()){
                               File file =new File(info.getFile().getPath());
                               FileUtil.deleteFile(file);
                           }else {
                               infosNoSelects.add(info);
                           }
                       }
                       long sizeNoSelect = 0;
                       long countNoSelect = 0;
                       for (FileInfo info:infosNoSelects){
                           sizeNoSelect = sizeNoSelect + info.getFile().length();
                           countNoSelect++;
                       }
                      final String sizeStr = PublicUtils.formatSize(sizeNoSelect);
                      final String countStr = String.valueOf(countNoSelect);
                       // 将文件列表数量和大小分别设置到对应的文件控件上
                       browseAdapter.setDataList(infosNoSelects);
                       runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               tv_filebrowse_number.setText(countStr+"");
                               tv_filebrowse_size.setText(sizeStr+"");
                               browseAdapter.notifyDataSetChanged();
                               cb_deffile.setChecked(false);
                           }
                       });
                   }
               }.start();
                break;
        }
    }


}
