package com.androidy.azsecuer.util;

import android.content.Context;

import java.io.File;

/**
 * Created by ljh on 2016/8/18.
 */
public class FileUtil {

    public static long getFileSize(File file){
        long size = 0;
        if (file.exists()){
            if (file.isDirectory()){
                File[] files = file.listFiles();
                for (File f:files) {
                    if (f.exists()){
                        if (f.isDirectory()){
                            return size+=getFileSize(f);
                        } else {
                            return size+=f.length();
                        }
                    }
                }
            }else {
                return size+=file.length();
            }
        }
        return size;
    }

    public  static void deleteFile(File file){
        if (file.exists()){
            if (file.isDirectory()){
                File[] files = file.listFiles();
                for (File f:files) {
                    if (f.isDirectory()){
                        deleteFile(f);
                    }else {
                        f.delete();
                    }
                }
            }else {
                file.delete();
            }
        }

    }

}
