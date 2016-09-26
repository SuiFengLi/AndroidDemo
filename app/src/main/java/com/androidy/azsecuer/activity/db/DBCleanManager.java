package com.androidy.azsecuer.activity.db;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

import com.androidy.azsecuer.R;
import com.androidy.azsecuer.entity.SDCleanInfo;
import com.androidy.azsecuer.util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ljh on 2016/8/17.
 */
public class DBCleanManager {
    public static File fileDB =null;
    /*
        创建
     */
    public static void creatFile(Context context){
        String fileDir = "/data/data/"+context.getPackageName()+"/db";
        File file = new File(fileDir);
        file.mkdirs();
        fileDB =new File(file,"clearpath.db");
        Log.i("DBCleanManager","创建成功--"+fileDB);
    }

    /*
        判断
     */
    public static boolean isFile(){
        if (fileDB.length()==0|| !fileDB.exists()){
            return false;
        }
        return true;
    }

    /*
        读取数据
     */
    public static List<SDCleanInfo> readSoftDetail(Context context){
        List<SDCleanInfo> list = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(fileDB,null);
        Cursor cursor = sqLiteDatabase.rawQuery("select * from softdetail",null);
        if (cursor.moveToFirst()){
            do{
                String chineseName =cursor.getString(cursor.getColumnIndex("softChinesename"));
                String apkname =cursor.getString(cursor.getColumnIndex("apkname"));
                String filepath = cursor.getString(cursor.getColumnIndex("filepath"));
                File cacheFile = new File(Environment.getExternalStorageDirectory()+filepath);
                long size = FileUtil.getFileSize(cacheFile);
                    Drawable drawable=null;
                    try {
                        drawable = context.getPackageManager().getApplicationIcon(apkname);
                    } catch (PackageManager.NameNotFoundException e) {
                        drawable=context.getResources().getDrawable(R.mipmap.ic_launcher);
                        e.printStackTrace();
                    }
                if (size!=0){
                    SDCleanInfo sdCleanInfo = new SDCleanInfo(chineseName,apkname,cacheFile.getPath(),drawable,size);
                    list.add(sdCleanInfo);
                    Log.i("DBCleanManager",sdCleanInfo+"");
                }

            }while (cursor.moveToNext());
        }else {
            Log.i("DBCleanManager","没有数据");
        }
        return list;
    }


}
