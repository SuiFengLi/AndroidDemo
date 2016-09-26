package com.androidy.azsecuer.activity.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.androidy.azsecuer.entity.CommunicationInfo;
import com.androidy.azsecuer.entity.CommunicationNumberInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ljh on 2016/8/11.
 */
public class DBManager {
    public static File fileDB = null;

    /*
        创建
     */
    public static void creatFile(Context context) {
        String dbFileDir = "/data/data/" + context.getPackageName() + "/db";
        File file = new File(dbFileDir); //文件的路径
        Boolean mkSure = file.mkdirs(); //创建路径
        Log.i("DBManager--", "mkSure");
        fileDB = new File(file, "commonnum.db");  //创建db文件
    }

    /*
        判断
     */
    public static Boolean dbFileExists() {
        if (fileDB.length() == 0 || !fileDB.exists()) {
            return false;
        }
        return true;
    }

    /*
        读取数据
     */

    public static List<CommunicationInfo> readClassList() {
        List<CommunicationInfo> listCommunicationInfo = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(fileDB, null);

        Cursor cursor = sqLiteDatabase.rawQuery("select * from classlist", null);

        if (cursor.moveToFirst()) {
            do {
                CommunicationInfo communicationInfo = new CommunicationInfo(cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getInt(cursor.getColumnIndex("idx")));
                listCommunicationInfo.add(communicationInfo);
                Log.i("DBManager", communicationInfo + "");
            } while (cursor.moveToNext());

        } else {
            Log.i("DBManager",   "没数据");
        }
        return listCommunicationInfo;
    }

    public static List<CommunicationNumberInfo> readTable(Integer idx) {
        List<CommunicationNumberInfo> listCommunicationNumberInfo = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(fileDB, null);
        Cursor cursor = sqLiteDatabase.rawQuery("select * from table"+idx, null);

        if (cursor.moveToFirst()) {
            do {
                CommunicationNumberInfo communicationNumberInfo = new CommunicationNumberInfo(cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getString(cursor.getColumnIndex("number")));
                listCommunicationNumberInfo.add(communicationNumberInfo);
                Log.i("DBManager", communicationNumberInfo + "");
            } while (cursor.moveToNext());

        } else {
            Log.i("DBManager",   "没数据");
        }

        return listCommunicationNumberInfo;
    }


}
