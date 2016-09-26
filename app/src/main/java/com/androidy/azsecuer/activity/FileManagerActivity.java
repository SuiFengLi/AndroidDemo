package com.androidy.azsecuer.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.androidy.azsecuer.R;
import com.androidy.azsecuer.activity.base.BaseActionBarActivity;
import com.androidy.azsecuer.adapter.FileManagerAdapter;
import com.androidy.azsecuer.biz.FileSearchManager;
import com.androidy.azsecuer.biz.FileSearchTypeEvent;
import com.androidy.azsecuer.entity.FileManagerInfo;
import com.androidy.azsecuer.util.PublicUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileManagerActivity extends BaseActionBarActivity implements AdapterView.OnItemClickListener{
    private ListView listview;
    private FileManagerAdapter adapter;
    private FileSearchManager searchManager;
    private HashMap<String, Long> fileSizes;
    private boolean isOnItem;
    private TextView tv_total_size;
    private Button btn_search;
    private long totalSize; // 文件总大小(注意每次进入页面时的重置)

    private Thread searchThread;

    private Handler mainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                // onSearchStart 当开始搜索时更新UI
                case 0:
                    btn_search.setClickable(false);
                    btn_search.setText("搜索中");
                    break;
                // onSearching 每搜索到一个文件更新UI
                case 1:
                    // String fileType = (String) msg.obj;
                    // long size = fileSizes.get(fileType);
                    tv_total_size.setText(PublicUtils.formatSize(totalSize));
                    break;
                // onSearchEnd 搜索结束更新UI
                case 2:
                    int searchLocationRom = msg.arg1;
                    switch (searchLocationRom) {
                        // 内置空间搜索结束后UI更新 (click为true,可再进行深度搜索)
                        case 0:
                            btn_search.setClickable(true);
                            btn_search.setText("深度搜索");
                            break;
                        // 外置空间搜索结束后UI更新(click为false,搜索完毕)
                        case 1:
                            btn_search.setClickable(false);
                            btn_search.setText("搜索完毕");
                            break;
                    }
                    int count = adapter.getCount();
                    for (int i = 0; i < count; i++) {
                        FileManagerInfo info = adapter.getItem(i);
                        info.setSize(fileSizes.get(info.getFileType()));
                        info.setLoading(true);
                    }
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_manager);
        // 初始加载当前页面控件
        initView();
        // 控件监听
        listenerView();
        // 加载数据
        loadTheData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 离开当前页面时，中止搜索
        searchManager.setStopSearch(true);
        // 离开当前页面时，中止线程
        if (searchThread != null) {
            searchThread.interrupt();
            searchThread = null;
        }
        System.gc();
    }

    public void initView() {
        setActionBarBack("文件管理");
        listview = (ListView) findViewById(R.id.list_file_manager);
        tv_total_size = (TextView) findViewById(R.id.tv_file_manager_total_size);
        btn_search = (Button) findViewById(R.id.btn_file_manager_search);
    }

    private void listenerView() {
        listview.setOnItemClickListener(this);
        btn_search.setOnClickListener(this);
        // 注意：设置监听中会将clickable默认设置为true
        // 此处是：进入文件管理即开始搜索内置文件,所以默认"深度搜索"不可click
        btn_search.setClickable(false);
    }

    private void loadTheData() {
        adapter = new FileManagerAdapter(this);
        adapter.addData(new FileManagerInfo("文本文件", FileSearchTypeEvent.TYPE_TXT));
        adapter.addData(new FileManagerInfo("图像文件", FileSearchTypeEvent.TYPE_IMAGE));
        adapter.addData(new FileManagerInfo("APK文件", FileSearchTypeEvent.TYPE_APK));
        adapter.addData(new FileManagerInfo("视屏文件", FileSearchTypeEvent.TYPE_VIDEO));
        adapter.addData(new FileManagerInfo("音屏文件", FileSearchTypeEvent.TYPE_AUDIO));
        adapter.addData(new FileManagerInfo("压缩文件", FileSearchTypeEvent.TYPE_ZIP));
        adapter.addData(new FileManagerInfo("其它文件", FileSearchTypeEvent.TYPE_OTHER));
        listview.setAdapter(adapter);
        totalSize = 0;
        searchManager = FileSearchManager.getInstance(true);
        fileSizes = searchManager.getFileSizes();
        // 异步搜索内置文件
        asyncSearchInRomFile();
    }

    // 异步搜索内置文件
    private void asyncSearchInRomFile() {
        searchThread = new Thread(new Runnable() {
            @Override
            public void run() {
                searchManager.setOnFileSearchListener(searchListener);
                searchManager.startSearchFromInRom(FileManagerActivity.this);
            }
        });
        searchThread.start();
    }

    // 异步搜索外置文件
    private void asyncSearchOutRomFile() {
        searchThread = new Thread(new Runnable() {
            @Override
            public void run() {
                searchManager.setOnFileSearchListener(searchListener);
                searchManager.startSearchFromOutRom(FileManagerActivity.this);
            }
        });
        searchThread.start();
    }

    /** 搜索监听,回调接口 */
    private FileSearchManager.OnFileSearchListener searchListener = new FileSearchManager.OnFileSearchListener() {

        @Override
        public void onSearchStart(int searchLocationRom) {
            // 线程通信
            mainHandler.sendEmptyMessage(0); // onSearchStart
        }

        @Override
        public void onSearching(String fileType, long totalSize) {
            // 保存文件总大小(全局)
            FileManagerActivity.this.totalSize = totalSize;
            // 线程通信
            Message message = mainHandler.obtainMessage();
            message.what = 1; // onSearching
            message.obj = fileType;
            mainHandler.sendMessage(message);
        }

        @Override
        public void onSearchEnd(boolean isExceptionEnd, int searchLocationRom) {
            // 线程通信
            Message message = mainHandler.obtainMessage();
            message.what = 2;
            message.arg1 = searchLocationRom;
            mainHandler.sendMessage(message); // onSearchEnd

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if(isOnItem){
            loadTheData();
            isOnItem = false;
        }


    }

    @Override
    public void onClick(View v) {
        int viewID = v.getId();
        switch (viewID) {
            case R.id.iv_action_bar_left_icon:
                finish();
                break;
            case R.id.btn_file_manager_search:
                // 按下"深度搜索"后,click失效
                btn_search.setClickable(false);
                // 按下"深度搜索"后,更新Adapter上UI
                int count = adapter.getCount();
                for (int i = 0; i < count; i++) {
                    adapter.getItem(i).setLoading(false);
                }
                adapter.notifyDataSetChanged();
                // 执行"深度搜索" - 异步搜索外置文件
                asyncSearchOutRomFile();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        isOnItem = true;
        FileManagerInfo classInfo = adapter.getItem(position);
        String fileType = classInfo.getFileType();
        Intent intent = new Intent(this, FileManagerBrowseActivity.class);
        intent.putExtra("fileType", fileType);
        startActivity(intent);
    }
}
