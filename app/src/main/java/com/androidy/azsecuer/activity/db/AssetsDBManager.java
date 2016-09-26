package com.androidy.azsecuer.activity.db;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ljh on 2016/8/11.
 */
public class AssetsDBManager {

        public static void copyDB(Context context, String assetsPath, File toFile) throws IOException{
            InputStream inputStream = context.getAssets().open(assetsPath);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            FileOutputStream fileOutputStream = new FileOutputStream(toFile);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            byte[] bytes = new byte[1024];
            int len =0  ;
            while ( (len = bufferedInputStream.read(bytes))!=-1 ){
                bufferedOutputStream.write(bytes,0,len);
            }
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
            bufferedInputStream.close();
            inputStream.close();
            fileOutputStream.close();

        }

}
