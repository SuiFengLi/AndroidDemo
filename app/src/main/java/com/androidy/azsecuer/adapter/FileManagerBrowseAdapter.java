package com.androidy.azsecuer.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidy.azsecuer.R;
import com.androidy.azsecuer.biz.FileSearchTypeEvent;
import com.androidy.azsecuer.entity.FileInfo;
import com.androidy.azsecuer.util.PublicUtils;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ljh on 2016/8/25.
 */
public class FileManagerBrowseAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<FileInfo> dataList = new ArrayList<FileInfo>();
    private Bitmap defBitmap;
    private AbsListView absListView;
    private boolean isFling;

    public FileManagerBrowseAdapter(Context context, AbsListView absListView) {
        this.context = context;
        this.absListView = absListView;
        layoutInflater = LayoutInflater.from(context);
        defBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        absListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                isFling = false;
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    isFling = true;
                }
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }

    public void addDataToAdapter(ArrayList<FileInfo> fileInfos) {
        dataList.addAll(fileInfos);
    }

    public void setDataList(ArrayList<FileInfo> datas){
        this.dataList = datas;
    }

    public ArrayList<FileInfo> getDataList(){
        return dataList;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return dataList.size();
    }

    @Override
    public FileInfo getItem(int position) {
        // TODO Auto-generated method stub
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            convertView = layoutInflater.inflate(R.layout.list_phone_rocket_item, null);
        TextView tv_name = (TextView) convertView.findViewById(R.id.tv_phone_rocket_TaskName);
        TextView tv_time = (TextView) convertView.findViewById(R.id.tv_phone_rocket_size);
        TextView tv_size = (TextView) convertView.findViewById(R.id.tv_phone_rocket_progressName);
        ImageView iv_icon = (ImageView) convertView.findViewById(R.id.img_phone_rocket_icon);
        CheckBox cb_del = (CheckBox) convertView.findViewById(R.id.cb_phone_rocket);
        FileInfo fileInfo = getItem(position);
        // 设置文件图像
        iv_icon.setImageBitmap(getFileIconBitmap(position, fileInfo));
        tv_name.setText(fileInfo.getFile().getName());
        tv_time.setText(PublicUtils.formatDate(fileInfo.getFile().lastModified()));
        tv_size.setText(PublicUtils.formatSize(fileInfo.getFile().length()));
        cb_del.setTag(fileInfo);
        cb_del.setChecked(fileInfo.isSelect());
        cb_del.setOnCheckedChangeListener(checkedChangeListener);
        return convertView;
    }

    private CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            ( (FileInfo)buttonView.getTag()).setSelect(isChecked);
        }
    };

    /** 用来缓存文件图像 */
    private LruCache<String, Bitmap> lruCache = new LruCache<String, Bitmap>(1024 * 1024 * 4) {
        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getRowBytes() * value.getHeight();
        };
    };
    /** 用来异步加载图像文件的线程池 */
    private ExecutorService executorService = Executors.newFixedThreadPool(3);

    /** 获取此文件的图像Bitmap对象 (根据图像名称) */
    private Bitmap getFileIconBitmap(int position, FileInfo fileInfo) {
        // 快速滑动时直接返回默认图像
        if (isFling) {
            return defBitmap;
        }
        String filePath = fileInfo.getFile().getAbsolutePath();
        // #1 到缓存中取图像(文件路径做为key)
        Bitmap bitmap = lruCache.get(filePath);
        if (bitmap != null) {
            return bitmap;
        }
        // #2 缓存中没有图像
        String fileType = fileInfo.getFileType();
        // 不是图像的文件 - 根据iconName到drawable中加载
        if (!fileType.equals(FileSearchTypeEvent.TYPE_IMAGE)) {
            String iconName = fileInfo.getIconName();
            int id = context.getResources().getIdentifier(iconName, "drawable", context.getPackageName());
            bitmap = BitmapFactory.decodeResource(context.getResources(), id);
            if (bitmap == null) {
                bitmap = defBitmap;
            } else {
                lruCache.put(filePath, bitmap);
            }
            return bitmap;
        }
        // 是图像的文件 - 异步-到本地去加载
        else {
            asyncLoadImageFromDisk(position, fileInfo);
            // 返回一张默认图像
            return defBitmap;
        }
    }

    private Handler mainHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0) {
                int position = msg.arg1;
                int cfPosition = absListView.getFirstVisiblePosition();
                int clPosition = absListView.getLastVisiblePosition();
                // 图像还在屏幕上
                if (position >= cfPosition && position <= clPosition) {
                    notifyDataSetChanged();
                }
            }
        }
    };

    private void asyncLoadImageFromDisk(final int position, final FileInfo fileInfo) {
        // 线程池控制线程并发数量
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                // 本地加载图像
                String filePath = fileInfo.getFile().getAbsolutePath();
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = true;// 打开加载边界处理
                BitmapFactory.decodeFile(filePath, opts);
                int wp = opts.outWidth / 40;
                int hp = opts.outHeight / 40;
                int p = wp > hp ? wp : hp;
                if (p < 1) {
                    p = 1;
                }
                opts.inSampleSize = p;// 计算设置缩放比
                opts.inJustDecodeBounds = false;
                Bitmap bitmap = BitmapFactory.decodeFile(filePath, opts);
                if (bitmap == null) {
                    bitmap = defBitmap;
                } else {
                    lruCache.put(filePath, bitmap);
                    Message message = mainHandler.obtainMessage();
                    message.what = 0;
                    message.arg1 = position;
                    mainHandler.sendMessage(message);
                }
            }
        });
    }
}
